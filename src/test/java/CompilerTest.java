import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.AfterClass;
import org.junit.Test;

public class CompilerTest {

    @AfterClass
    public static void afterClass() {
        File f = new File("Counter.class");
        f.delete();
        f.deleteOnExit();
    }

    @Test
    public void test() throws FileNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        CacheCompiler c = new CacheCompiler();
        assertNotNull(c);

        Class<?> clazz = c.loadClass("src/test/resources/Counter.java");
        assertNotNull(clazz);
        assertEquals("Counter", clazz.getName());

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

}
