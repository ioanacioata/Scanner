package ro.ubb.lftc.model.programscanner;

import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {
	private static final int CODE_FOR_IDENTIFIER = 1;
	private static final int CODE_FOR_CONSTANT = 0;
	List<Tuple<Integer, Integer>> pifTable;

	public ProgramInternalForm() {
		pifTable = new ArrayList<>();
	}

	/**
	 * @param code                - Integer
	 * @param positionSymbolTable - Integer number identifier or constant, null otherwise
	 * @throws CustomException if we try to add a null code or we do not insert the position from the symbol table in
	 *                         the case of identifiers and constants; we may insert a null position
	 */
	public void addValues(Integer code, Integer positionSymbolTable) throws CustomException {
		if (code.equals(null)) {
			throw new CustomException("Cannot insert null value for the code");
		}
		if ((code.equals(CODE_FOR_IDENTIFIER) || code.equals(CODE_FOR_CONSTANT)) && positionSymbolTable.equals(null)) {
			throw new CustomException("For constants and identifier you must add the symbol table position! ");
		}
		pifTable.add(new Tuple<>(code, positionSymbolTable));
	}

	@Override
	public String toString() {
		String s = "ProgramInternalForm: \n";
		for (Tuple<Integer, Integer> t : pifTable) {
			s += t.value1;
			s += " , ";
			if (t.value2 == null) {
				s += "/";
			} else {
				s += t.value2;
			}
			s += "\n";
		}
		return s;
	}
}
