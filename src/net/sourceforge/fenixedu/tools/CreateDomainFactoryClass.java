package net.sourceforge.fenixedu.tools;

import java.io.FileOutputStream;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


public class CreateDomainFactoryClass extends AbstractDomainPostprocessor {

    public static void main (final String args[]) {
        CreateDomainFactoryClass loader = new CreateDomainFactoryClass();
        loader.processArgs(args);
        loader.start();
    }

    private ClassWriter factoryWriter;

    protected void initializeFactoryWriter() {
        ClassWriter cw = new ClassWriter(true);

        String factoryFullName = this.classFullName;
        String factoryClassDesc = nameToDesc(factoryFullName);
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, factoryClassDesc, null, "java/lang/Object", null);

        String sourceName = factoryFullName.substring(factoryFullName.lastIndexOf('.') + 1);
        cw.visitSource(sourceName + ".java", null);

        // The default empty constructor
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        
        this.factoryWriter = cw;
    }

    protected void addFactoryMethod(String classDesc, String constructorDesc, String signature, String[] exceptions) {
        Type[] argTypes = Type.getArgumentTypes(constructorDesc);

        String methodName = "makeNew_" + classDesc.replace('/', '_');
        String methodDesc = Type.getMethodDescriptor(Type.getType("L" + classDesc + ";"), argTypes);

        MethodVisitor mv = factoryWriter.visitMethod(ACC_PUBLIC + ACC_STATIC, methodName, methodDesc, signature, exceptions);
        mv.visitCode();
        mv.visitTypeInsn(NEW, classDesc);
        mv.visitInsn(DUP);
        // push parameters into stack
        for (int i = 0; i < argTypes.length; i++) {
            mv.visitVarInsn(argTypes[i].getOpcode(ILOAD), i);
        }
        mv.visitMethodInsn(INVOKESPECIAL, classDesc, "<init>", constructorDesc);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(argTypes.length + 2, argTypes.length);
        mv.visitEnd();
    }

    protected void closeFactoryWriter() {
        factoryWriter.visitEnd();

        try {        
            FileOutputStream fos = new FileOutputStream(nameToDesc(this.classFullName) + ".class");
            fos.write(factoryWriter.toByteArray());
            fos.close();
        } catch (Exception e) {
            throw new Error("Couldn't write DomainFactory class file: " + e);
        }
    }

    public void start() {
        initializeFactoryWriter();
        super.start();
        closeFactoryWriter();
    }

    protected ClassVisitor makeNewClassVisitor(ClassWriter cw) {
        return new WriteConstructorsToFactory(cw);
    }

    class WriteConstructorsToFactory extends ClassAdapter implements Opcodes {
        private String classDesc = null;

        public WriteConstructorsToFactory(ClassVisitor cv) {
            super(cv);
        }

        public void visit(int version, int access, String desc, String signature, String superName, String[] interfaces) {
            if (isDomainNonBaseClass(descToName(desc))) {
                this.classDesc = desc;
            }
            super.visit(version, access, desc, signature, superName, interfaces);
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (classDesc != null) {
                // process all public constructors
                if ("<init>".equals(name) && ((access & ACC_PUBLIC) != 0)) {
                    addFactoryMethod(classDesc, desc, signature, exceptions);
                }
            }

            return mv;
        }
    }
}
