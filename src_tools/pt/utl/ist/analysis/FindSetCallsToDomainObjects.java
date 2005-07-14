package pt.utl.ist.analysis;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FindSetCallsToDomainObjects extends DomainDrivenClassFilesVisitor implements Opcodes {

    private int totalSets = 0;

    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String methodName, final String className) {
        // ignore domain objects' constructors
        if ("<init>".equals(methodName) && getModelInfo().isDomainClass(descToName(className))) {
            return mv;
        } else {
            return new MethodAdapter(mv) {
                    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                        if (name.startsWith("set") && getModelInfo().belongsToDomainModel(descToName(owner))) {
                            totalSets++;
                            System.out.println(className + ": method " + methodName + " -> calling " + name);
                        }
                        
                        super.visitMethodInsn(opcode, owner, name, desc);
                    }
            };
        }
    }

    public static void main (final String args[]) throws Exception {
        FindSetCallsToDomainObjects visitor = new FindSetCallsToDomainObjects();
        visitor.processArgs(args);
        visitor.start();

        System.out.println("Total sets = " + visitor.totalSets);
    }
}
