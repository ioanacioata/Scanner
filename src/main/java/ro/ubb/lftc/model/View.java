package ro.ubb.lftc.model;

import ro.ubb.lftc.model.finiteautomata.FiniteAutomata;
import ro.ubb.lftc.model.finiteautomata.FiniteAutomataFromFile;
import ro.ubb.lftc.model.programscanner.CustomException;

import java.util.Scanner;

public class View {
	private FiniteAutomata finiteAutomata;
	private Scanner keyboard;

	public View(FiniteAutomata finiteAutomata) {
		this.finiteAutomata = finiteAutomata;
		keyboard = new Scanner(System.in);
	}

	public void printMenu() {
		String menu = "Choose from the following: \n";
		menu += "1.Show the finite automata.\n";
		menu += "2.Show states.\n";
		menu += "3.Show final states.\n";
		menu += "4.Show transitions\n";
		menu += "5.Show the alphabet\n";
		menu += "6.For a determinist finite automaton, verify if a sequence is accepted by the automaton\n";
		menu += "7.For a determinist finite automaton, determine the longest prefix of a sequence that is accepted by" +
				" the automaton.\n";
		menu += "8.Input a finite automata from keyboard.\n";
		menu += "9.Use a finite automata from file.\n";
		menu += "10.Print Menu again.\n";
		menu += "0.Exit\n";
		System.out.println(menu);
	}

	public void run() {
		printMenu();
		while (true) {
			int a = keyboard.nextInt();
			switch (a) {
				case 1: {
					//1.Show the finite automata.
					System.out.println(finiteAutomata.toString());
					continue;
				}
				case 2: {
					//2.Show states
					System.out.println(finiteAutomata.getStringStates());
					continue;
				}
				case 3: {
					//3.Show final states.
					System.out.println(finiteAutomata.getStringFinalStates());
					continue;
				}
				case 4: {
					//4.Show transitions
					System.out.println((finiteAutomata.getStringTransitions()));
					continue;
				}
				case 5: {
					//5.Show the alphabet
					System.out.println(finiteAutomata.getStringAlphabet());
					continue;
				}
				case 6: {
					//6.For a determinist finite automaton, verify if a sequence is accepted by the automaton
					continue;
				}
				case 7: {
					//7.For a determinist finite automaton, determine the longest prefix of a sequence that is
					// accepted by the automaton.
					continue;
				}
				case 8: {
					//8.Input a finite automata from keyboard.
					continue;
				}
				case 9: {
					//9.Use a finite automata from file.
					System.out.println("Give file name: ");
					keyboard = new Scanner(System.in);
					String file = keyboard.nextLine().trim();
					finiteAutomata = new FiniteAutomataFromFile("src/main/resources/" + file);
					try {
						finiteAutomata.readAutomata();
						System.out.println("Read successfully the automata from the file: " + file
								+ " !\n");
					} catch (CustomException e) {
						System.out.println("Invalid file!");
					}
					continue;
				}
				case 10: {
					//10.Print Menu again.
					printMenu();
					continue;
				}
				case 0: {
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
		}
	}
}
