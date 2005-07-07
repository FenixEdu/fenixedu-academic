package net.sourceforge.fenixedu.tools;

import org.objectweb.asm.*;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

import java.net.URL;

import java.util.HashSet;
import java.util.Iterator;

import dml.*;

public class AddOJBConstructors extends ClassLoader implements Opcodes {
    private static final String CONSTRUCTOR_DESC = "(Lorg/apache/ojb/odmg/OJB;)V";

    private DomainModel domainModel;
    private HashSet<String> loadedClasses = new HashSet<String>();

    AddOJBConstructors(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    static String descToName(String desc) {
        return desc.replace('/', '.');
    }

    static String nameToDesc(String name) {
        return name.replace('.', '/');
    }

    public boolean isDomainBaseClass(String name) {
        return (name.endsWith("_Base") && (domainModel.findClass(name.substring(0, name.length() - 5)) != null));
    }

    public boolean isDomainNonBaseClass(String name) {
        return (domainModel.findClass(name) != null);
    }

    public boolean belongsToDomainModel(String name) {
        return isDomainNonBaseClass(name) || isDomainBaseClass(name);
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (belongsToDomainModel(name) && (! loadedClasses.contains(name))) {
            //System.err.println("Adding constructor for OJB to class '" + name + "'");
        } else {
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
            ClassVisitor cv = new AddOJBConstructorClassAdapter(cw);
            cr.accept(cv, false);
            bytecode = cw.toByteArray();
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
        
        
        try {
            FileOutputStream fos = new FileOutputStream(new File(classURL.toURI()));
            fos.write(bytecode);
            fos.close();
        } catch (Exception e) {
            throw new Error("Couldn't rewrite class file: " + e);
        }

        loadedClasses.add(name);

        return defineClass(name, bytecode, 0, bytecode.length);
    }
    
    public static void main (final String args[]) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: AddOJBConstructors <DomainAllocator classFullName> <dml file 1> ... <dml file n>");
        }

        String[] dmlFiles = new String[args.length - 1];
        System.arraycopy(args, 1, dmlFiles, 0, args.length - 1);
        DomainModel domainModel = DmlCompiler.getDomainModel(dmlFiles, true);

        AddOJBConstructors loader = new AddOJBConstructors(domainModel);        
        for (Iterator iter = domainModel.getClasses(); iter.hasNext();) {
            loader.loadClass(((DomainClass)iter.next()).getFullName());
        }

        String domainAllocFullName = args[0];
        dumpDomainAllocatorClass(domainAllocFullName, domainModel);
    }

    public static void dumpDomainAllocatorClass(String domainAllocFullName, DomainModel model) throws Exception {
        ClassWriter cw = new ClassWriter(true);

        String domainAllocClassDesc = nameToDesc(domainAllocFullName);
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, domainAllocClassDesc, null, "java/lang/Object", null);

        String sourceName = domainAllocFullName.substring(domainAllocFullName.lastIndexOf('.') + 1);
        cw.visitSource(sourceName + ".java", null);

        MethodVisitor mv;
        // The default empty constructor
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        // write alloc methods
        for (Iterator iter = model.getClasses(); iter.hasNext();) {
            String domClassName = ((DomainClass)iter.next()).getFullName();

            String domClassDesc = nameToDesc(domClassName);
            String methodName = "allocate_" + domClassName.replace('.', '_');
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, methodName, "()L" + domClassDesc + ";", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, domClassDesc);
            mv.visitInsn(DUP);
            mv.visitInsn(ACONST_NULL);
            mv.visitMethodInsn(INVOKESPECIAL, domClassDesc, "<init>", CONSTRUCTOR_DESC);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        FileOutputStream fos = new FileOutputStream(domainAllocClassDesc + ".class");
        fos.write(cw.toByteArray());
        fos.close();
    }


    class AddOJBConstructorClassAdapter extends ClassAdapter implements Opcodes {
        private String classDesc = null;
        private String superDesc = null;
        private boolean foundConstructor = false;
        private boolean warnOnFiels = false;

        public AddOJBConstructorClassAdapter(ClassVisitor cv) {
            super(cv);
        }

        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.classDesc = name;
            this.superDesc = superName;
            this.warnOnFiels = isDomainNonBaseClass(descToName(classDesc));
            super.visit(version, access, name, signature, superName, interfaces);
        }

        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            if (warnOnFiels && ((access & ACC_STATIC) == 0)) {
                System.err.println(classDesc + ": field not declared on base class -> " + name);
            }
            return super.visitField(access, name, desc, signature, value);
        }

        public void visitEnd() {
            if (! foundConstructor) {
                // force it
                visitMethod(ACC_PUBLIC, "<init>", CONSTRUCTOR_DESC, null, null);
            }
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if ("<init>".equals(name) && CONSTRUCTOR_DESC.equals(desc)) {
                // we process it and remove the original, if any, by returning null
                mv.visitCode();
                boolean initWrappers = isDomainBaseClass(descToName(classDesc));
                if (belongsToDomainModel(descToName(superDesc))) {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitMethodInsn(INVOKESPECIAL, superDesc, "<init>", CONSTRUCTOR_DESC);
                    if (initWrappers) {
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitMethodInsn(INVOKESPECIAL, classDesc, "initWrappers", "()V");
                    }
                    mv.visitInsn(RETURN);
                    mv.visitMaxs(2, 2);
                } else {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKESPECIAL, superDesc, "<init>", "()V");
                    if (initWrappers) {
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitMethodInsn(INVOKESPECIAL, classDesc, "initWrappers", "()V");
                    }
                    mv.visitInsn(RETURN);
                    mv.visitMaxs(1, 2);
                }
                mv.visitEnd();

                foundConstructor = true;
                return null;
            } else {
                return mv;                 
            }
        }
    }
}
