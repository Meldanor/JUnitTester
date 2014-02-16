import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class Core {

    public static void main(String[] args) {
        try {
            CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
            Class<Object> builderClass = com.compileJavaFile("MyBuilder", Core.class.getResourceAsStream("/MyBuilder.java"));
            if (builderClass == null)
                System.err.println("Can't compile builder class");
            Class<Object> testClass = com.compileJavaFile("TestMyBuilder", Core.class.getResourceAsStream("/TestMyBuilder.java"));

            Result result = JUnitCore.runClasses(testClass);
            System.out.println(result.getFailureCount());
            System.out.println(result.getFailures());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
