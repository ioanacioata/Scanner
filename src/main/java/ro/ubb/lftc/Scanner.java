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

public class Scanner {
	private static final String CONSTANT = "constant";
	private static final String IDENTIFIER = "identifier";
	private ProgramInternalForm programInternalForm;
	private SymbolTable symbolTable;
	private CodingTable codingTable;
	private FiniteAutomaton integerFiniteAutomaton;
	private FiniteAutomaton identifierFiniteAutomaton;
	private FiniteAutomaton tokenAutomaton;

	public Scanner() throws CustomException {
		codingTable = new CodingTable("src/main/resources/lab1/codes.txt");
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();
		//read the automatons
		integerFiniteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_integer.txt");
		integerFiniteAutomaton.readAutomaton();
		identifierFiniteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_identifier.txt");
		identifierFiniteAutomaton.readAutomaton();
		tokenAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_token.txt");
		tokenAutomaton.readAutomaton();
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
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();

		String[] tokensVal = getProgramFromFile(filename);

		int i = 0;
		while (i < tokensVal.length) {
			verifySingleTokens(tokensVal[i]);
			i++;
		}
	}

	private void verifyAutomatons() throws CustomException {
		if(!identifierFiniteAutomaton.isDeterministic()){
			throw new CustomException("The finite automaton for IDENTIFIER is not deterministic, It would cause errors!");

		}
		if(!integerFiniteAutomaton.isDeterministic()){
			throw new CustomException("The finite automaton for INTEGER is not deterministic, It would cause errors!");
		}
		if(!tokenAutomaton.isDeterministic()){
			throw new CustomException("The finite automaton for TOKENS is not deterministic, It would cause errors!");
		}
	}

	/**
	 * Verifies tokens composed of a single word - simple keywords, constants, identifiers
	 * Exits the function if an empty string is found
	 *
	 * @param token - the token to be checked
	 * @throws CustomException - if it cannot identify a type for this token.
	 *                         It allerts that there is a mistake in the written program from the file, that does
	 *                         not respect the alphabet of this language
	 */
	private void verifySingleTokens(String token) throws CustomException {
		//verify normal codes
		if (token.equals("")) {
			return;
		}
		if (codingTable.hasCode(token)) {
			System.out.println("FOUND Keyword: " + token);
			programInternalForm.addValues(codingTable.getValueForCode(token), null);
			return;
		}
		if (isConstantToken(token)) {
			System.out.println("FOUND Constant: " + token);
			symbolTable.addValue(token);
			programInternalForm.addValues(codingTable.getValueForCode(CONSTANT), symbolTable.getPosition(token));
			return;
		}
		if (isIdentifierToken(token)) {
			System.out.println("FOUND Identifier: " + token);
			symbolTable.addValue(token);
			programInternalForm.addValues(codingTable.getValueForCode(IDENTIFIER), symbolTable.getPosition(token));
			return;
		}

		throw new CustomException("Couldn't find a category for string '" + token + "'");
	}

	/**
	 * @param s - the string to verify
	 * @return true if is an identifier (contains only letters and is not a keyword),
	 * false otherwise
	 */
	private boolean isIdentifierToken(String s){
		return identifierFiniteAutomaton.isAccepted(s);
	}

	/**
	 * @param s - the string to verify
	 * @return true if s is a valid number, false otherwise
	 */
	private boolean isConstantToken(String s) {
		return integerFiniteAutomaton.isAccepted(s);
	}

	/**
	 * Reads and splits the text in an array of string which were separated by space
	 *
	 * @param filename - the file from which it reads the program
	 * @return an array of possible tokens
	 * @throws CustomException in case of an IOException (cannot find or open the file)
	 */
	private String[] getProgramFromFile(String filename) throws CustomException {
		System.out.println("HELLO");
		String program = "";
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				program += line;
				program += " ";
			}
		} catch (IOException e) {
			throw new CustomException("Problems in reading the program :", e);
		}
		System.out.println(program);
		String[] programTokens = getTokens(program);
		//get rid of identation
		for (String t : programTokens) {
			t.replaceAll("\\s+", "");
			t.replaceAll("\\t", "");
		}
		return programTokens;
	}

	private String[] getTokens(String program) {
		List<String> tokens= new ArrayList<>();
		while (!program.isEmpty()){
			program=program.trim();
			String nextToken = tokenAutomaton.getLongestPrefixForSequence(program);
			if(!nextToken.isEmpty()){
				tokens.add(nextToken);
				System.out.println("Next token: "+nextToken);
				//TODO remove from the beginning the next token
				program=program.substring(nextToken.length());
			}
			program=program.trim();
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
