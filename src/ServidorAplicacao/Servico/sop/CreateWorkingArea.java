/*
 * Created on 2003/07/17
 * 
 */
package ServidorAplicacao.Servico.sop;

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
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;
/**
 * @author Luis Crus & Sara Ribeiro
 */

public class CreateWorkingArea implements IServico {

	private static CreateWorkingArea _servico = new CreateWorkingArea();
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateWorkingArea getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateWorkingArea() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CreateWorkingArea";
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

			IExecutionYear executionYearOfWorkingArea =
				executionYearDAO.readExecutionYearByName(
					infoExecutionPeriodOfWorkingArea
						.getInfoExecutionYear()
						.getYear());

			if (executionYearOfWorkingArea == null) {
				// Create coresponding execution year
				executionYearOfWorkingArea =
					new ExecutionYear(
						infoExecutionPeriodOfWorkingArea
							.getInfoExecutionYear()
							.getYear());
				executionYearOfWorkingArea.setState(
					new PeriodState(PeriodState.NOT_OPEN));

				executionYearDAO.writeExecutionYear(executionYearOfWorkingArea);
			}

			IExecutionPeriod executionPeriodOfWorkingArea =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriodOfWorkingArea.getSemester(),
					executionYearOfWorkingArea);

			if (executionPeriodOfWorkingArea == null) {
				// Create coresponding execution period
				executionPeriodOfWorkingArea =
					new ExecutionPeriod(
						infoExecutionPeriodOfWorkingArea.getName(),
						executionYearOfWorkingArea);
				executionPeriodOfWorkingArea.setSemester(
					infoExecutionPeriodOfWorkingArea.getSemester());
				executionPeriodOfWorkingArea.setState(
					new PeriodState(PeriodState.OPEN));

				executionPeriodDAO.writeExecutionPeriod(
					executionPeriodOfWorkingArea);
			}

			// Create working area
			IExecutionPeriod workingArea =
				new ExecutionPeriod(
					"AT:" + executionPeriodOfWorkingArea.getName(),
					executionPeriodOfWorkingArea.getExecutionYear());
			workingArea.setSemester(
				new Integer(
					-1
						* executionPeriodOfWorkingArea.getSemester().intValue()));
			workingArea.setState(new PeriodState(PeriodState.OPEN));

			try {
				executionPeriodDAO.writeExecutionPeriod(workingArea);
			} catch (ExistingPersistentException ex) {
				throw new ExistingExecutionPeriod();
			}

			// Export data to working area
			executionPeriodDAO.transferData(
				workingArea,
				executionPeriodToExportDataFrom,
				new Boolean(false));

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
		private InvalidExecutionPeriod() {
			super();
		}

		/**
		 * @param errorType
		 */
		private InvalidExecutionPeriod(int errorType) {
			super(errorType);
		}

		/**
		 * @param s
		 */
		private InvalidExecutionPeriod(String s) {
			super(s);
		}

		/**
		 * @param cause
		 */
		private InvalidExecutionPeriod(Throwable cause) {
			super(cause);
		}

		/**
		 * @param message
		 * @param cause
		 */
		private InvalidExecutionPeriod(String message, Throwable cause) {
			super(message, cause);
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
		private ExistingExecutionPeriod() {
			super();
		}

		/**
		 * @param errorType
		 */
		private ExistingExecutionPeriod(int errorType) {
			super(errorType);
		}

		/**
		 * @param message
		 */
		private ExistingExecutionPeriod(String message) {
			super(message);
		}

		/**
		 * @param message
		 * @param cause
		 */
		private ExistingExecutionPeriod(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param cause
		 */
		private ExistingExecutionPeriod(Throwable cause) {
			super(cause);
		}

	}

}