/*
 * CreateExam.java
 *
 * Created on 2003/03/26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarAula.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import java.util.ArrayList;
import java.util.Calendar;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.ExamExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Season;

public class CreateExam implements IServico {

	private static CreateExam _servico = new CreateExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CreateExam";
	}

	public Boolean run(
		Calendar examDate,
		Calendar examTime,
		Season season,
		InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionCourse.getInfoExecutionPeriod());

			IExecutionCourse executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);

			for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
				IExam exam = (IExam) executionCourse.getAssociatedExams().get(i);
				if (exam.getSeason().equals(season)) {
					throw new ExistingServiceException();
				}
			}

			IExam exam = new Exam(examDate, examTime, null, season);
			exam.setAssociatedRooms(new ArrayList());
			IExamExecutionCourse examExecutionCourse = new ExamExecutionCourse(exam, executionCourse);

			try {
				sp.getIPersistentExam().lockWrite(exam);
				sp.getIPersistentExamExecutionCourse().lockWrite(examExecutionCourse);
			} catch (ExistingPersistentException ex) {
				throw new ExistingServiceException(ex);
			}


			result = new Boolean(true);
		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}