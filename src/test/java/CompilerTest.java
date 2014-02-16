import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import de.meldanor.junittester.compiler.CharSequenceCompiler;
import de.meldanor.junittester.compiler.CharSequenceCompilerException;
import de.meldanor.junittester.io.TextFileLoader;

public class CompilerTest {

    @Test
    public void counterClassTest() throws ClassCastException, CharSequenceCompilerException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        CharSequenceCompiler<Object> t = new CharSequenceCompiler<Object>();
        assertNotNull(t);

        Class<?> clazz = t.compileJavaFile("MyCounter", getClass().getResourceAsStream("/MyCounter.java"));
        assertNotNull(clazz);
        assertEquals("MyCounter", clazz.getName());

        Constructor<?> standardConstructor = clazz.getConstructor();
        assertNotNull(standardConstructor);

        Object counterObject = standardConstructor.newInstance();
        assertNotNull(counterObject);

        Method countMethod = clazz.getMethod("count");
        countMethod.invoke(counterObject);
        countMethod.invoke(counterObject);

        Method toStringMethod = clazz.getMethod("toString");
        assertEquals("Counter: 2", toStringMethod.invoke(counterObject));
    }

    @Test
    public void builderClassTest() throws ClassCastException, CharSequenceCompilerException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        CharSequenceCompiler<Object> t = new CharSequenceCompiler<Object>();
        assertNotNull(t);

        TextFileLoader c = new TextFileLoader();
        String javaSource = c.readFile(getClass().getResourceAsStream("/MyBuilder.java"));
        Class<?> clazz = t.compile("MyBuilder", javaSource);

        assertNotNull(clazz);
        assertEquals("MyBuilder", clazz.getName());

        Constructor<?> standardConstructor = clazz.getConstructor();
        assertNotNull(standardConstructor);

        Object builderObject = standardConstructor.newInstance();
        assertNotNull(builderObject);

        Method appendMethod = clazz.getMethod("append", String.class);
        appendMethod.invoke(builderObject, "Hello ");
        appendMethod.invoke(builderObject, "World");

        Method toStringMethod = clazz.getMethod("toString");
        assertEquals("Hello World", toStringMethod.invoke(builderObject));
    }
}
