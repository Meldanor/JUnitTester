import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.meldanor.junittester.io.TextFileLoader;
import de.meldanor.junittester.validate.ContentValidator;
import de.meldanor.junittester.validate.IOContentValidator;
import de.meldanor.junittester.validate.ContentValidateEngine.ContentValidatorResult;

public class IOContentCheckTest {

    private static String maliciousSourceCode;
    private static String valideSourceCode;
    private static ContentValidator validator;

    @BeforeClass
    public static void beforeClass() {
        TextFileLoader loader = new TextFileLoader();
        maliciousSourceCode = loader.readFile(IOContentCheckTest.class.getResourceAsStream("/DeleteFile.java"));
        valideSourceCode = loader.readFile(IOContentCheckTest.class.getResourceAsStream("/MyBuilder.java"));
        validator = new IOContentValidator();
    }

    @Test
    public void simpleTest() {

        ContentValidatorResult result = validator.validateCode("import java.io");
        assertFalse(result.isValid());
        assertEquals("package java.io is not allowed", result.getReason());

        result = validator.validateCode("import java.nio");
        assertFalse(result.isValid());
        assertEquals("package java.nio is not allowed", result.getReason());

        result = validator.validateCode("import java.lang");
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void maliciousFileTest() {

        ContentValidatorResult result = validator.validateCode(maliciousSourceCode);
        assertFalse(result.isValid());
        assertEquals("package java.io is not allowed", result.getReason());
    }

    @Test
    public void valideFileTest() {

        ContentValidatorResult result = validator.validateCode(valideSourceCode);
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }
}
