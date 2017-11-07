package ro.ubb.lftc.model.finiteautomata;

import org.junit.Before;
import org.junit.Test;
import ro.ubb.lftc.model.programscanner.CustomException;

import static org.junit.Assert.assertEquals;

public class FiniteAutomatonTest {
	private FiniteAutomaton finiteAutomaton;

	@Before
	public void setUp() throws CustomException {
		finiteAutomaton = new FiniteAutomatonFromFile("src/main/resources/fa_ex2.txt");
		finiteAutomaton.readAutomaton();
	}

	@Test
	public void testVerifySequence() {
		assertEquals("abc", finiteAutomaton.verifySequence("abc"));
		assertEquals("aaaabcabc", finiteAutomaton.verifySequence("aaaabcabc"));
		assertEquals("aaabc", finiteAutomaton.verifySequence("aaabcab"));
	}
}