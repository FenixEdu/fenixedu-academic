

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
	public IPersistentMWTreatedEnrollment getIPersistentMWTreatedEnrollment();
	public IPersistentMWCurricularCourseScope getIPersistentMWCurricularCourseScope();
	public IPersistentMWCurricularCourse getIPersistentMWCurricularCourse();
	
	public IPersistentMWStudentClass getIPersistentMWStudentClass();
	public IPersistentMWDegreeTranslation getIPersistentMWDegreeTranslation();
	public IPersistentMWCurricularCourseOutsideStudentDegree getIPersistentMWCurricularCourseOutsideStudentDegree();
	public IPersistentMWUniversity getIPersistentMWUniversity();
	public IPersistentMWGrupoIleec getIPersistentMWGruposILeec();
	public IPersistentMWAreaEspecializacaoIleec getIPersistentMWAreasEspecializacaoIleec();
	public IPersistentMWAreaSecundariaIleec getIPersistentMWAreaSecundariaIleec();
	public IPersistentMWDisciplinaIleec getIPersistentMWDisciplinaIleec();
	public IPersistentMWDisciplinaGrupoIleec getIPersistentMWDisciplinaGrupoIleec();
	public IPersistentMWEquivalenciaIleec getIPersistentMWEquivalenciasIleec();
	public IPersistentMWDisciplinaIleec getIPersistentMWDisciplinasIleec();
	public IPersistentMWTipoEquivalenciaIleec getIPersistentMWTipoEquivalenciaIleec();
	public IPersistentMWAreaCientificaIleec getIPersistentMWAreaCientificaIleec();
	public IPersistentMWPrecedenciaDisciplinaDisciplinaIleec getIPersistentMWPrecedenciaDisciplinaDisciplinaIleec();
	public IPersistentMWPrecedenciaNumeroDisciplinasIleec getIPersistentMWPrecedenciaNumeroDisciplinas();
}