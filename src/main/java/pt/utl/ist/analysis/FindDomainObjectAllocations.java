package pt.utl.ist.analysis;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FindDomainObjectAllocations extends DomainDrivenClassFilesVisitor implements Opcodes {

    private int totalNews = 0;

    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String methodName, final String className) {
        return new MethodAdapter(mv) {
                public void visitTypeInsn(int opcode, String desc) {
                    if ((opcode == NEW) && getModelInfo().belongsToDomainModel(descToName(desc))) {
                        totalNews++;
                        System.out.println(className + ": method " + methodName + " -> new " + desc);
                    }
                    super.visitTypeInsn(opcode, desc);
                }
            };
    }

    public static void main (final String args[]) throws Exception {
        FindDomainObjectAllocations visitor = new FindDomainObjectAllocations();
        visitor.processArgs(args);
        visitor.start();

        System.out.println("Total allocations = " + visitor.totalNews);
    }
}
