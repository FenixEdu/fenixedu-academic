/*
 * Created on 2003/07/25
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import Dominio.IExecutionPeriod;
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

public class AlterExecutionPeriodState implements IService {

	

	/**
	 * The actor of this class.
	 **/
	public AlterExecutionPeriodState() {
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
				
				if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
					// Deactivate the current
					IExecutionPeriod currentExecutionPeriod =
						executionPeriodDAO.readActualExecutionPeriod();
					
					executionPeriodDAO.simpleLockWrite(currentExecutionPeriod);
					currentExecutionPeriod.setState(new PeriodState(PeriodState.OPEN));
									 
				}
				
				executionPeriodDAO.simpleLockWrite(executionPeriod);
				executionPeriod.setState(periodState);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

	
	public class InvalidExecutionPeriod extends FenixServiceException {

		InvalidExecutionPeriod() {
			super();
		}

	}

}