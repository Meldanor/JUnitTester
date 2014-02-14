import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

}
