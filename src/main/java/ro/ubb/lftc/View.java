package ro.ubb.lftc;

import ro.ubb.lftc.model.finiteautomata.FiniteAutomaton;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomatonFromFile;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomatonFromKeyboard;
import ro.ubb.lftc.model.programscanner.CustomException;

import java.util.Scanner;

public class View {
	private FiniteAutomaton finiteAutomaton;
	private Scanner keyboard;

	public View(FiniteAutomaton finiteAutomaton) {
		this.finiteAutomaton = finiteAutomaton;
		keyboard = new Scanner(System.in);
	}

	public void printMenu() {
		String menu = "Choose from the following: \n";
		menu += "1.Show the finite automaton.\n";
		menu += "2.Show states.\n";
		menu += "3.Show final states.\n";
		menu += "4.Show transitions\n";
		menu += "5.Show the alphabet\n";
		menu += "6.For a determinist finite automaton, verify if a sequence is accepted by the automaton\n";
		menu += "7.For a determinist finite automaton, determine the longest prefix of a sequence that is accepted " +
				"by" +
				" the automaton.\n";
		menu += "8.Input a finite automaton from keyboard.\n";
		menu += "9.Use a finite automaton from file.\n";
		menu += "10.Print Menu again.\n";
		menu += "0.Exit\n";
		System.out.println(menu);
	}

	public void run() {
		printMenu();
		while (true) {
			try {
				String a = getStringFromKeyboard("Give command: ").trim();
				switch (a) {
					case "1": {
						//1.Show the finite automaton.
						System.out.println(finiteAutomaton.toString());
						continue;
					}
					case "2": {
						//2.Show states
						System.out.println(finiteAutomaton.getStringStates());
						continue;
					}
					case "3": {
						//3.Show final states.
						System.out.println(finiteAutomaton.getStringFinalStates());
						continue;
					}
					case "4": {
						//4.Show transitions
						System.out.println((finiteAutomaton.getStringTransitions()));
						continue;
					}
					case "5": {
						//5.Show the alphabet
						System.out.println(finiteAutomaton.getStringAlphabet());
						continue;
					}
					case "6": {
						//6.For a determinist finite automaton, verify if a sequence is accepted by the automaton
						String sequence = getStringFromKeyboard("Give a sequence: ");
						if (!finiteAutomaton.isDeterministic()) {
							System.out.println("The automaton has to be deterministic.");
							continue;
						}
						if (sequence.equals(finiteAutomaton.verifySequence(sequence))) {
							System.out.println("This sequence '" + sequence + "'is accepted by the automaton");
						} else {
							System.out.println("The sequence '" + sequence + "' is NOT accepted by the automaton.");
						}
						continue;
					}

					case "7": {
						//7.For a determinist finite automaton, determine the longest prefix of a sequence that is
						// accepted by the automaton.
						if (finiteAutomaton.isDeterministic()) {
							String sequence = getStringFromKeyboard("Give a sequence: ");
							System.out.println("The longest accepted prefix is: '" + finiteAutomaton.verifySequence
									(sequence)

									+ "' \n");
						} else {
							System.out.println("The automaton has to be deterministic.");
						}

						continue;
					}
					case "8": {
						//8.Input a finite automaton from keyboard.
						finiteAutomaton = new FiniteAutomatonFromKeyboard();
						finiteAutomaton.readAutomaton();
						continue;
					}
					case "9": {
						//9.Use a finite automaton from file.
						initializeAutomatonFromGivenFile();
						continue;
					}
					case "10": {
						//10.Print Menu again.
						printMenu();
						continue;
					}
					case "0": {
						//0.Exit
						System.out.println("B-Bye!!!");
						keyboard.close();
						return;
					}

					default: {
						System.out.println("Invalid input! \n");
						continue;
					}
				}
			} catch (CustomException e) {
				System.out.println("There has been a problem: " + e.getMessage());
			}
		}
	}

	private String getStringFromKeyboard(String message) {
		System.out.println(message);
		keyboard = new Scanner(System.in);
		return keyboard.nextLine();
	}

	private void initializeAutomatonFromGivenFile() {
		String file = getStringFromKeyboard("Give file name: ").trim();
		finiteAutomaton = new FiniteAutomatonFromFile("src/main/resources/lab2/" + file);
		try {
			finiteAutomaton.readAutomaton();
			System.out.println("Read successfully the automaton from the file: " + file + " !\n");
		} catch (CustomException e) {
			System.out.println("Invalid file!");
		}
	}
}
