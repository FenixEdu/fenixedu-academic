package pt.utl.ist.analysis;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class FindSetCallsWithNoLockWrite extends DomainDrivenClassFilesVisitor implements Opcodes {

    private int totalSets = 0;

    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String methodName, final String className) {
        // ignore domain objects' constructors
        if ("<init>".equals(methodName) && getModelInfo().isDomainClass(descToName(className))) {
            return mv;
        } else {
            return new MethodAdapter(mv) {
                    private boolean foundLock = false;

                    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                        if (name.equals("simpleLockWrite") || name.equals("lockWrite")) {
                            foundLock = true;
                        } else if ((! foundLock) && name.startsWith("set") && getModelInfo().belongsToDomainModel(descToName(owner))) {
                            totalSets++;
                            System.out.println(className + ": method " + methodName + " -> calling " + name);
                        }
                        
                        super.visitMethodInsn(opcode, owner, name, desc);
                    }
            };
        }
    }

    public static void main (final String args[]) throws Exception {
        FindSetCallsWithNoLockWrite visitor = new FindSetCallsWithNoLockWrite();
        visitor.processArgs(args);
        visitor.start();

        System.out.println("Total sets = " + visitor.totalSets);
    }
}
