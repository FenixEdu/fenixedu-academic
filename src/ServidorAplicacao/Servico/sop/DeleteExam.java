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
import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
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

	public Object run(InfoExam infoExam) {

		IExam exam = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExam.getInfoExecutionCourse().getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExam.getInfoExecutionCourse().getSigla(),
					executionPeriod);

			System.out.println("infoexam= " + infoExam);
			System.out.println("executionCourse= " + executionCourse);

			if (executionCourse == null)
				return new Boolean(result);

			exam =
				sp.getIPersistentExam().readBy(
					infoExam.getDay(),
					infoExam.getBeginning(),
					executionCourse);

			if (exam != null) {
				sp.getIPersistentExam().delete(exam);
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return new Boolean(result);
	}

}