/*
 * CreateExam.java
 *
 * Created on 2003/03/28
 */

package ServidorAplicacao.Servico.sop;

/**
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingRoomsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Season;

public class EditExam implements IServico {

	private static EditExam _servico = new EditExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditExam";
	}

	public Boolean run(
		Calendar examDate,
		Calendar examTime,
		Season season,
		InfoViewExamByDayAndShift infoViewOldExam)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO =
				sp.getIPersistentExecutionCourse();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					((InfoExecutionCourse) infoViewOldExam
						.getInfoExecutionCourses()
						.get(0))
						.getInfoExecutionPeriod());

			IExecutionCourse executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					((InfoExecutionCourse) infoViewOldExam
						.getInfoExecutionCourses()
						.get(0))
						.getSigla(),
					executionPeriod);

			IExam examFromDBToBeEdited = null;
			boolean newSeasonAlreadyScheduled = false;
			for (int i = 0;
				i < executionCourse.getAssociatedExams().size();
				i++) {
				IExam exam =
					(IExam) executionCourse.getAssociatedExams().get(i);
				if (exam
					.getSeason()
					.equals(infoViewOldExam.getInfoExam().getSeason())) {
					examFromDBToBeEdited = exam;
				} else if (exam.getSeason().equals(season)) {
					newSeasonAlreadyScheduled = true;
				}
			}

			if (newSeasonAlreadyScheduled) {
				throw new ExistingServiceException();
			}

			if (hasValidRooms(examFromDBToBeEdited, examDate, examTime)) {
				// TODO: Temporary solution to lock object for write. In the future we'll use readByUnique()				
				examFromDBToBeEdited = (IExam) sp.getIPersistentExam().readByOId(examFromDBToBeEdited,true);
				examFromDBToBeEdited.setBeginning(examTime);
				examFromDBToBeEdited.setDay(examDate);
				examFromDBToBeEdited.setEnd(null);
				examFromDBToBeEdited.setSeason(season);

				result = new Boolean(true);
			} else {
				throw new InterceptingRoomsServiceException();
			}

		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

	private boolean hasValidRooms(
		IExam exam,
		Calendar examDate,
		Calendar examTime)
		throws FenixServiceException {

		ISuportePersistente sp;
		try {
			sp = SuportePersistenteOJB.getInstance();
			ISalaPersistente persistentRoom = sp.getISalaPersistente();
			IExam examQuery = new Exam(examDate, examTime, null, null);
			examQuery.setIdInternal(exam.getIdInternal());
			List availableRooms = persistentRoom.readAvailableRooms(examQuery);

			if (availableRooms.containsAll(exam.getAssociatedRooms())) {
				return true;
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e.getMessage());
		}
		return false;
	}

}