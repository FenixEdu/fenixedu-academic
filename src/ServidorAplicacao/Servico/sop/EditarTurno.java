/*
 * EditarTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:00
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurno.
 *
 * @author tfc130
 **/
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class EditarTurno implements IServico {

	private static EditarTurno _servico = new EditarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditarTurno";
	}

	public Object run(InfoShift shiftToEdit, InfoShift turnoNova)
		throws FenixServiceException {

		ITurno turno = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Copia infoExecutionCourse para DisciplinaExecucao
			IDisciplinaExecucao executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					shiftToEdit.getInfoDisciplinaExecucao());
			// fim da cópia 

			turno =
				sp.getITurnoPersistente().readByNameAndExecutionCourse(
					shiftToEdit.getNome(),
					executionCourse);
			if (turno != null) {
				turno.setNome(turnoNova.getNome());
				turno.setTipo(turnoNova.getTipo());
				turno.setLotacao(turnoNova.getLotacao());
				try {
					sp.getITurnoPersistente().lockWrite(turno);
				} catch (ExistingPersistentException ex) {
					throw new ExistingServiceException(ex);

				}
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return new Boolean(result);
	}

}