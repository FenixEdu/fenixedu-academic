package net.sourceforge.fenixedu.tools;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainModel;

public abstract class AbstractDomainPostprocessor extends ClassLoader implements Opcodes {
    private ArrayList<String> dmlFiles = new ArrayList<String>();
    private HashSet<String> loadedClasses = new HashSet<String>();

    private DomainModel domainModel;
    protected String classFullName;

    // --------------------------------
    // HACK!!!
    //
    // All this command line processing is copied&pasted from 
    // src_tools/pt/utl/ist/analysis/ClassFilesVisitor
    // 
    // I should refactor this soon!

    public void processArgs(String[] args) {
        int i = 0;
        while (i < args.length) {
            if ("-d".equals(args[i])) {
                dmlFiles.add(getNextArg(args, i));
                consumeArg(args, i);
                i += 2;
            } else if ("-cfn".equals(args[i])) {
                classFullName = getNextArg(args, i);
                consumeArg(args, i);
                i += 2;
            } else if (args[i] != null) {
                throw new Error("Unknown argument: '" + args[i] + "'");
            } else {
                i++;
            }
        }
    }

    protected void consumeArg(String[] args, int i) {
        args[i] = null;
    }

    protected String getNextArg(String[] args, int i) {
        int next = i + 1;
        if ((next >= args.length) || (args[next] == null)) {
            throw new Error("Invalid argument following '" + args[i] + "'");
        }
        String result = args[next];
        consumeArg(args, next);
        return result;
    }
    

    protected DomainModel getModel() {
        if (domainModel == null) {
            if (dmlFiles.isEmpty()) {
                throw new Error("No DML files specified");
            } else {
                try {
                    domainModel = DmlCompiler.getFenixDomainModel(dmlFiles.toArray(new String[dmlFiles.size()]));
                } catch (antlr.ANTLRException ae) {
                    System.err.println("Error parsing the DML files, leaving the domain empty");
                }
            }
        }
        return domainModel;
    }

    public static String descToName(String desc) {
        return desc.replace('/', '.');
    }

    public static String nameToDesc(String name) {
        return name.replace('.', '/');
    }

    public boolean isDomainBaseClass(String name) {
        return (name.endsWith("_Base") && (getModel().findClass(name.substring(0, name.length() - 5)) != null));
    }

    public boolean isDomainNonBaseClass(String name) {
        return (getModel().findClass(name) != null);
    }

    public boolean belongsToDomainModel(String name) {
        return isDomainNonBaseClass(name) || isDomainBaseClass(name);
    }

    protected abstract ClassVisitor makeNewClassVisitor(ClassWriter cw);

    protected void finishedProcessingClass(URL classURL, byte[] classBytecode) {
        // do nothing by default
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if ((! belongsToDomainModel(name)) || loadedClasses.contains(name)) {
            return super.loadClass(name, resolve);
        }
        
        // find the resource for the class file
        URL classURL = getResource(nameToDesc(name) + ".class");
        if (classURL == null) {
            throw new ClassNotFoundException(name);
        }

        InputStream is = null;
        byte[] bytecode;
        
        try {
            // get an input stream to read the bytecode of the class
            is = classURL.openStream();
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(false);
            ClassVisitor cv = makeNewClassVisitor(cw);
            cr.accept(cv, false);
            bytecode = cw.toByteArray();
            finishedProcessingClass(classURL, bytecode);
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // intentionally empty
                }
            }
        }
        
        loadedClasses.add(name);

        return defineClass(name, bytecode, 0, bytecode.length);
    }


    public void start() {
        for (Iterator iter = getModel().getClasses(); iter.hasNext();) {
            String className = ((DomainClass)iter.next()).getFullName();
            try {
                loadClass(className);
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Error: Couldn't load class " + className + ": " + cnfe);
            }
        }
    }
}
