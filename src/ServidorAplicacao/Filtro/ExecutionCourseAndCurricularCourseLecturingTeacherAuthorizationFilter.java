/*
 * Created on 19/Mai/2003
 *
 * 
 */
package ServidorAplicacao.Filtro;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *
 */
public class ExecutionCourseAndCurricularCourseLecturingTeacherAuthorizationFilter
	extends AuthorizationByRoleFilter {

	public final static ExecutionCourseAndCurricularCourseLecturingTeacherAuthorizationFilter instance =
		new ExecutionCourseAndCurricularCourseLecturingTeacherAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType() {
		return RoleType.TEACHER;
	}

	public void preFiltragem(
		IUserView id,
		IServico servico,
		Object[] argumentos)
		throws Exception {
		if ((id == null)
			|| (id.getRoles() == null)
			|| !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
			|| !lecturesExecutionCourse(id, argumentos)
			|| !CurricularCourseBelongsExecutionCourse(id, argumentos)) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean CurricularCourseBelongsExecutionCourse(
		IUserView id,
		Object[] argumentos) {
		InfoExecutionCourse infoExecutionCourse = null;
		IExecutionCourse executionCourse = null;
		ICurricularCourse curricularCourse = null;
		InfoCurricularCourse infoCurricularCourse = null;
		ISuportePersistente sp;
		if (argumentos == null) {
			return false;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentCurricularCourse persistentCurricularCourse =
				sp.getIPersistentCurricularCourse();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
				executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse,false);		
			} else {
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(
						new DisciplinaExecucao((Integer) argumentos[0]),
						false);
			}
			if (argumentos[1] instanceof InfoCurricularCourse) {
				infoCurricularCourse = (InfoCurricularCourse) argumentos[1];
				curricularCourse =
					Cloner.copyInfoCurricularCourse2CurricularCourse(
						infoCurricularCourse);
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse,false);		
			} else {
				curricularCourse =
					(ICurricularCourse) persistentCurricularCourse.readByOId(
						new CurricularCourse((Integer) argumentos[1]),
						false);
			}
			

		} catch (Exception e) {
			return false;
		}
		return executionCourse.getAssociatedCurricularCourses().contains(curricularCourse);
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean lecturesExecutionCourse(
		IUserView id,
		Object[] argumentos) {
		InfoExecutionCourse infoExecutionCourse = null;
		IExecutionCourse executionCourse = null;
		ISuportePersistente sp;
		IProfessorship professorship = null;
		if (argumentos == null) {
			return false;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
			} else {
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(
						new DisciplinaExecucao((Integer) argumentos[0]),
						false);
			}

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher =
				persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());
			if (teacher != null && executionCourse != null) {
				IPersistentProfessorship persistentProfessorship =
					sp.getIPersistentProfessorship();
				professorship =
					persistentProfessorship.readByTeacherAndExecutionCoursePB(
						teacher,
						executionCourse);
			}
		} catch (Exception e) {
			return false;
		}
		return professorship != null;
	}

}
