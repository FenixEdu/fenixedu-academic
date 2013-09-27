package net.sourceforge.fenixedu.util.report;

import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRAbstractMultiClassCompiler;

public class FenixJasperCompiler extends JRAbstractMultiClassCompiler {

    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    @Override
    public String compileClasses(File[] sourceFiles, String classpath) throws JRException {
        // Retrieve files from the standard location
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> sources = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFiles));

        CompilationTask task =
                compiler.getTask(new StringWriter(), fileManager, null, Arrays.asList("-classpath", classpath), null, sources);

        if (!task.call()) {
            throw new JRException("Error compiling report java source files");
        }

        return null;
    }

}
