/*
 * ATENÇÃO: JÁ NÃO É USADO!!!
 * ApagarTurno.java
 *
 * Created on 27 de Outubro de 2002, 18:24
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço ApagarTurno.
 *
 * @author tfc130
 **/
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ApagarTurno implements IServico {

	private static ApagarTurno _servico = new ApagarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static ApagarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ApagarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ApagarTurno";
	}

	public Boolean run(ShiftKey keyTurno) {

		ITurno turno1 = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(keyTurno.getInfoExecutionCourse());

			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

			turno1 =
				shiftDAO.readByNameAndExecutionCourse(
					keyTurno.getShiftName(),
					executionCourse);
			if (turno1 != null) {
				shiftDAO.delete(turno1);
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return new Boolean(result);
	}

}