package ServidorAplicacao.Servico.gesdis.teacher;
/**
 * @author jmota
 */
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
public class RemoveTeacher implements IServico {
	private static RemoveTeacher service = new RemoveTeacher();
	/**
	 * The singleton access method of this class.
	 **/
	public static RemoveTeacher getService() {
		return service;
	}
	/**
	 * The Actor of this class.
	 **/
	private RemoveTeacher() {
	}
	/**
	 * Returns service name
	 **/
	public final String getNome() {
		return "RemoveTeacher";
	}
	/**
	 * Executes the service.	
	 **/
	public boolean run(
		InfoExecutionCourse infoExecutionCourse,
		Integer teacherNumber)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher =
				persistentTeacher.readTeacherByNumber(teacherNumber);
			
			if (teacher == null) {
				throw new InvalidArgumentsServiceException();
			}
			IDisciplinaExecucao executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					infoExecutionCourse);
			List professorShipsExecutionCoursesList =
				teacher.getProfessorShipsExecutionCourses();
			
			//note: removed the possibility for a responsible teacher to remove from himself the professorship (it was a feature that didnt make sense)
			List responsibleForExecutionCoursesList = teacher.getResponsibleForExecutionCourses();
			if(responsibleForExecutionCoursesList.contains(executionCourse)) {
				throw new notAuthorizedServiceDeleteException();
			}
				
			while (professorShipsExecutionCoursesList.contains(executionCourse)){
			boolean xptoBoolean=professorShipsExecutionCoursesList.remove(executionCourse);
			}
			teacher.setProfessorShipsExecutionCourses(
				professorShipsExecutionCoursesList);
			persistentTeacher.lockWrite(teacher);
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
