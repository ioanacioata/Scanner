package ro.ubb.lftc.model.finiteautomata;

import org.junit.Before;
import org.junit.Test;
import ro.ubb.lftc.model.programscanner.CustomException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FiniteAutomatonTest {
	private FiniteAutomaton finiteAutomaton;

	@Before
	public void setUp() throws CustomException {
		finiteAutomaton = new FiniteAutomatonFromFile("src/test/resources/fa_test.txt");
		finiteAutomaton.readAutomaton();
	}

	@Test
	public void testVerifySequence() {
		assertEquals("abc", finiteAutomaton.getLongestPrefixForSequence("abc"));
		assertEquals("aaaabcabc", finiteAutomaton.getLongestPrefixForSequence("aaaabcabc"));
		assertEquals("aaabc", finiteAutomaton.getLongestPrefixForSequence("aaabcab"));
	}

	@Test
	public void testIsDeterministic() throws CustomException {
		assertTrue(finiteAutomaton.isDeterministic());

		FiniteAutomaton finiteAutomatonNonDeterministic = new FiniteAutomatonFromFile
				("src/test/resources/fa_test2.txt");
		finiteAutomatonNonDeterministic.readAutomaton();

		assertFalse(finiteAutomatonNonDeterministic.isDeterministic());
	}
}