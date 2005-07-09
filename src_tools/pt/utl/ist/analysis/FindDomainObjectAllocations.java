package pt.utl.ist.analysis;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

import java.io.File;

public class FindDomainObjectAllocations extends DomainDrivenClassFilesVisitor implements Opcodes {

    private int totalNews = 0;

    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String className) {
        return new MethodAdapter(mv) {
                public void visitTypeInsn(int opcode, String desc) {
                    if ((opcode == NEW) && descBelongsToDomainModel(desc)) {
                        totalNews++;
                        System.out.println(className + ": new " + desc);
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
