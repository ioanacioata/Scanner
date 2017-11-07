package ro.ubb.lftc.model.finiteautomata;

import ro.ubb.lftc.model.programscanner.CustomException;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Set<String> states;
 * Set<String> alphabet;
 * String initialState;
 * Set<String> finalStates;
 * Map<Tuple<String,String>, Set<String>> transitions;
 */
public abstract class FiniteAutomata {
	protected Set<String> states;
	protected Set<String> alphabet;
	protected String initialState;
	protected Set<String> finalStates;
	protected Set<Transition> transitions;

	public FiniteAutomata() {
		states = new LinkedHashSet<>();
		alphabet = new LinkedHashSet<>();
		finalStates = new LinkedHashSet<>();
		transitions = new LinkedHashSet<>();
	}

	public Set<String> getStates() {
		return states;
	}

	public void setStates(Set<String> states) {
		this.states = states;
	}

	public Set<String> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Set<String> alphabet) {
		this.alphabet = alphabet;
	}

	public String getInitialState() {
		return initialState;
	}

	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	public Set<String> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(Set<String> finalStates) {
		this.finalStates = finalStates;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(Set<Transition> transitions) {
		this.transitions = transitions;
	}

	public abstract void readAutomata() throws CustomException;

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
