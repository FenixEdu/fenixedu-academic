package pt.utl.ist.analysis;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

import java.util.ArrayList;

public class DomainDrivenClassFilesVisitor extends ClassFilesVisitor {

    private DomainModelInfo modelInfo = null;
    private ArrayList<String> dmlFiles = new ArrayList<String>();
    private ArrayList<String> ignoreClasses = new ArrayList<String>();

    public void processArgs(String[] args) {
        int i = 0;
        while (i < args.length) {
            if ("-i".equals(args[i])) {
                ignoreClasses.add(getNextArg(args, i));
                consumeArg(args, i);
                i += 2;
            } else if ("-d".equals(args[i])) {
                dmlFiles.add(getNextArg(args, i));
                consumeArg(args, i);
                i += 2;
            } else {
                i++;
            }
        }
        super.processArgs(args);
    }

    protected DomainModelInfo getModelInfo() {
        if (modelInfo == null) {
            if (dmlFiles.isEmpty()) {
                throw new Error("No DML files specified");
            } else {
                modelInfo = new DomainModelInfo(dmlFiles);
            }
        }
        return modelInfo;
    }

    protected boolean descBelongsToDomainModel(String desc) {
        return getModelInfo().belongsToDomainModel(descToName(desc));
    }

    protected boolean shouldProcessClass(String name) {
        for (String ignoreRe : ignoreClasses) {
            if (name.matches(ignoreRe)) {
                return false;
            }
        }
        return true;
    }

    protected ClassVisitor makeNewClassVisitor() {
        return new DefaultClassVisitor();
    }
    
    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, String className) {
        return mv;
    }

    public static String[] stripArgs(String[] args, int num) {
        String[] newArgs = new String[args.length - num];
        System.arraycopy(args, num, newArgs, 0, args.length - num);
        return newArgs;
    }

    public class DefaultClassVisitor extends ClassAdapter {
        private String className = null;
        private boolean processIt = true;

        public DefaultClassVisitor() {
            super(new EmptyVisitor());
        }

        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name;
            this.processIt = shouldProcessClass(descToName(name));
            super.visit(version, access, name, signature, superName, interfaces);
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if (processIt) {
                return makeMethodVisitor(mv, className);
            } else {
                return mv;
            }
        }
    }
}
