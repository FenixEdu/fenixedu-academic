package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.List;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class AssociateTeacher implements IServico {

	private static AssociateTeacher service = new AssociateTeacher();

	/**
	 * The singleton access method of this class.
	 **/

	public static AssociateTeacher getService() {

		return service;

	}

	/**
	 * The Actor of this class.
	 **/

	private AssociateTeacher() {
	}

	/**
	 * returns the service name
	 **/

	public final String getNome() {

		return "AssociateTeacher";

	}

	/**
	 * Executes the service.
	 *
	 **/

	public boolean run(
		InfoExecutionCourse infoExecutionCourse,
		Integer teacherNumber)
		throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = null;
			IDisciplinaExecucao executionCourse = null;
			teacher = persistentTeacher.readTeacherByNumber(teacherNumber);

			executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					infoExecutionCourse);
			System.out.println("Teacher: " + teacher.getTeacherNumber());
			System.out.println("Teacher: " + teacher);
			if (teacher == null) {
				System.out.println("O Teacher está a null: ");
				
				throw new InvalidArgumentsServiceException();
			} else {

				List professorShipsExecutionCoursesList =
					teacher.getProfessorShipsExecutionCourses();

				if (professorShipsExecutionCoursesList
					.contains(executionCourse))
					throw new ExistingServiceException();

				professorShipsExecutionCoursesList.add(executionCourse);

				teacher.setProfessorShipsExecutionCourses(
					professorShipsExecutionCoursesList);

				persistentTeacher.lockWrite(teacher);
				
				return true;
			}

		} catch (ExcepcaoPersistencia ex) {
			
			throw new ExistingServiceException(ex);

		}

	}

}
