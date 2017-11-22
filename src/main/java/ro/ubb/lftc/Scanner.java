package ro.ubb.lftc;

import ro.ubb.lftc.model.finiteautomata.FiniteAutomaton;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomatonFromFile;
import ro.ubb.lftc.model.programscanner.CodingTable;
import ro.ubb.lftc.model.programscanner.CustomException;
import ro.ubb.lftc.model.programscanner.ProgramInternalForm;
import ro.ubb.lftc.model.programscanner.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Scanner class uses all the other classes. In the constructor it initializes the CodingTable and creates the
 * symbolTable and the programInternalForm and the finite automatons defined.
 */
public class Scanner {
	private static final String CONSTANT = "constant";
	private static final String IDENTIFIER = "identifier";
	private ProgramInternalForm programInternalForm;
	private SymbolTable symbolTable;
	private CodingTable codingTable;
	private FiniteAutomaton integerFiniteAutomaton;
	private FiniteAutomaton floatFiniteAutomaton;
	private FiniteAutomaton identifierFiniteAutomaton;
	private FiniteAutomaton tokenFiniteAutomaton;

	public Scanner() throws CustomException {
		codingTable = new CodingTable("src/main/resources/lab1/codes.txt");
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();
		//read the automatons
		integerFiniteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_integer.txt");
		integerFiniteAutomaton.readAutomaton();
		floatFiniteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_float.txt");
		floatFiniteAutomaton.readAutomaton();
		identifierFiniteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_identifier.txt");
		identifierFiniteAutomaton.readAutomaton();
		tokenFiniteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_token.txt");
		tokenFiniteAutomaton.readAutomaton();
	}

	/**
	 * It fills the programInternalForm and the symbolTable with the
	 * associated data
	 *
	 * @param filename - the name of the file with the program we scan
	 * @throws CustomException - in case there is an unaccepted token or
	 *                         if there is a IOException thrown when trying to read the file
	 */
	public void scan(String filename) throws CustomException {
		verifyAutomatons();
		System.out.println("\nSCANNING THE PROGRAM: ");
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();

		String[] tokensVal = getProgramFromFile(filename);

		int i = 0;
		while (i < tokensVal.length) {
			verifyToken(tokensVal[i]);
			i++;
		}
	}

	/**
	 * @throws CustomException - if the automatons used by this program are not deterministic
	 */
	private void verifyAutomatons() throws CustomException {
		if (!identifierFiniteAutomaton.isDeterministic()) {
			throw new CustomException("The finite automaton for IDENTIFIER is not deterministic, It would cause " +
					"errors!");
		}
		if (!integerFiniteAutomaton.isDeterministic()) {
			throw new CustomException("The finite automaton for INTEGER is not deterministic, It would cause errors!");
		}
		if (!tokenFiniteAutomaton.isDeterministic()) {
			throw new CustomException("The finite automaton for TOKENS is not deterministic, It would cause errors!");
		}
		if (!floatFiniteAutomaton.isDeterministic()) {
			throw new CustomException("The finite automaton for FLOAT is not deterministic, It would cause errors!");
		}
	}

	/**
	 * Verifies tokens composed of a single word - simple keywords, constants(float or integer), identifiers
	 * Exits the function if an empty string is found
	 *
	 * @param token - the token to be checked
	 * @throws CustomException - if it cannot identify a type for this token.
	 *                         It allerts that there is a mistake in the written program from the file, that does
	 *                         not respect the alphabet of this language
	 */
	private void verifyToken(String token) throws CustomException {
//		System.out.println("TOKEN: " + token);
		String nextToken;
		while (!token.isEmpty()) {
			//look for keywords
			boolean found = false;
			if (codingTable.hasCode(token)) {
				System.out.println("FOUND Keyword: " + token);
				programInternalForm.addValues(codingTable.getValueForCode(token), null);
				return;
			}
			//look for identifiers
			nextToken = identifierFiniteAutomaton.getLongestPrefixForSequence(token);
			if (!nextToken.isEmpty()) {
				found = true;
				System.out.println("FOUND Identifier: " + token);
				symbolTable.addValue(nextToken);
				programInternalForm.addValues(codingTable.getValueForCode(IDENTIFIER), symbolTable.getPosition
						(nextToken));
				//remove this part from the token
				token = token.substring(nextToken.length());
			}
			//look for constants - FLOAT
			nextToken = floatFiniteAutomaton.getLongestPrefixForSequence(token);
			if (!nextToken.isEmpty()) {
				found = true;
				System.out.println("FOUND Constant -  FLOAT: " + nextToken);
				symbolTable.addValue(nextToken);
				programInternalForm.addValues(codingTable.getValueForCode(CONSTANT), symbolTable.getPosition
						(nextToken));
				//remove this part from the token
				token = token.substring(nextToken.length());
			}
			//look for constants - INTEGER
			nextToken = integerFiniteAutomaton.getLongestPrefixForSequence(token);
			if (!nextToken.isEmpty()) {
				found = true;
				System.out.println("FOUND Constant -  INTEGER: " + nextToken);
				symbolTable.addValue(nextToken);
				programInternalForm.addValues(codingTable.getValueForCode(CONSTANT), symbolTable.getPosition
						(nextToken));
				//remove this part from the token
				token = token.substring(nextToken.length());
			}

			if (!found) {
				throw new CustomException("Couldn't find a category for string '" + token + "'");
			}
		}
	}

	/**
	 * Reads and splits the text in an array of string which were separated by space
	 *
	 * @param filename - the file from which it reads the program
	 * @return an array of possible tokens
	 * @throws CustomException in case of an IOException (cannot find or open the file)
	 */
	private String[] getProgramFromFile(String filename) throws CustomException {
		StringBuilder program = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				program.append(line);
				program.append(" ");
			}
		} catch (IOException e) {
			throw new CustomException("Problems in reading the program :", e);
		}
		System.out.println(program);
		return getTokens(program.toString());
	}

	/**
	 * @param program - String (the program lines read from the file)
	 * @return an array of strings representing possible tokens ( a token can be a sequence of letters and/or digits,
	 * or the special signs from the coding table)
	 * @throws CustomException - if cannot identify a token (that token is not accepted by the defined automaton)
	 */
	private String[] getTokens(String program) throws CustomException {
		System.out.println("READING FROM FILE: ");
		List<String> tokens = new ArrayList<>();
		while (!program.isEmpty()) {
			program = program.trim();
			String nextToken = tokenFiniteAutomaton.getLongestPrefixForSequence(program);
			//There should not be empty tokens, otherwise there is an error
			if (!nextToken.isEmpty()) {
				tokens.add(nextToken);
				System.out.println("Next token: '" + nextToken + "'");
				//remove from the beginning the next token
				program = program.substring(nextToken.length());
			} else {
				throw new CustomException("Error reading the program from here:" + program);
			}
			//delete all spaces
			program = program.trim();
		}
		return tokens.toArray(new String[0]);
	}

	public ProgramInternalForm getProgramInternalForm() {
		return programInternalForm;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
}
