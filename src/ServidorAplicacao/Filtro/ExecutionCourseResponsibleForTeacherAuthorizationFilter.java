/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *  
 */
public class ExecutionCourseResponsibleForTeacherAuthorizationFilter
	extends AuthorizationByRoleFilter {

	public final static ExecutionCourseResponsibleForTeacherAuthorizationFilter instance =
		new ExecutionCourseResponsibleForTeacherAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the
	 *         authorization access to services.
	 */
	public static Filtro getInstance() {
		return instance;
	}
	/*
	 * (non-Javadoc)
	 * 
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
		if (((id != null
			&& id.getRoles() != null
			&& !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
			&& !isResponsibleForExecutionCourse(id, argumentos)))
			|| (id == null)
			|| (id.getRoles() == null)) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */

	private boolean isResponsibleForExecutionCourse(
		IUserView id,
		Object[] argumentos) {

		InfoExecutionCourse infoExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;
		ISuportePersistente sp;
		IResponsibleFor responsibleFor = null;
		if (argumentos == null) {
			return false;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
			} else {
				executionCourse =
					(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
						new DisciplinaExecucao((Integer) argumentos[0]),
						false);
			}

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher =
				persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());

			IPersistentResponsibleFor persistentResponsibleFor =
				sp.getIPersistentResponsibleFor();
			responsibleFor =
				persistentResponsibleFor.readByTeacherAndExecutionCoursePB(
					teacher,
					executionCourse);

		} catch (Exception e) {
			return false;
		}
		if (responsibleFor == null) {
			return false;
		} else {
			return true;
		}

	}

}
