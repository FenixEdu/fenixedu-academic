package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaCientificaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaSecundariaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWPrecedenciaDisciplinaDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWPrecedenciaNumeroDisciplinasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWStudentClass;
import middleware.persistentMiddlewareSupport.IPersistentMWTipoEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWTreatedEnrollment;
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

	public IPersistentMWTreatedEnrollment getIPersistentMWTreatedEnrollment() {
		return new MWTreatedEnrollmentOJB();
	}

	public IPersistentMWEquivalenciaIleec getIPersistentMWEquivalenciasIleec()
	{
		return new MWEquivalenciaIleecOJB();
	}

	public IPersistentMWDisciplinaIleec getIPersistentMWDisciplinasIleec()
	{
		return new MWDisciplinaIleecOJB();
	}

	public IPersistentMWTipoEquivalenciaIleec getIPersistentMWTipoEquivalenciaIleec()
	{
		return new MWTipoEquivalenciaIleecOJB();
	}
	
	public IPersistentMWGrupoIleec getIPersistentMWGruposILeec()
	{
		return new MWGrupoIleecOJB();
	}

	public IPersistentMWAreaEspecializacaoIleec getIPersistentMWAreasEspecializacaoIleec()
	{
		return new MWAreaEspecializacaoIleecOJB();
	}

	public IPersistentMWAreaSecundariaIleec getIPersistentMWAreaSecundariaIleec()
	{
		return new MWAreaSecundariaIleecOJB();
	}
	
	public IPersistentMWDisciplinaIleec getIPersistentMWDisciplinaIleec()
	{
		return new MWDisciplinaIleecOJB();
	}
	
	public IPersistentMWDisciplinaGrupoIleec getIPersistentMWDisciplinaGrupoIleec()
	{
		return new MWDisciplinaGrupoIleecOJB();
	}
	
	public IPersistentMWAreaCientificaIleec getIPersistentMWAreaCientificaIleec()
	{
		return new MWAreaCientificaIleecOJB();
	}	

	public IPersistentMWPrecedenciaDisciplinaDisciplinaIleec getIPersistentMWPrecedenciaDisciplinaDisciplinaIleec()
	{

		return new MWPrecedenciaDisciplinaDisciplinaIleecOJB();
	}

	public IPersistentMWPrecedenciaNumeroDisciplinasIleec getIPersistentMWPrecedenciaNumeroDisciplinas()
	{
		return new MWPrecedenciaNumeroDisciplinasIleecOJB();
	}
}