package ro.ubb.lftc.model.finiteautomata;

import ro.ubb.lftc.model.programscanner.CustomException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FiniteAutomatonFromFile extends FiniteAutomaton {
	private String fileName;

	public FiniteAutomatonFromFile(String fileName) {
		super();
		this.fileName = fileName;
	}

	/**
	 * Read the automaton from file and initializes the components of the finite automaton
	 *
	 * @throws CustomException if an IOException is thrown
	 */
	@Override
	public void readAutomaton() throws CustomException {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("INITIAL:")) {
					line = br.readLine();//read next line
					line.trim();
					if (line.equals("")) {
						throw new CustomException("You have to add an initial state!");
					}
					this.initialState = line;
					this.states.add(line);
				}

				if (line.equals("FINAL:")) {
					line = br.readLine();//read next line
					line.trim();
					List<String> finals = Arrays.stream(line.split("\\s")).filter(s -> !s.equals("")).map
							(String::trim).collect(Collectors.toList());
					if (finals.size() == 0) {
						System.out.println("The automaton has to have at least a final state!");
					}
					for (String s : finals) {
						this.finalStates.add(s);
						this.states.add(s);
					}
				}

				if (line.equals("TRANSITIONS:")) {
					String transition;
					while ((transition = br.readLine()) != null) {
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
					}
				}
				if (transitions.size() == 0) {
					throw new CustomException("You have to input transitions!");
				}
			}
		} catch (IOException e) {
			throw new CustomException("Problems in reading the program :", e);
		}
	}
}
