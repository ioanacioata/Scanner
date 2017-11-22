package ro.ubb.lftc.model.finiteautomata;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Transition of an automaton: contains state1, state2 and the transition symbol
 */
public class Transition {
	private String state1;
	private String state2;
	private Set<String> symbols;

	public Transition(String state1, String state2) {
		this.state1 = state1;
		this.state2 = state2;
		symbols = new LinkedHashSet<>();
	}

	public String getState1() {
		return state1;
	}

	public String getState2() {
		return state2;
	}

	public Set<String> getSymbols() {
		return symbols;
	}

	public void addSymbol(String symbol) {
		symbols.add(symbol);
	}

	@Override
	public String toString() {
		String s;
		s = state1 + " " + state2 + " {";
		for (String i : symbols) {
			s += i;
			s += " ";
		}
		s += "}";
		return s;
	}
}
