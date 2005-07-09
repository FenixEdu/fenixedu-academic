
package pt.utl.ist.analysis;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

public abstract class ClassFilesVisitor {

    private ArrayList<String> files = new ArrayList<String>();

    public void processArgs(String[] args) {
        int i = 0;
        while (i < args.length) {
            if ("-f".equals(args[i])) {
                files.add(getNextArg(args, i));
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
    

    public void start() {
        for (String file : files) {
            visitFile(new File(file));
        }
    }

    public void visitFile(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                visitFile(subFile);
            }
        } else {
            String fileName = file.getName();
            if (fileName.endsWith(".class")) {
                visitClassFile(file);
            }
        }
    }

    protected void visitClassFile(File classFile) {
        InputStream is = null;
        
        try {
            // get an input stream to read the bytecode of the class
            is = new FileInputStream(classFile);
            ClassReader cr = new ClassReader(is);
            ClassVisitor cv = makeNewClassVisitor();
            cr.accept(cv, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error processing class file: " + e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // intentionally empty
                }
            }
        }
    }
    
    protected abstract ClassVisitor makeNewClassVisitor();


    public static String descToName(String desc) {
        return desc.replace('/', '.');
    }

    public static String nameToDesc(String name) {
        return name.replace('.', '/');
    }
}
