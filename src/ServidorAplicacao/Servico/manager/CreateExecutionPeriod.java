/*
 * Created on 2003/07/17
 * 
 */
package ServidorAplicacao.Servico.manager;

import java.util.Calendar;

import DataBeans.InfoExecutionPeriod;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
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

public class CreateExecutionPeriod implements IServico {

	private static CreateExecutionPeriod _servico = new CreateExecutionPeriod();
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateExecutionPeriod getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateExecutionPeriod() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CreateExecutionPeriod";
	}

	public Boolean run(
		InfoExecutionPeriod infoExecutionPeriodOfWorkingArea,
		InfoExecutionPeriod infoExecutionPeriodToExportDataFrom)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();
			IPersistentExecutionYear executionYearDAO =
				sp.getIPersistentExecutionYear();

			IExecutionPeriod executionPeriodToExportDataFrom =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriodToExportDataFrom.getSemester(),
					executionYearDAO.readExecutionYearByName(
						infoExecutionPeriodToExportDataFrom
							.getInfoExecutionYear()
							.getYear()));

			if (executionPeriodToExportDataFrom == null) {
				throw new InvalidExecutionPeriod();
			}

			IExecutionYear executionYearToCreate =
				executionYearDAO.readExecutionYearByName(
					infoExecutionPeriodOfWorkingArea
						.getInfoExecutionYear()
						.getYear());

			if (executionYearToCreate == null) {
				// Create coresponding execution year
				executionYearToCreate =
					new ExecutionYear(
						infoExecutionPeriodOfWorkingArea
							.getInfoExecutionYear()
							.getYear());
				executionYearToCreate.setState(
					new PeriodState(PeriodState.NOT_OPEN));
				executionYearToCreate.setBeginDate(Calendar.getInstance().getTime());
				executionYearToCreate.setEndDate(Calendar.getInstance().getTime());

				executionYearDAO.writeExecutionYear(executionYearToCreate);
			}

			IExecutionPeriod executionPeriodToCreate =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriodOfWorkingArea.getSemester(),
					executionYearToCreate);

			if (executionPeriodToCreate == null) {
				// Create coresponding execution period
				executionPeriodToCreate =
					new ExecutionPeriod(
						infoExecutionPeriodOfWorkingArea.getName(),
						executionYearToCreate);
				executionPeriodToCreate.setSemester(
					infoExecutionPeriodOfWorkingArea.getSemester());
				executionPeriodToCreate.setState(
					new PeriodState(PeriodState.NOT_OPEN));
				executionPeriodToCreate.setBeginDate(Calendar.getInstance().getTime());
				executionPeriodToCreate.setEndDate(Calendar.getInstance().getTime());

				executionPeriodDAO.writeExecutionPeriod(
					executionPeriodToCreate);
			} else {
				throw new ExistingExecutionPeriod();
			}

			sp.confirmarTransaccao();
			sp.iniciarTransaccao();
			
			// Export data to new execution period
			executionPeriodDAO.transferData(
				executionPeriodToCreate,
				executionPeriodToExportDataFrom);

			result = new Boolean(true);
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

		/**
		 * 
		 */
		InvalidExecutionPeriod() {
			super();
		}

	}

	/**
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class ExistingExecutionPeriod extends FenixServiceException {

		/**
		 * 
		 */
		ExistingExecutionPeriod() {
			super();
		}

	}

}