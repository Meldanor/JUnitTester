import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import de.meldanor.junittester.compiler.CharSequenceCompiler;
import de.meldanor.junittester.compiler.CharSequenceCompilerException;

public class JUnitTest {

    @Test
    public void test() throws ClassCastException, CharSequenceCompilerException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> builderClass = com.compileJavaFile("MyBuilder", getClass().getResourceAsStream("/MyBuilder.java"));
        assertNotNull(builderClass);
        Class<Object> testClass = com.compileJavaFile("TestMyBuilder", getClass().getResourceAsStream("/TestMyBuilder.java"));
        assertNotNull(testClass);

        assertEquals(0, JUnitCore.runClasses(testClass).getFailureCount());
    }

}
