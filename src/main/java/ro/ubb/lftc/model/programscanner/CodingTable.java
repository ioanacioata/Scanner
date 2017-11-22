package ro.ubb.lftc.model.programscanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CodingTable class contains a map with the numeric codes of the possible keywords in the program. This map is
 * filled in the constructor of the class, it calls a private method that reads from a given file the data.
 * <p>
 * Exceptions : The constructor may throw an CustomException  if there is an IO Exception thrown, related to reading
 * the file.
 */
public class CodingTable {
	String filename;
	Map<String, Integer> codes;

	public CodingTable(String filename) throws CustomException {
		this.filename = filename;
		codes = new HashMap<>();
		this.readFromFile();
	}

	public Integer getValueForCode(String code) {
		return codes.get(code);
	}

	/**
	 * Read the codes from a given file and saves them in the map
	 */
	private void readFromFile() throws CustomException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokensVal = line.split(" ");
				Integer value = Integer.valueOf(tokensVal[0]);
				String identifier = tokensVal[1];

				//add extra words
				for (int i = 2; i < tokensVal.length; i++) {
					if (!tokensVal[i].equals(" ")) {
						identifier += " ";
						identifier += tokensVal[i];
					}
				}
				//add to map
				codes.put(identifier, value);
			}
		} catch (IOException e) {
			throw new CustomException("Problems at reading the codes file. ", e);
		}
	}

	public Map<String, Integer> getCodes() {
		return codes;
	}

	public boolean hasCode(String code) {
		return codes.containsKey(code);
	}
}
