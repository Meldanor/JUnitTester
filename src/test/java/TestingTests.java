import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.meldanor.junittester.compiler.CharSequenceCompiler;
import de.meldanor.junittester.compiler.CharSequenceCompilerException;
import de.meldanor.junittester.io.TextFileLoader;
import de.meldanor.junittester.testing.JUnitTest;
import de.meldanor.junittester.testing.JUnitTest.JUnitTestBuilder;

public class TestingTests {

    @Test
    public void runStaticTest() throws ClassCastException, CharSequenceCompilerException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> builderClass = com.compileJavaFile("MyBuilder", getClass().getResourceAsStream("/MyBuilder.java"));
        assertNotNull(builderClass);
        Class<Object> testClass = com.compileJavaFile("TestMyBuilder", getClass().getResourceAsStream("/TestMyBuilder.java"));
        assertNotNull(testClass);

        assertEquals(0, JUnitCore.runClasses(testClass).getFailureCount());
    }

    @Test
    public void runCorrectDynamicTest() throws ClassCastException, CharSequenceCompilerException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> counterClass = com.compileJavaFile("MyCounter", getClass().getResourceAsStream("/MyCounter.java"));
        assertNotNull(counterClass);

        TextFileLoader loader = new TextFileLoader();
        assertNotNull(loader);
        String content = loader.readFile(getClass().getResourceAsStream("/TestCounterPattern.java"));
        assertNotNull(content);

        // Create builder
        JUnitTestBuilder builder = JUnitTest.create("TestCounter", content);
        // Set the correct class name
        JUnitTest test = builder.replaceClassTag("MyCounter").build();
        // Compile new generated file
        Class<Object> testClass = com.compile(test.getClassName(), test.getContent());

        assertNotNull(testClass);

        assertEquals(0, JUnitCore.runClasses(testClass).getFailureCount());
    }

    @Test(expected = CharSequenceCompilerException.class)
    public void runWrongDynamicTest() throws ClassCastException, CharSequenceCompilerException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> counterClass = com.compileJavaFile("MyCounter", getClass().getResourceAsStream("/MyCounter.java"));
        assertNotNull(counterClass);

        TextFileLoader loader = new TextFileLoader();
        assertNotNull(loader);
        String content = loader.readFile(getClass().getResourceAsStream("/TestCounterPattern.java"));
        assertNotNull(content);

        // Create builder
        JUnitTestBuilder builder = JUnitTest.create("TestCounter", content);
        // Set the wrong class name
        JUnitTest test = builder.replaceClassTag("MyBuilder").build();
        // Compile new generated file
        Class<Object> testClass = com.compile(test.getClassName(), test.getContent());

        assertNotNull(testClass);

        assertEquals(0, JUnitCore.runClasses(testClass).getFailureCount());
    }

    @Test(timeout = 2000)
    public void runEndlessLoop() throws ClassCastException, CharSequenceCompilerException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> counterClass = com.compileJavaFile("EndlessMyCounter", getClass().getResourceAsStream("/EndlessMyCounter.java"));
        assertNotNull(counterClass);

        TextFileLoader loader = new TextFileLoader();
        assertNotNull(loader);
        String content = loader.readFile(getClass().getResourceAsStream("/TestCounterPattern.java"));
        assertNotNull(content);

        // Create builder
        JUnitTestBuilder builder = JUnitTest.create("TestCounter", content);
        int timeout = 500;
        // Set the wrong class name and add a timeout limit of 500 seconds
        JUnitTest test = builder.replaceClassTag("EndlessMyCounter").setTimeOut(timeout).build();
        // Compile new generated file
        Class<Object> testClass = com.compile(test.getClassName(), test.getContent());

        assertNotNull(testClass);
        Result result = JUnitCore.runClasses(testClass);
        assertEquals("test(TestCounter): test timed out after " + timeout + " milliseconds", result.getFailures().get(0).toString());
    }
}
