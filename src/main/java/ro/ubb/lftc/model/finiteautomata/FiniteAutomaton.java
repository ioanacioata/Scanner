package ro.ubb.lftc.model.finiteautomata;

import ro.ubb.lftc.model.programscanner.CustomException;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Set<String> states;
 * Set<String> alphabet;
 * String initialState;
 * Set<String> finalStates;
 * Set<Transition> transitions;
 */
public abstract class FiniteAutomaton {
	protected Set<String> states;
	protected Set<String> alphabet;
	protected String initialState;
	protected Set<String> finalStates;
	protected Set<Transition> transitions;

	public FiniteAutomaton() {
		states = new LinkedHashSet<>();
		alphabet = new LinkedHashSet<>();
		finalStates = new LinkedHashSet<>();
		transitions = new LinkedHashSet<>();
	}

	/**
	 * @return true if the automaton is deterministic or not
	 */
	public boolean isDeterministic() {
		for (String state : states) {
			Map<String, String> nextStates = new HashMap<>();
			for (Transition t : transitions) {
				if (t.getState1().equals(state)) {
					for (String symbol : t.getSymbols()) {
						if (nextStates.containsKey(symbol)) {
							return false;
						}
						nextStates.put(symbol, t.getState2());
					}
				}
			}
		}
		return true;
	}

	/**
	 * Verifies if the sequence is accepted by the automaton. (works for deterministic automatons)
	 *
	 * @param sequence - String sequence of characters
	 * @return the longest prefix that is accepted by the automaton or the same string given if it is all correct
	 */
	public String verifySequence(String sequence) {
		String acceptedPrefix = "";
		String acceptedCompleteSequence = "";
		String currentState = initialState;
		String currentSymbol;
		String nextState;

		for (int i = 0; i < sequence.length(); i++) {
			//iterate through the sequence
			currentSymbol = String.valueOf(sequence.charAt(i));
			acceptedCompleteSequence += currentSymbol;
			try {
				nextState = findNextState(currentState, currentSymbol);
			} catch (CustomException e) {
				//if this transition does not exist
				return acceptedPrefix;
			}
			if (finalStates.contains(nextState)) {
				acceptedPrefix += acceptedCompleteSequence;
				acceptedCompleteSequence = "";
			}
			currentState = nextState;
		}
		return acceptedPrefix;
	}

	protected String findNextState(String initialState, String currentSymbol) throws CustomException {
		for (Transition t : transitions) {
			if (t.getState1().equals(initialState) && t.getSymbols().contains(currentSymbol)) {
				return t.getState2();
			}
		}
		throw new CustomException("It does not exist another state");
	}

	public abstract void readAutomaton() throws CustomException;

	@Override
	public String toString() {
		String str = "Finite automata: \n";
		str += getStringStates();
		str += getStringAlphabet();
		str += getStringInitialState();
		str += getStringFinalStates();
		str += getStringTransitions();
		return str;
	}

	public String getStringTransitions() {
		String str = "Transitions: ";
		for (Transition s : transitions) {
			str += "\n";
			str += s.toString();
		}
		str += "\n";
		return str;
	}

	public String getStringFinalStates() {
		String str = "Final states: ";
		for (String s : finalStates) {
			str += s;
			str += " ";
		}
		str += "\n";
		return str;
	}

	public String getStringInitialState() {
		String str = "Initial State: ";
		str += initialState;
		str += "\n";
		return str;
	}

	public String getStringAlphabet() {
		String str = "Alphabet: ";
		for (String s : alphabet) {
			str += s;
			str += " ";
		}
		str += "\n";
		return str;
	}

	public String getStringStates() {
		String str = "States: ";
		for (String s : states) {
			str += s;
			str += " ";
		}
		str += "\n";
		return str;
	}
}
