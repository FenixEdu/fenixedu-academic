

package middleware.persistentMiddlewareSupport;




/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */

public interface IPersistentMiddlewareSupport {


	public IPersistentMWAluno getIPersistentMWAluno();
	public IPersistentMWBranch getIPersistentMWBranch();
	public IPersistentMWEnrolment getIPersistentMWEnrolment();
	public IPersistentMWStudentClass getIPersistentMWStudentClass();
	public IPersistentMWDegreeTranslation getIPersistentMWDegreeTranslation();
}


