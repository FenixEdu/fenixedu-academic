

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
	public IPersistentMWCurricularCourseScope getIPersistentMWCurricularCourseScope();
	public IPersistentMWCurricularCourse getIPersistentMWCurricularCourse();
	
	public IPersistentMWStudentClass getIPersistentMWStudentClass();
	public IPersistentMWDegreeTranslation getIPersistentMWDegreeTranslation();
	public IPersistentMWCurricularCourseOutsideStudentDegree getIPersistentMWCurricularCourseOutsideStudentDegree();
	public IPersistentMWUniversity getIPersistentMWUniversity();
	public IPersistentMWGruposIleec getIPersistentMWGruposILeec();
	public IPersistentMWAreasEspecializacaoIleec getIPersistentMWAreasEspecializacaoIleec();
	public IPersistentMWAreaSecundariaIleec getIPersistentMWAreaSecundariaIleec();
	public IPersistentMWDisciplinasIleec getIPersistentMWDisciplinaIleec();
	public IPersistentMWDisciplinaGrupoIleec getIPersistentMWDisciplinaGrupoIleec();
	public IPersistentMWEquivalenciasIleec getIPersistentMWEquivalenciasIleec();
	public IPersistentMWDisciplinasIleec getIPersistentMWDisciplinasIleec();
	public IPersistentMWTipoEquivalenciaIleec getIPersistentMWTipoEquivalenciaIleec();
}