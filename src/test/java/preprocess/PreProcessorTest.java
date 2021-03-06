package preprocess;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class PreProcessorTest {

	private static int SOME_TEST_CONSTANT;

	@Test
	public void testProcess() {
		List<Declaration> declarations = PreProcessor.process("src/test/resources");
		assertNotNull(declarations);
		assertFalse(declarations.isEmpty());
		assertEquals(1, declarations.size());
		Declaration declaration = declarations.get(0);

		// Class Names
		assertEquals(1, declaration.getClassNames().size());

		// Method Names
		assertEquals(5, declaration.getMethodNames().size());

		// Variable names
		assertEquals(19, declaration.getVariableNames().size());

		// Constants
		assertEquals(2, declaration.getStaticVariableNames().size());

	}
}