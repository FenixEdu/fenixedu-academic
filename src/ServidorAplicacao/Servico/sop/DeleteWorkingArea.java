/*
 * Created on 2003/07/18
 * 
 */
package ServidorAplicacao.Servico.sop;

import DataBeans.InfoExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 * @author Luis Crus & Sara Ribeiro
 */

public class DeleteWorkingArea implements IServico {

	private static DeleteWorkingArea _servico = new DeleteWorkingArea();
	/**
	 * The singleton access method of this class.
	 **/
	public static DeleteWorkingArea getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private DeleteWorkingArea() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "DeleteWorkingArea";
	}

	public Boolean run(InfoExecutionPeriod infoExecutionPeriodToDelete)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();
			IPersistentExecutionYear executionYearDAO =
				sp.getIPersistentExecutionYear();

			IExecutionPeriod executionPeriodToDelete =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriodToDelete.getSemester(),
					executionYearDAO.readExecutionYearByName(
						infoExecutionPeriodToDelete
							.getInfoExecutionYear()
							.getYear()));

			if (executionPeriodToDelete != null) {
				executionPeriodDAO.deleteWorkingArea(executionPeriodToDelete);
				result = new Boolean(true);
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