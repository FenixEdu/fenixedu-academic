/*
 * Created on Nov 12, 2003
 *  
 */
package ServidorAplicacao.Filtro;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.Exam;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class ExecutionCourseAndExamLecturingTeacherAuthorizationFilter
	extends AuthorizationByRoleFilter {

	public final static ExecutionCourseAndExamLecturingTeacherAuthorizationFilter instance =
		new ExecutionCourseAndExamLecturingTeacherAuthorizationFilter();

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
		try {
			if ((id == null)
				|| (id.getRoles() == null)
				|| !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
				|| !lecturesExecutionCourse(id, argumentos)
				|| !examBelongsExecutionCourse(id, argumentos)) {
				throw new NotAuthorizedException();
			}
		} catch (RuntimeException e) {
			throw new NotAuthorizedException();
		}
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
		IDisciplinaExecucao executionCourse = null;
		ISuportePersistente sp;
		IProfessorship professorship = null;
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

	private boolean examBelongsExecutionCourse(
		IUserView id,
		Object[] argumentos) {

		ISuportePersistente sp = null;
		InfoExecutionCourse infoExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;
		InfoExam infoExam = null;
		IExam exam = null;

		if (argumentos == null) {
			return false;
		}
		try {
			sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentExam persistentExam = sp.getIPersistentExam();

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

			if (argumentos[1] instanceof InfoExam) {
				infoExam = (InfoExam) argumentos[1];
				exam = Cloner.copyInfoExam2IExam(infoExam);
			} else {
				exam = (IExam) persistentExam.readByOId(new Exam((Integer) argumentos[1]), false);
			}
			
			if(executionCourse != null && exam!= null)
				return executionCourse.getAssociatedExams().contains(exam);
			else 
				return false;

		} catch (Exception e) {
			return false;
		}
	}

}
