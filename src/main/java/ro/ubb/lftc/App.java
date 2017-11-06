package ro.ubb.lftc;

import ro.ubb.lftc.model.CustomException;

public class App {
	public static void main(String[] args) throws CustomException {
		Scanner scanner = new Scanner();

		scanner.scan("src/main/resources/circle.txt");
//		scanner.scan("src/main/resources/sum.txt");
//		scanner.scan("src/main/resources/divisor.txt");

		System.out.println("\n");
		System.out.println(scanner.getProgramInternalForm().toString());
		System.out.println(scanner.getSymbolTable().toString());
	}
}
