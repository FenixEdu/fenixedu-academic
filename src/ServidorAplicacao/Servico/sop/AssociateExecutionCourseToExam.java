/*
 * CriarAula.java
 *
 * Created on 2003/03/30
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarAula.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
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

public class AssociateExecutionCourseToExam implements IServico {

	private static AssociateExecutionCourseToExam _servico = new AssociateExecutionCourseToExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static AssociateExecutionCourseToExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private AssociateExecutionCourseToExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "AssociateExecutionCourseToExam";
	}

	public Boolean run(InfoViewExamByDayAndShift infoViewExam, InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO =
				sp.getIPersistentExecutionCourse();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionCourse.getInfoExecutionPeriod());

			IExecutionCourse executionCourseToBeAssociatedWithExam =
				executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);

			// We assume it's the same execution period.
			IExecutionCourse someExecutionCourseAlreadyAssociatedWithExam =
				executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriod(
					((InfoExecutionCourse) infoViewExam.getInfoExecutionCourses().get(0)).getSigla(),
					executionPeriod);

			// Obtain a mapped exam
			IExam examFromDBToBeAssociated = null;
			for (int i = 0; i < someExecutionCourseAlreadyAssociatedWithExam.getAssociatedExams().size(); i++) {
				IExam exam = (IExam) someExecutionCourseAlreadyAssociatedWithExam.getAssociatedExams().get(i);
				if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
					examFromDBToBeAssociated = exam;
				}
			}

			// Check that the execution course which will be associated with the exam
			// doesn't already have an exam scheduled for the corresponding season
			for (int i = 0; i < executionCourseToBeAssociatedWithExam.getAssociatedExams().size(); i++) {
				IExam exam = (IExam) executionCourseToBeAssociatedWithExam.getAssociatedExams().get(i);
				if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
					throw new ExistingServiceException();
				}
			}

			IExamExecutionCourse examExecutionCourse =
				new ExamExecutionCourse(
					examFromDBToBeAssociated,
					executionCourseToBeAssociatedWithExam);

			try {
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