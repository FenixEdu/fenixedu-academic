/*
 * AdicionarTurno.java
 *
 * Created on 27 de Outubro de 2002, 12:31
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço AdicionarTurno.
 *
 * @author tfc130
 **/
import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class AdicionarTurno implements IServico {

	private static AdicionarTurno _servico = new AdicionarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static AdicionarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private AdicionarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "AdicionarTurno";
	}

	public Boolean run(InfoClass infoClass, InfoShift infoShift)
		throws FenixServiceException {

		ITurmaTurno turmaTurno = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoClass.getInfoExecutionPeriod());
			ICursoExecucao executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					infoClass.getInfoExecutionDegree());
			IExecutionCourse executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					infoShift.getInfoDisciplinaExecucao());

			ITurma group =
				sp
					.getITurmaPersistente()
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					infoClass.getNome(),
					executionDegree,
					executionPeriod);
			ITurno shift =
				sp.getITurnoPersistente().readByNameAndExecutionCourse(
					infoShift.getNome(),
					executionCourse);

			turmaTurno = new TurmaTurno(group, shift);

			try {
				sp.getITurmaTurnoPersistente().lockWrite(turmaTurno);
			} catch (ExistingPersistentException e) {
				throw new ExistingServiceException(e);
			}

			result = true;
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return new Boolean(result);
	}

}