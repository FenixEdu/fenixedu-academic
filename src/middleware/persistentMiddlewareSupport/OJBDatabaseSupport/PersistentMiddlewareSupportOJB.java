package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWEquivalenciasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWStudentClass;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class PersistentMiddlewareSupportOJB implements IPersistentMiddlewareSupport {

	private static PersistentMiddlewareSupportOJB instance = null;

	public static synchronized PersistentMiddlewareSupportOJB getInstance() {
		if (instance == null) {
			instance = new PersistentMiddlewareSupportOJB();
		}
		return instance;
	}

	public static synchronized void resetInstance() {
		if (instance != null) {
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			instance = null;
		}
	}

	/** Creates a new instance of SuportePersistenteOJB */
	private PersistentMiddlewareSupportOJB() {
	}


	public IPersistentMWAluno getIPersistentMWAluno() {
		return new MWAlunoOJB();
	}

	public IPersistentMWBranch getIPersistentMWBranch() {
		return new MWBranchOJB();
	}

	public IPersistentMWEnrolment getIPersistentMWEnrolment() {
		return new MWEnrolmentOJB();
	}

	public IPersistentMWCurricularCourseScope getIPersistentMWCurricularCourseScope() {
		return new MWCurricularCourseScopeOJB();
	}

	public IPersistentMWCurricularCourse getIPersistentMWCurricularCourse() {
		return new MWCurricularCourseOJB();
	}

	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport#getIPersistentMWStudentClass()
	 */
	public IPersistentMWStudentClass getIPersistentMWStudentClass()
	{
		return new MWStudentClassOJB();
	}

	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport#getIPersistentMWDegreeTranslation()
	 */
	public IPersistentMWDegreeTranslation getIPersistentMWDegreeTranslation()
	{
		return new MWDegreeTranslationOJB();
	}

	public IPersistentMWCurricularCourseOutsideStudentDegree getIPersistentMWCurricularCourseOutsideStudentDegree()
	{
		return new MWCurricularCourseOutsideStudentDegreeOJB();
	}

	public IPersistentMWUniversity getIPersistentMWUniversity()
	{
		return new MWUniversityOJB();
	}

	public IPersistentMWEquivalenciasIleec getIPersistentMWEquivalenciasIleec()
	{
		return new MWEquivalenciaIleecOJB();
	}

	public IPersistentMWDisciplinasIleec getIPersistentMWDisciplinasIleec()
	{
		return new MWDisciplinaIleecOJB();
	}
}