import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.meldanor.junittester.check.ContentValidateEngine.ContentValidatorResult;
import de.meldanor.junittester.check.ContentValidator;
import de.meldanor.junittester.check.NetworkingContentValidator;
import de.meldanor.junittester.io.TextFileLoader;

public class NetworkingContentCheckTest {

    private static String maliciousSourceCode;
    private static String valideSourceCode;
    private static ContentValidator validator;

    @BeforeClass
    public static void beforeClass() {
        TextFileLoader loader = new TextFileLoader();
        maliciousSourceCode = loader.readFile(NetworkingContentCheckTest.class.getResourceAsStream("/NetworkingClass.java"));
        valideSourceCode = loader.readFile(NetworkingContentCheckTest.class.getResourceAsStream("/MyBuilder.java"));
        validator = new NetworkingContentValidator();
    }

    @Test
    public void simpleTest() {

        ContentValidatorResult result = validator.validateCode("import java.net");
        assertFalse(result.isValid());
        assertEquals("package java.net is not allowed", result.getReason());

        result = validator.validateCode("import javax.net");
        assertFalse(result.isValid());
        assertEquals("package javax.net is not allowed", result.getReason());

        result = validator.validateCode("import java.lang");
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void maliciousFileTest() {

        ContentValidatorResult result = validator.validateCode(maliciousSourceCode);
        assertFalse(result.isValid());
        assertEquals("package java.net is not allowed", result.getReason());
    }

    @Test
    public void valideFileTest() {

        ContentValidatorResult result = validator.validateCode(valideSourceCode);
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }
}
