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
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

	public Object run(
		InfoShift shiftToEdit,
		InfoShift turnoNova) {

		ITurno turno = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Copia infoExecutionCourse para DisciplinaExecucao
			IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(shiftToEdit.getInfoDisciplinaExecucao());
			// fim da cópia 
						
			turno =
				sp.getITurnoPersistente().readByNameAndExecutionCourse(
					shiftToEdit.getNome(),
					executionCourse);
			if (turno != null) {
				turno.setNome(turnoNova.getNome());
				turno.setTipo(turno.getTipo());
				turno.setLotacao(turnoNova.getLotacao());
				sp.getITurnoPersistente().lockWrite(turno);
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
		}

		return new Boolean(result);
	}

}