/*
 * Created on 2003/07/25
 * 
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;
/**
 * @author Luis Crus & Sara Ribeiro
 */

public class AlterExecutionPeriodState implements IServico {

	private static AlterExecutionPeriodState _servico =
		new AlterExecutionPeriodState();
	/**
	 * The singleton access method of this class.
	 **/
	public static AlterExecutionPeriodState getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private AlterExecutionPeriodState() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "AlterExecutionPeriodState";
	}

	public Boolean run(
		InfoExecutionPeriod infoExecutionPeriod,
		PeriodState periodState)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();
			IPersistentExecutionYear executionYearDAO =
				sp.getIPersistentExecutionYear();

			IExecutionPeriod executionPeriod =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriod.getSemester(),
					executionYearDAO.readExecutionYearByName(
						infoExecutionPeriod.getInfoExecutionYear().getYear()));

			if (executionPeriod == null) {
				throw new InvalidExecutionPeriod();
			} else {
				System.out.println("periodState.getStateCode()= " + periodState.getStateCode());
				if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
					// Deactivate the current
					IExecutionPeriod currentExecutionPeriod =
						executionPeriodDAO.readActualExecutionPeriod();
					System.out.println("Actual = " + currentExecutionPeriod);
					executionPeriodDAO.lockWrite(currentExecutionPeriod);
					currentExecutionPeriod.setState(new PeriodState(PeriodState.OPEN));
					executionPeriodDAO.lockWrite(currentExecutionPeriod);					 
				}
				
				executionPeriodDAO.lockWrite(executionPeriod);
				executionPeriod.setState(periodState);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

	/**
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class InvalidExecutionPeriod extends FenixServiceException {

		InvalidExecutionPeriod() {
			super();
		}

	}

}