/*
 * CriarTurno.java
 *
 * Created on 27 de Outubro de 2002, 18:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurno
 *
 * @author tfc130
 **/
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CriarTurno implements IServico {

	private static CriarTurno _servico = new CriarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static CriarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CriarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CriarTurno";
	}

	public Boolean run(InfoShift infoTurno)
		throws FenixServiceException {

		ITurno turno = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();
			
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoTurno.getInfoDisciplinaExecucao().getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoTurno.getInfoDisciplinaExecucao().getSigla(),
					executionPeriod);

			turno =
				new Turno(
					infoTurno.getNome(),
					infoTurno.getTipo(),
					infoTurno.getLotacao(),
					executionCourse);

			
			try {
				sp.getITurnoPersistente().lockWrite(turno);
			} catch (ExistingPersistentException ex) {
				throw new ExistingServiceException(ex);
			}

			result = true;
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return new Boolean(result);
	}

}