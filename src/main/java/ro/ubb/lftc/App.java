package ro.ubb.lftc;

import ro.ubb.lftc.model.View;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomaton;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomatonFromFile;
import ro.ubb.lftc.model.programscanner.CustomException;

public class App {
	public static void main(String[] args) throws CustomException {
		//lab1ExampleScanner();

		FiniteAutomaton f= new FiniteAutomatonFromFile("src/main/resources/fa_ex1.txt");
		f.readAutomaton();
		System.out.println(f.toString());
		View view = new View(f);
		view.run();


	}

	private static void lab1ExampleScanner() throws CustomException {
		Scanner scanner = new Scanner();

		scanner.scan("src/main/resources/circle.txt");
//		scanner.scan("src/main/resources/sum.txt");
//		scanner.scan("src/main/resources/divisor.txt");

		System.out.println("\n");
		System.out.println(scanner.getProgramInternalForm().toString());
		System.out.println(scanner.getSymbolTable().toString());
	}
}
