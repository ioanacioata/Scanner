package ro.ubb.lftc.model.finiteautomata;

import java.util.LinkedHashSet;
import java.util.Set;

public class Transition {
	private String state1;
	private String symbol;
	private Set<String> states2;

	public Transition(String state1, String symbol) {
		this.state1 = state1;
		this.symbol = symbol;
		states2 = new LinkedHashSet<>();
	}

	public String getState1() {
		return state1;
	}

	public String getSymbol() {
		return symbol;
	}

	public Set<String> getStates2() {
		return states2;
	}

	public void setStates2(Set<String> states2) {
		this.states2 = states2;
	}

	public void addSymbol(String symbol) {
		states2.add(symbol);
	}

	@Override
	public String toString() {
		String s;
		s = state1 + " " + symbol + " {";
		for (String i : states2) {
			s += i;
			s += " ";
		}
		s += "}";
		return s;
	}
}
