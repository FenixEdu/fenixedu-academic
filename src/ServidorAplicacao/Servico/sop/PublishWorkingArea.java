/*
 * Created on 2003/07/22
 * 
 */
package ServidorAplicacao.Servico.sop;

import DataBeans.InfoExecutionPeriod;
import Dominio.ExecutionPeriod;
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

public class PublishWorkingArea implements IServico {

	private static PublishWorkingArea _servico = new PublishWorkingArea();
	/**
	 * The singleton access method of this class.
	 **/
	public static PublishWorkingArea getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private PublishWorkingArea() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "PublishWorkingArea";
	}

	public Boolean run(InfoExecutionPeriod infoExecutionPeriodOfWorkingArea)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();
			IPersistentExecutionYear executionYearDAO =
				sp.getIPersistentExecutionYear();

			if (infoExecutionPeriodOfWorkingArea.getSemester().intValue()
				>= 0) {
				throw new InvalidWorkingAreaException();
			}

			IExecutionPeriod executionPeriodToExportDataFrom =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriodOfWorkingArea.getSemester(),
					executionYearDAO.readExecutionYearByName(
						infoExecutionPeriodOfWorkingArea
							.getInfoExecutionYear()
							.getYear()));

			if (executionPeriodToExportDataFrom == null) {
				throw new InvalidExecutionPeriod();
			}

			IExecutionPeriod executionPeriodToImportDataTo =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					new Integer(
						-1
							* infoExecutionPeriodOfWorkingArea
								.getSemester()
								.intValue()),
					executionPeriodToExportDataFrom.getExecutionYear());

			if (executionPeriodToImportDataTo == null) {
				// Create coresponding execution period
				executionPeriodToImportDataTo =
					new ExecutionPeriod(
						infoExecutionPeriodOfWorkingArea.getName().substring(3),
						executionPeriodToExportDataFrom.getExecutionYear());
				executionPeriodToImportDataTo.setSemester(
					new Integer(
						-1
							* infoExecutionPeriodOfWorkingArea
								.getSemester()
								.intValue()));
			}

			executionPeriodDAO.writeExecutionPeriod(
				executionPeriodToImportDataTo);

			executionPeriodToImportDataTo.setState(
				new PeriodState(PeriodState.OPEN));				

			// Publish working area
			executionPeriodDAO.transferData(
				executionPeriodToImportDataTo,
				executionPeriodToExportDataFrom,
				new Boolean(true));

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

	/**
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class InvalidWorkingAreaException extends FenixServiceException {

		/**
		 * 
		 */
		private InvalidWorkingAreaException() {
			super();
		}

		/**
		 * @param errorType
		 */
		private InvalidWorkingAreaException(int errorType) {
			super(errorType);
		}

		/**
		 * @param message
		 */
		private InvalidWorkingAreaException(String message) {
			super(message);
		}

		/**
		 * @param message
		 * @param cause
		 */
		private InvalidWorkingAreaException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param cause
		 */
		private InvalidWorkingAreaException(Throwable cause) {
			super(cause);
		}

	}

}