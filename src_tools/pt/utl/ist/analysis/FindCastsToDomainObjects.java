package pt.utl.ist.analysis;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class FindCastsToDomainObjects extends DomainDrivenClassFilesVisitor implements Opcodes {

    private int totalCasts = 0;

    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String methodName, final String className) {
        return new MethodAdapter(mv) {
                public void visitTypeInsn(int opcode, String desc) {
                    if ((opcode == CHECKCAST) && getModelInfo().isDomainClass(descToName(desc))) {
                        totalCasts++;
                        System.out.println(className + ": method " + methodName + " -> cast to " + desc);
                    }
                    super.visitTypeInsn(opcode, desc);
                }
            };
    }

    public static void main (final String args[]) throws Exception {
        FindCastsToDomainObjects visitor = new FindCastsToDomainObjects();
        visitor.processArgs(args);
        visitor.start();

        System.out.println("Total casts = " + visitor.totalCasts);
    }
}
