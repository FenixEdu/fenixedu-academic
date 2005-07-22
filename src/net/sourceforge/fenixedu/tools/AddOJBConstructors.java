package net.sourceforge.fenixedu.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import dml.DomainClass;

public class AddOJBConstructors extends AbstractDomainPostprocessor {
    private static final String CONSTRUCTOR_DESC = "(Lorg/apache/ojb/odmg/OJB;)V";

    public static void main (final String args[]) {
        AddOJBConstructors loader = new AddOJBConstructors();
        loader.processArgs(args);
        loader.start();
    }

    protected ClassVisitor makeNewClassVisitor(ClassWriter cw) {
        return new AddOJBConstructorClassAdapter(cw);
    }

    protected void finishedProcessingClass(URL classURL, byte[] classBytecode) {
        super.finishedProcessingClass(classURL, classBytecode);

        try {
            FileOutputStream fos = new FileOutputStream(new File(classURL.toURI()));
            fos.write(classBytecode);
            fos.close();
        } catch (Exception e) {
            throw new Error("Couldn't rewrite class file: " + e);
        }
    }

    public void start() {
        super.start();
        dumpDomainAllocatorClass();
    }
    
    public void dumpDomainAllocatorClass() {
        ClassWriter cw = new ClassWriter(true);

        String domainAllocFullName = this.classFullName;
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
        for (Iterator iter = getModel().getClasses(); iter.hasNext();) {
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

        try {
            FileOutputStream fos = new FileOutputStream(domainAllocClassDesc + ".class");
            fos.write(cw.toByteArray());
            fos.close();
        } catch (Exception e) {
            throw new Error("Couldn't write DomainAllocator class file: " + e);
        }
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
