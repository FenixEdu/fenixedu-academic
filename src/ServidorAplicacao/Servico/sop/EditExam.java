/*
 * CriarAula.java
 *
 * Created on 2003/03/28
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviï¿½o CriarAula.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import java.util.Calendar;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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
		InfoExecutionCourse infoExecutionCourse,
		InfoExam oldExam)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionCourse.getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);

			IExam examFromDBToBeEdited = null;
			boolean newSeasonAlreadyScheduled = false;
			for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
				IExam exam = (IExam) executionCourse.getAssociatedExams().get(i);
				if (exam.getSeason().equals(oldExam.getSeason())) {
					System.out.println("Exontrámos o exam de época: " + season + ".");
					examFromDBToBeEdited = exam;
				} else if (exam.getSeason().equals(season)) {
					System.out.println("Exam de época: " + season + " já existe...");
					newSeasonAlreadyScheduled = true;
				}
			}

			if (newSeasonAlreadyScheduled) {
				throw new ExistingServiceException();
			}

			examFromDBToBeEdited.setBeginning(examTime);
			examFromDBToBeEdited.setDay(examDate.getTime());
			examFromDBToBeEdited.setEnd(null);
			examFromDBToBeEdited.setSeason(season);

			result = new Boolean(true);
		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}