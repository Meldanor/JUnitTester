import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class ClassFileReaderTest {

    @Test
    public void testFileFromStream() {
        CompleteFileReader cReader = new CompleteFileReader();
        assertNotNull(cReader);

        String s = cReader.readFile(getClass().getResourceAsStream("/Counter.java"));
        assertNotNull(s);
        assertFalse(s.isEmpty());
        assertTrue(s.contains("public class Counter"));
    }

    @Test
    public void testa() throws FileNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        CacheCompiler c = new CacheCompiler();
        Class<?> clazz = c.loadClass("src/test/resources/Counter.java");
        Object o = clazz.newInstance();
        Method m = clazz.getMethod("count");
        m.invoke(m);

        Method m2 = clazz.getMethod("toString");
        String s = (String) m2.invoke(o);
        System.out.println(s);
//        clazz.getMethod(name, parameterTypes)
    }

}
