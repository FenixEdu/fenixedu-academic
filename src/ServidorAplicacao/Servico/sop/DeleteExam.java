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
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.IExam;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
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

	public Object run(InfoViewExamByDayAndShift infoViewExam)
		throws FenixServiceException {

		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExam examToDelete =
				(IExam) sp.getIPersistentExam().readByOId(
					Cloner.copyInfoExam2IExam(
						infoViewExam.getInfoExam()), false);

			sp.getIPersistentExam().delete(examToDelete);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException("Errer deleteing exam");
		}

		return new Boolean(result);

	}

}