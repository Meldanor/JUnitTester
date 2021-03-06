import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.meldanor.junittester.io.TextFileLoader;
import de.meldanor.junittester.validate.CharSequenceValidator;
import de.meldanor.junittester.validate.ContentValidateEngine;
import de.meldanor.junittester.validate.ContentValidator.ContentValidatorResult;

public class ValidatorEngineTest {

    private static ContentValidateEngine validatorEngine;

    @BeforeClass
    public static void beforeClass() {
        validatorEngine = new ContentValidateEngine();
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.io", "java.nio"));
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.net", "javax.net"));
    }

    @Test
    public void simpleTests() {
        ContentValidatorResult result = validatorEngine.validate("import java.io");
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        result = validatorEngine.validate("import javax.net");
        assertFalse(result.isValid());
        assertEquals("Code can not use javax.net", result.getReason());

        result = validatorEngine.validate("import java.math");
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void validateSourceCode() {
        TextFileLoader tLoader = new TextFileLoader();
        String sourceCode = null;
        ContentValidatorResult result = null;

        sourceCode = tLoader.readFile(getClass().getResourceAsStream("/DeleteFile.java"));
        result = validatorEngine.validate(sourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        sourceCode = tLoader.readFile(getClass().getResourceAsStream("/EndlessMyCounter.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = tLoader.readFile(getClass().getResourceAsStream("/MyBuilder.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = tLoader.readFile(getClass().getResourceAsStream("/MyCounter.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = tLoader.readFile(getClass().getResourceAsStream("/NetworkingClass.java"));
        result = validatorEngine.validate(sourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.net", result.getReason());
    }

}
