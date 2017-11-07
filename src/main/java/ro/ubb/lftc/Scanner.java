package ro.ubb.lftc;

import ro.ubb.lftc.model.programscanner.CodingTable;
import ro.ubb.lftc.model.programscanner.CustomException;
import ro.ubb.lftc.model.programscanner.ProgramInternalForm;
import ro.ubb.lftc.model.programscanner.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {
	public static final String INCLUDE = "#include";
	public static final String IOSTREAM = "<iostream>";
	public static final String USING = "using";
	public static final String NAMESPACE = "namespace";
	public static final String STD = "std";
	public static final String INT = "int";
	public static final String MAIN = "main()";
	public static final String CONSTANT = "constant";
	public static final String IDENTIFIER = "identifier";
	public static final int IDENTIFIER_NAME_LIMIT = 8;
	private ProgramInternalForm programInternalForm;
	private SymbolTable symbolTable;
	private CodingTable codingTable;
	private Boolean foundCode;

	public Scanner() throws CustomException {
		codingTable = new CodingTable("src/main/resources/lab1/codes.txt");
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();
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
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();

		String[] tokensVal = getProgramFromFile(filename);

		foundCode = false;
		int i = 0;
		while (i < tokensVal.length) {

			i = verifyCodesWithMoreWords(tokensVal, i);
			if (foundCode == Boolean.FALSE) {
				verifySingleTokens(tokensVal[i]);
				i++;
			}

			foundCode = Boolean.FALSE;
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
			foundCode = Boolean.TRUE;
		}
		if (isConstantToken(token)) {
			foundCode = Boolean.TRUE;
			System.out.println("FOUND Constant: " + token);
			symbolTable.addValue(token);
			programInternalForm.addValues(codingTable.getValueForCode(CONSTANT), symbolTable.getPosition(token));
		}
		if (isIdentifierToken(token)) {
			System.out.println("FOUND Identifier: " + token);
			foundCode = Boolean.TRUE;
			symbolTable.addValue(token);
			programInternalForm.addValues(codingTable.getValueForCode(IDENTIFIER), symbolTable.getPosition(token));
		}

		if (foundCode.equals(Boolean.FALSE)) {
			throw new CustomException("Couldn't find a category for string '" + token + "'");
		}
	}

	/**
	 * @param s - the string to verify
	 * @return true if is an identifier (contains only letters and is not a keyword),
	 * false otherwise
	 * @throws CustomException if the string s is an identifier longer than IDENTIFIER_NAME_LIMIT (=8)
	 */
	private boolean isIdentifierToken(String s) throws CustomException {
		if (s.chars().allMatch(Character::isLetter) && !codingTable.getCodes().containsKey(s)) {
			if (s.length() > IDENTIFIER_NAME_LIMIT) {
				throw new CustomException("You cannot have identifiers with name longer than " +
						IDENTIFIER_NAME_LIMIT);
			}
			return true;
		}
		return false;
	}

	/**
	 * @param s - the string to verify
	 * @return true if s is a valid number, false otherwise
	 * Examples unvalid numbers: -0 , 01, -01.10, 2.0, 2000.000020
	 */
	private boolean isConstantToken(String s) {
		if (s.matches("0") || s.matches("-0.[0-9]*[1-9]")) { //just digits
//			System.out.println("TRUE - just 0 or -0.other numbers ");
			return true;
		}
		if (s.matches("^-?[1-9][0-9]*(.[0-9]*[1-9])?")) { //just digits
//			System.out.println("TRUE - more digits");
			return true;
		}
		return false;
	}

	/**
	 * Identifies tokens of longer length and returns the position of the next token.
	 * (Finds the tokens : "#include <iostream>", "using namespace std", "int main()" )
	 *
	 * @param tokensVal - list of tokens in the program
	 * @param i         - the current position in the list of program tokens/atoms
	 * @return the position of the next token to analyze
	 * @throws CustomException if it cannot add in the programInternalForm these values
	 */
	private int verifyCodesWithMoreWords(String[] tokensVal, int i) throws CustomException {
		if (tokensVal[i].equals(INCLUDE) && tokensVal[i + 1].equals(IOSTREAM)) {
			programInternalForm.addValues(codingTable.getValueForCode(INCLUDE + " " + IOSTREAM), null);
			this.foundCode = Boolean.TRUE;
			System.out.println("FOUND Special Keyword: " + INCLUDE + " " + IOSTREAM + " next word is " + tokensVal[i +
					2]);
			return i + 2;
		}
		if (tokensVal[i].equals(USING) && tokensVal[i + 1].equals(NAMESPACE) && tokensVal[i + 2].equals(STD)) {
			programInternalForm.addValues(codingTable.getValueForCode(USING + " " + NAMESPACE + " " + STD), null);
			foundCode = Boolean.TRUE;
			System.out.println("FOUND Special Keyword: " + USING + " " + NAMESPACE + " " + STD + " next word is " +
					tokensVal[i + 3]);
			return i + 3;
		}
		if (tokensVal[i].equals(INT) && tokensVal[i + 1].equals(MAIN)) {
			programInternalForm.addValues(codingTable.getValueForCode(INT + " " + MAIN), null);
			foundCode = Boolean.TRUE;
			System.out.println("FOUND Special Keyword: " + INT + " " + MAIN + " next word is " + tokensVal[i + 2]);
			return i + 2;
		}

		return i;
	}

	/**
	 * Reads and splits the text in an array of string which were separated by space
	 *
	 * @param filename - the file from which it reads the program
	 * @return an array of possible tokens
	 * @throws CustomException in case of an IOException (cannot find or open the file)
	 */
	private String[] getProgramFromFile(String filename) throws CustomException {
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
		String[] programTokens = program.split(" ");
		//get rid of identation
		for (String t : programTokens) {
			t.replaceAll("\\s+", "");
			t.replaceAll("\\t", "");
		}
		return programTokens;
	}

	public ProgramInternalForm getProgramInternalForm() {
		return programInternalForm;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
}
