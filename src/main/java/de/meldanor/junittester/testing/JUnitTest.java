package de.meldanor.junittester.testing;

import java.util.regex.Pattern;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.meldanor.junittester.compiler.CharSequenceCompiler;
import de.meldanor.junittester.compiler.CharSequenceCompilerException;

public class JUnitTest {

    private String content;
    private final String className;

    private JUnitTest(String className, String content) {
        this.content = content;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }

    public <T> Class<T> compile(CharSequenceCompiler<T> compiler) throws ClassCastException, CharSequenceCompilerException {
        return compiler.compile(className, content);
    }
    
    public <T> Result runTest(CharSequenceCompiler<T> compiler) throws ClassCastException, CharSequenceCompilerException {
        Class<T> compiledClass = compile(compiler);
        return JUnitCore.runClasses(compiledClass);
    }

    public static JUnitTestBuilder create(String className, String content) {
        return new JUnitTestBuilder(className, content);
    }

    public static class JUnitTestBuilder {

        private JUnitTest product;

        private final static Pattern CLASS_TAG_PATTERN = Pattern.compile(Pattern.quote("${CLASS}"));
        private final static Pattern TEST_TAG_PATTERN = Pattern.compile(Pattern.quote("@Test"));

        private JUnitTestBuilder(String className, String content) {
            this.product = new JUnitTest(className, content);
        }

        public JUnitTestBuilder setClassTag(String className) {
            this.product.content = CLASS_TAG_PATTERN.matcher(this.product.getContent()).replaceAll(className);
            return this;
        }

        public JUnitTestBuilder setTimeOut(int timeout) {
            this.product.content = TEST_TAG_PATTERN.matcher(this.product.getContent()).replaceAll("@Test(timeout = " + timeout + ")");
            return this;
        }

        public JUnitTest build() {
            return this.product;
        }

    }

}
