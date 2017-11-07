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

	public FiniteAutomatonFromFile(String fileName)  {
		super();
		this.fileName = fileName;
	}

	@Override
	public void readAutomaton() throws CustomException {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("INITIAL:")) {
					line = br.readLine();//read next line
					line.trim();
					this.initialState = line;
					this.states.add(line);
				}

				if (line.equals("FINAL:")) {
					line = br.readLine();//read next line
					line.trim();
					List<String> finals = Arrays.stream(line.split("\\s")).filter(s -> !s.equals("")).map(String::trim).collect(Collectors.toList());
					for (String s : finals) {
						this.finalStates.add(s);
						this.states.add(s);
					}
				}

				if (line.equals("TRANSITIONS:")) {
					String transition;
					while ((transition = br.readLine()) != null) {
						List<String> trans = Arrays.stream(transition.split("[\\s+{},]")).filter(s -> !s.equals("")).map(String::trim).collect(Collectors.toList());
						String state1 = trans.get(0);
						String state2 = trans.get(1);
						Transition t = new Transition(state1, state2);
						this.states.add(state1);
						this.states.add(state2);
						//read symbols
						for (int i = 2; i < trans.size(); i++) {
							String symbol = trans.get(i);
							t.addSymbol(symbol);
							this.alphabet.add(symbol);
						}
						this.transitions.add(t);
					}
				}
			}
		} catch (IOException e) {
			throw new CustomException("Problems in reading the program :", e);
		}
	}
}
