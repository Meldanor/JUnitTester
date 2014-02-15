import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class CacheCompiler {

    private JavaCompiler compiler;
    private CompleteFileReader cReader;

    public CacheCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        // True, when JDK not installed or javac not linked
        if (this.compiler == null) {
            throw new RuntimeException("Can't find java compiler. Is JDK installed?");
        }

        this.cReader = new CompleteFileReader();
    }

    public Class<?> loadClass(String fileName) throws FileNotFoundException {
        String content = cReader.readFile(fileName);
        return loadClass(fileName, content);
    }

    public Class<?> loadClass(File file) throws FileNotFoundException {
        return loadClass(file.getName());
    }

    public Class<?> loadClass(String className, String classContent) {
        List<JavaObject> compileObjects = Arrays.asList(new JavaObject(className, classContent));

        // Collector for compilation information
        DiagnosticCollector<Object> dia = new DiagnosticCollector<Object>();
        // Create task to compile
        CompilationTask task = compiler.getTask(null, null, dia, null, null, compileObjects);
        // Start compiling
        boolean result = task.call();

        // Print out results - if compilation was successfull, nothing happens
        List<Diagnostic<? extends Object>> diagnostics = dia.getDiagnostics();
        StringBuilder sBuilder = new StringBuilder();
        for (Diagnostic<? extends Object> diagnostic : diagnostics) {
            sBuilder.append(diagnostic);
            sBuilder.append("\n");
        }

        if (result) {
            try {
                // Load the class using url loader
                URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();

                // Delete possible / and the ending .java from the class path
                className = className.substring(className.lastIndexOf('/') + 1);
                className = className.replaceAll(".java", "");
                // Load the class
                return urlClassLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println(sBuilder);
            return null;
        }
    }

    /**
     * Wrapper class for compiable objects created just be strings without an
     * existing file source
     */
    private class JavaObject extends SimpleJavaFileObject {

        private String content;

        public JavaObject(String className, String content) {
            super(URI.create(className), Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }

    }
}
