/*
 * ApagarAula.java
 *
 * Created on 27 de Outubro de 2002, 14:30
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o ApagarAula.
 *
 * @author tfc130
 **/
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteExam implements IServico {

	private static DeleteExam _servico = new DeleteExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static DeleteExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private DeleteExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "DeleteExam";
	}

	public Object run(InfoViewExamByDayAndShift infoViewExam) throws FenixServiceException {

		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					((InfoExecutionCourse) infoViewExam.getInfoExecutionCourses().get(0))
					.getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					((InfoExecutionCourse) infoViewExam.getInfoExecutionCourses().get(0))
					.getSigla(),
					executionPeriod);

			if (executionCourse == null)
				return new Boolean(result);

			for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
				IExam exam = (IExam) executionCourse.getAssociatedExams().get(i);
				if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
						sp.getIPersistentExam().delete(exam);
						result = true;					
				}
			}

		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException("Errer deleteing exam");
		}

		return new Boolean(result);
		
	}

}