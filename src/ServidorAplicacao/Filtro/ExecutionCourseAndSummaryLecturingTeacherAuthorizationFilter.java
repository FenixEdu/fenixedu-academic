/*
 * Created on 19/Mai/2003
 *
 * 
 */
package ServidorAplicacao.Filtro;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSummary;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.Summary;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *
 */
public class ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter
	extends AuthorizationByRoleFilter {

	public final static ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter instance =
		new ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter();

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
		Object[] argumentos)
		throws Exception {
		try {
			if ((id == null)
				|| (id.getRoles() == null)
				|| !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
				|| !lecturesExecutionCourse(id, argumentos)
				|| !SummaryBelongsExecutionCourse(id, argumentos)) {
				throw new NotAuthorizedException();
			}
		}
		catch (RuntimeException ex) {
			throw new NotAuthorizedException(ex.getMessage());
		}
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean SummaryBelongsExecutionCourse(
		IUserView id,
		Object[] argumentos) {
		InfoExecutionCourse infoExecutionCourse = null;
		IExecutionCourse executionCourse = null;
		ISuportePersistente sp;
		ISummary summary = null;
		InfoSummary infoSummary = null;

		if (argumentos == null) {
			return false;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIPersistentExecutionCourse();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
			}
			else {
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(
						new ExecutionCourse((Integer) argumentos[0]),
						false);
			}
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			if (argumentos[1] instanceof InfoSummary) {
				infoSummary = (InfoSummary) argumentos[1];
				summary = Cloner.copyInfoSummary2ISummary(infoSummary);
				summary =
					(ISummary) persistentSummary.readByOId(summary, false);
			}
			else {
				summary =
					(ISummary) persistentSummary.readByOId(
						new Summary((Integer) argumentos[1]),
						false);

			}
		}
		catch (Exception e) {
			return false;
		}
		return summary.getExecutionCourse().equals(executionCourse);
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
				sp.getIPersistentExecutionCourse();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
			}
			else {
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(
						new ExecutionCourse((Integer) argumentos[0]),
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
		}
		catch (Exception e) {
			return false;
		}
		return professorship != null;
	}

}
