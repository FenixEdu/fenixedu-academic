package pt.utl.ist.analysis;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class FindInstanceofsDomainObjects extends DomainDrivenClassFilesVisitor implements Opcodes {

    private int totalInstanceofs = 0;

    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String methodName, final String className) {
        return new MethodAdapter(mv) {
                public void visitTypeInsn(int opcode, String desc) {
                    if ((opcode == INSTANCEOF) && getModelInfo().isDomainClass(descToName(desc))) {
                        totalInstanceofs++;
                        System.out.println(className + ": method " + methodName + " -> instanceof " + desc);
                    }
                    super.visitTypeInsn(opcode, desc);
                }
            };
    }

    public static void main (final String args[]) throws Exception {
        FindInstanceofsDomainObjects visitor = new FindInstanceofsDomainObjects();
        visitor.processArgs(args);
        visitor.start();

        System.out.println("Total instanceofs = " + visitor.totalInstanceofs);
    }
}
