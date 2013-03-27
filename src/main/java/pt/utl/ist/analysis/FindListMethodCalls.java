package pt.utl.ist.analysis;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

public class FindListMethodCalls extends ClassFilesVisitor implements Opcodes {

    protected ClassVisitor makeNewClassVisitor() {
        return new DefaultClassVisitor();
    }

    public class DefaultClassVisitor extends ClassAdapter {
        private String className = null;

        public DefaultClassVisitor() {
            super(new EmptyVisitor());
        }

        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
	    return makeMethodVisitor(mv, name, className);
        }
    }
    
    static final String[] BAD = new String[] { 
	"add", "(ILjava/lang/Object;)V",
	"addAll", "(ILjava/util/Collection;)Z",
	"get", "(I)Ljava/lang/Object;",
	"indexOf", "(Ljava/lang/Object;)I",
	"lastIndexOf", "(Ljava/lang/Object;)I",
	"listIterator", "()Ljava/util/ListIterator;",
	"listIterator", "(I)Ljava/util/ListIterator;",
	"remove", "(I)Ljava/lang/Object;",
	"set", "(ILjava/lang/Object;)Ljava/lang/Object;",
	"subList", "(II)Ljava/util/List;"
    };


    protected MethodVisitor makeMethodVisitor(MethodVisitor mv, final String methodName, final String className) {
	return new MethodAdapter(mv) {		
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		    if ((opcode == INVOKEINTERFACE) && "java/util/List".equals(owner)) {
			for (int i = 0; i < BAD.length; i += 2) {
			    if (BAD[i].equals(name) && BAD[i+1].equals(desc)) {
				System.out.println(className + ": method " + methodName + " -> calling List's " + name);
			    }
			}
		    }
		    
		    super.visitMethodInsn(opcode, owner, name, desc);
		}
            };
    }

    public static void main (final String args[]) throws Exception {
        FindListMethodCalls visitor = new FindListMethodCalls();
        visitor.processArgs(args);
        visitor.start();
    }
}
