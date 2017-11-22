package ro.ubb.lftc;

import ro.ubb.lftc.model.finiteautomata.FiniteAutomaton;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomatonFromFile;
import ro.ubb.lftc.model.programscanner.CustomException;

/**
 * Homework constraint:
 * 1. Identifiers: a. length at most 8 characters
 * 2. Symbol Table: a. unique for identifiers and constants
 * 3. Symbol Table Organization: c. hashing table
 */
public class App {
	public static void main(String[] args) throws CustomException {
		try {
//			lab1ExampleScanner();
//			Lab2Part2ExampleFiniteAutomaton();

			lab2Part2Scan();
		} catch (CustomException e) {
			System.out.println("EXCEPTION: " + e.getMessage());
		}
	}

	private static void lab2Part2Scan() throws CustomException {
		Scanner scanner = new Scanner();
//		scanner.scan("src/main/resources/lab2Part2/scanner_ex1.txt");
		scanner.scan("src/main/resources/lab2Part2/scanner_ex.txt");
		System.out.println("\nRESULTS:");
		System.out.println(scanner.getProgramInternalForm().toString());
		System.out.println(scanner.getSymbolTable().toString());
	}

	private static void Lab2Part2ExampleFiniteAutomaton() throws CustomException {
		//Laboratory 2 - Part 1
//		FiniteAutomaton f = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_integer.txt");
		FiniteAutomaton f = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_token.txt");
		//FiniteAutomaton f = new FiniteAutomatonFromFile("src/main/resources/lab2Part2/dfa_identifier.txt");
		f.readAutomaton();
		View view = new View(f);
		view.run();
	}

	private static void lab1ExampleScanner() throws CustomException {
		Scanner scanner = new Scanner();

//		scanner.scan("src/main/resources/lab1/circle.txt");
//		scanner.scan("src/main/resources/lab1/sum.txt");
		scanner.scan("src/main/resources/lab1/divisor.txt");

		System.out.println("\n");
		System.out.println(scanner.getProgramInternalForm().toString());
		System.out.println(scanner.getSymbolTable().toString());
	}
}
