package net.sourceforge.fenixedu.tools;

import java.io.File;
import java.io.FileWriter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CreateDomainFactoryClassSource extends AbstractDomainPostprocessor {

    private StringBuilder resultFile;

    public static void main(final String args[]) {
        CreateDomainFactoryClassSource loader = new CreateDomainFactoryClassSource();
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

    protected void addFactoryMethod(String classDesc, String constructorDesc) {

        String className = classDesc.replace('/', '.');
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);

        String arguments = constructorDesc.substring(1, constructorDesc.length() - 2);
        String[] argsArray = arguments.split(";");

        StringBuilder generatedMethod = new StringBuilder("\n\n\tpublic static " + className + " make"
                + simpleClassName + "(");
        StringBuilder invokedArgsByConstructor = new StringBuilder();

        for (int i = 0; i < argsArray.length; i++) {
            String argumentType = argsArray[i];

            if (argumentType.equals("Lorg/apache/ojb/odmg/OJB")) {
                return;
            }

            if (argumentType.length() == 0) {
                continue;
            }

            if (argumentType.matches("I+")) {// primitive types: int

                for (int j = 0; j < argumentType.length(); j++) {
                    generatedMethod.append(" int arg" + i + "_" + j);
                    invokedArgsByConstructor.append("arg" + i + "_" + j);

                    if (j < argumentType.length() - 1) {
                        invokedArgsByConstructor.append(",");
                        generatedMethod.append(",");
                    }
                }

            } else {
                argumentType = argumentType.substring(1); // remove "L"
                argumentType = argumentType.replace('/', '.');
                generatedMethod.append(" " + argumentType + " arg" + i);
                invokedArgsByConstructor.append(" arg" + i);
            }

            if (i < argsArray.length - 1) {
                generatedMethod.append(",");
                invokedArgsByConstructor.append(",");
            }

        }

        generatedMethod.append(") { \n\t\treturn new " + className + "("
                + invokedArgsByConstructor.toString() + ");\n\t}");

        this.resultFile.append(generatedMethod);

    }

    protected void closeFactoryWriter() {
        factoryWriter.visitEnd();

        try {

            this.resultFile.append("\n\n}");

            String resultPath = nameToDesc(this.classFullName) + ".java";

            final File file = new File(resultPath);
            file.getParentFile().mkdirs();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(resultFile.toString());
            fileWriter.close();

            System.out.println("Generated File: " + resultPath);

        } catch (Exception e) {
            throw new Error("Couldn't write DomainFactory class file: " + e);
        }
    }

    public void start() {
        initializeResultFile();
        initializeFactoryWriter();
        super.start();
        closeFactoryWriter();
    }

    private void initializeResultFile() {
        this.resultFile = new StringBuilder();
        String packageName = classFullName.substring(0, classFullName.lastIndexOf('.'));
        String simpleClassName = classFullName.substring(classFullName.lastIndexOf('.') + 1);
        resultFile.append("package " + packageName + ";\n\npublic class " + simpleClassName + "{\n");
    }

    protected ClassVisitor makeNewClassVisitor(ClassWriter cw) {
        return new WriteConstructorsToFactory(cw);
    }

    class WriteConstructorsToFactory extends ClassAdapter implements Opcodes {
        private String classDesc = null;

        public WriteConstructorsToFactory(ClassVisitor cv) {
            super(cv);
        }

        public void visit(int version, int access, String desc, String signature, String superName,
                String[] interfaces) {
            if (isDomainNonBaseClass(descToName(desc))) {
                this.classDesc = desc;
            }
            super.visit(version, access, desc, signature, superName, interfaces);
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (classDesc != null) {
                // process all public constructors
                if ("<init>".equals(name) && ((access & ACC_PUBLIC) != 0)) {
                    addFactoryMethod(classDesc, desc);
                }
            }

            return mv;
        }
    }
}
