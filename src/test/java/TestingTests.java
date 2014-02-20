import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.meldanor.junittester.compiler.CharSequenceCompiler;
import de.meldanor.junittester.compiler.CharSequenceCompilerException;
import de.meldanor.junittester.io.TextFileLoader;
import de.meldanor.junittester.testing.JUnitTest;

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

        JUnitTest unitTest = JUnitTest.create("TestCounter", content).setClassTag("MyCounter").build();
        assertNotNull(unitTest);
        Result testResult = unitTest.runTest(com);
        assertNotNull(testResult);

        assertEquals(0, testResult.getFailureCount());
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

        // Create test suite with unit class name
        JUnitTest test = JUnitTest.create("TestCounter", content).setClassTag("MyBuilder").build();
        // Here comes the exception
        test.compile(com);
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

        int timeout = 500;

        JUnitTest test = JUnitTest.create("TestCounter", content).setClassTag("EndlessMyCounter").setTimeOut(timeout).build();
        assertNotNull(test);
        assertTrue("JUnitTest doesn't have the Rule for timeout!", test.getContent().contains("@Rule\npublic Timeout globalTimeout = new Timeout(500);"));
        Result result = test.runTest(com);
        assertNotNull(result);
        assertEquals("test(TestCounter): test timed out after " + timeout + " milliseconds", result.getFailures().get(0).toString());
    }

    @Test
    public void sameClassnamesDifferentImplementation() throws ClassCastException, CharSequenceCompilerException {

        TextFileLoader loader = new TextFileLoader();
        String unitTestSource = loader.readFile(getClass().getResourceAsStream("/TestCounterPattern.java"));
        String correctSource = loader.readFile(getClass().getResourceAsStream("/MyCounter.java"));
        JUnitTest unitTest = JUnitTest.create("TestCounter", unitTestSource).setClassTag("MyCounter").build();

        // Correct class compiling and testing

        // Load correct working source
        // Compile sources
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> correctClass = com.compile("MyCounter", correctSource);
        assertNotNull(correctClass);

        Result result = unitTest.runTest(com);
        assertNotNull(result);
        assertEquals(0, result.getFailureCount());

        // Unload the current compiled classes
        com = null;
        System.gc();

        // Test wrong class after compiling and testing correctly

        com = new CharSequenceCompiler<Object>();
        // Modify to be wrong
        String wrongSource = correctSource.replaceAll(Pattern.quote("++counter"), "--counter");
        Class<Object> wrongClass = com.compile("MyCounter", wrongSource);
        assertNotNull(wrongClass);

        result = unitTest.runTest(com);
        assertEquals(1, result.getFailureCount());
        assertEquals("test(TestCounter): expected:<Counter: []2> but was:<Counter: [-]2>", result.getFailures().get(0).toString());
    }
}
