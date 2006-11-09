/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode.injector.antTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.Vector;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import net.sourceforge.fenixedu.injectionCode.injector.CodeGenerator;
import net.sourceforge.fenixedu.injectionCode.injector.ChecksInjector;
import net.sourceforge.fenixedu.injectionCode.injector.ChecksInjector.InvalidReturnTypeForGetter;
import net.sourceforge.fenixedu.injectionCode.injector.ChecksInjector.NoGetterAvaliableForSpecifiedSlot;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 10:58:53,24/Nov/2005
 * @version $Id$
 */
public class InjectCode extends Task {
   
    private File targetClassesDir;

    private Path injectionClassPath;

    private boolean verbose;

    private FileSet fileset;

    private String codeProducerClassName;

    private String annotation;

    protected Vector<FileSet> filesets = new Vector();

    public String getAnnotation() {
	return annotation;
    }

    public void setAnnotation(String annotation) {
	this.annotation = annotation;
    }

    public Path getInjectionClassPath() {
	return injectionClassPath;
    }

    public void setInjectionClassPath(Path injectionClassPath) {
	this.injectionClassPath = injectionClassPath;
    }

    public boolean getVerbose() {
	return verbose;
    }

    public void setVerbose(boolean verbose) {
	this.verbose = verbose;
    }

    private CodeGenerator producerClassByName() throws ClassNotFoundException,
	    ClassCastException, InstantiationException, IllegalAccessException {
	return (CodeGenerator) Class.forName(this.codeProducerClassName).newInstance();
    }

    private Class annotationClassByName(String className) throws ClassNotFoundException {
	return Class.forName(className);
    }

    private boolean isClassFile(String file) {
	return file.endsWith(".class");
    }

    @Override
    public void execute() {
	long before = System.currentTimeMillis();
	this.checkMandatoryAttributes();
	int processedFiles = 0;
	int changedFiles = 0;
	Collection<String> searchPaths = new ArrayList<String>();
	for (int j = 0; j < this.injectionClassPath.list().length; j++) {
	    searchPaths.add(this.injectionClassPath.list()[j]);
	}
	ChecksInjector injector;
	try {
	    injector = new ChecksInjector(this.verbose, searchPaths);
	} catch (NotFoundException e) {
	    throw new BuildException("Invalid search paths", e);
	}

	String[] files = this.includedFiles();
	if (this.verbose) {
	    System.out.println("Will process " + files.length + " files");
	}
	for (int i = 0; i < files.length; i++) {
	    if (this.isClassFile(files[i])) {
		final String separator = File.separator.equals("\\") ? "\\\\" : File.separator;
		String nextClass = files[i].replaceAll(separator, ".").substring(0,
			files[i].lastIndexOf("."));
		try {
		    boolean changed = injector.perform(nextClass, targetClassesDir.getAbsolutePath(),
			    this.producerClassByName(), this
				    .annotationClassByName(this.annotation));
		    if (changed)
			changedFiles++;
		    processedFiles++;
		} catch (NotFoundException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f.format("C %s not found.\nCause:", files[i], e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (CannotCompileException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f.format("Could not compile file %s with the injected code.\nCause:", files[i], e);		    
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (IOException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f.format("IO Exception: %s.", e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());

		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f.format("ClassNotFound Exception: %s.", e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (ClassCastException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "Provided generator does not implement AccessControlCodeGenerator.\n Exception: %s.",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (InstantiationException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "Provided generator does not have an empty public constructor.\n Exception: %s.",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f.format("Illegal access to class constructor.\n Exception: %s.", e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (SecurityException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "Not allowed to access the \"value\" field for the specified annotation.\n Exception: %s.",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (IllegalArgumentException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "Error accessing the present \"value\" in the specified annotation. This really should not happen.\n Exception: %s",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (NoSuchMethodException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "The specified annotation does not have the needed \"value\" (specifies the getter for the allowed group).\n Exception: %s.",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "Error accessing the present \"value\" in the specified annotation. This really should not happen.\n Exception: %s",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (NoGetterAvaliableForSpecifiedSlot e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f
			    .format(
				    "Could not access the property getter for the specified slot (please verify the value you gave to the checker annotation.\n Example: \"@Checked(\"xpto\")\". In this example getXpto() must exist in the class (or any superclass) that declares the annotated method.\n Exception: %s",
				    e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		} catch (InvalidReturnTypeForGetter e) {
		    e.printStackTrace();
		    StringBuilder message = new StringBuilder();
		    Formatter f = new Formatter(message);
		    f.format("Invalid return type for access control slot.\n Exception: %s", e);
		    System.err.printf(message.toString());
		    throw new BuildException(message.toString());
		}
	    } else {
		if (this.verbose) {
		    System.out.printf("Skipped %s as it is not a class file (does not end in .class)",
			    files[i]);
		}
	    }
	}
	long after = System.currentTimeMillis();
	System.out.printf("\nProcessed %d file%s. %d class%s re-written (%s).\nDone.", processedFiles,
		processedFiles == 0 || processedFiles > 1 ? "s" : "", changedFiles, changedFiles == 0
			|| changedFiles > 1 ? "es were" : " was", this.calcHMS((after - before) / 1000));
    }

    protected String calcHMS(long timeInSeconds) {
	long hours, minutes, seconds;
	hours = timeInSeconds / 3600;
	timeInSeconds = timeInSeconds - (hours * 3600);
	minutes = timeInSeconds / 60;
	timeInSeconds = timeInSeconds - (minutes * 60);
	seconds = timeInSeconds;
	StringBuilder format = new StringBuilder();
	Formatter f = new Formatter(format);
	f.format("%d minute%s, %d second%s", minutes, minutes > 0 || minutes == 0 ? "s" : "", seconds,
		seconds > 0 || seconds == 0 ? "s" : "");

	return format.toString();
    }

    /**
     * 
     */
    private String[] includedFiles() {
	Vector<String> fileNamesVector = new Vector<String>();

	for (int i = 0; i < filesets.size(); i++) {
	    FileSet fs = (FileSet) filesets.elementAt(i);
	    DirectoryScanner ds = fs.getDirectoryScanner(this.getProject());
	    String[] fileSetFiles = ds.getIncludedFiles();
	    for (int j = 0; j < fileSetFiles.length; j++) {
		fileNamesVector.add(fileSetFiles[j]);
	    }
	}
	String[] result = new String[fileNamesVector.size()];
	for (int i = 0, size = fileNamesVector.size(); i < size; i++) {
	    result[i] = fileNamesVector.elementAt(i);
	}

	return result;
    }

    /**
     * 
     */
    private void checkMandatoryAttributes() {
	if (this.codeProducerClassName == null)
	    throw new BuildException("codeProducerClassName is a mandatory attribute");
	if (this.annotation == null)
	    throw new BuildException("accessControlAnnotation is a mandatory attribute");
	if (this.targetClassesDir == null)
	    throw new BuildException("targetClassesDir is a mandatory attribute");
    }

    public void addFileset(FileSet set) {
	filesets.addElement(set);
    }

    public File getTargetClassesDir() {
	return targetClassesDir;
    }

    public void setTargetClassesDir(File targetClassesDir) {
	this.targetClassesDir = targetClassesDir;
    }

    public void setClasspath(Path classpath) {
	if (injectionClassPath == null) {
	    injectionClassPath = classpath;
	} else {
	    injectionClassPath.append(classpath);
	}
    }

    public Path getClasspath() {
	return injectionClassPath;
    }

    /**
     * Adds a path to the classpath.
     * 
     * @return a class path to be configured
     */
    public Path createClasspath() {
	if (injectionClassPath == null) {
	    injectionClassPath = new Path(getProject());
	}
	return injectionClassPath.createPath();
    }

    public String getCodeProducerClassName() {
	return codeProducerClassName;
    }

    public void setCodeProducerClassName(String codeProducerClass) {
	this.codeProducerClassName = codeProducerClass;
    }

    public FileSet getFileset() {
	return fileset;
    }

    public void setFileset(FileSet fileSet) {
	this.fileset = fileSet;
    }

}
