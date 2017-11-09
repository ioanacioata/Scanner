package ro.ubb.lftc.model.finiteautomata;

import ro.ubb.lftc.model.programscanner.CustomException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FiniteAutomatonFromKeyboard extends FiniteAutomaton{
	private Scanner keyboard;

	public FiniteAutomatonFromKeyboard() {
		super();
	}

	@Override
	public void readAutomaton() throws CustomException {
		String initialState = getStringFromKeyboard("Give initial state: ");
		initialState.trim();
		if (initialState.equals("")) {
			throw new CustomException("The automaton has to have an initial state!");
		}
		this.initialState = initialState;
		this.states.add(initialState);

		String finalStates = getStringFromKeyboard("Give  final states, separated by space: ");
		finalStates.trim();
		List<String> finals = Arrays.stream(finalStates.split("\\s")).filter(s -> !s.equals("")).map
				(String::trim).collect(Collectors.toList());
		if (finals.size() == 0) {
			System.out.println("The automaton has to have at least a final state!");
		}
		for (String s : finals) {
			this.finalStates.add(s);
			this.states.add(s);
		}
		System.out.println("Give transitions: ");
		String transition = getStringFromKeyboard("Give transition:");
		while ((!transition.equals(""))) {
			List<String> trans = Arrays.stream(transition.split("[\\s+{},]")).filter(s -> !s.equals(""))
					.map(String::trim).collect(Collectors.toList());
			String state1 = trans.get(0);
			String symbol = trans.get(1);
			Transition t = new Transition(state1, symbol);
			this.states.add(state1);
			this.alphabet.add(symbol);
			//read symbols
			for (int i = 2; i < trans.size(); i++) {
				String state2 = trans.get(i);
				t.addSymbol(state2);
				this.states.add(state2);
			}
			this.transitions.add(t);
			transition = getStringFromKeyboard("Give transition: ");
		}
		if (transitions.size() == 0) {
			throw new CustomException("You have to input transitions!");
		}
	}

	private String getStringFromKeyboard(String message) {
		System.out.println(message);
		keyboard = new Scanner(System.in);
		return keyboard.nextLine();
	}
}
