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

			System.out.println(
				"infoExecutionPeriodToExportDataFrom.getName():"
					+ infoExecutionPeriodToExportDataFrom.getName());
			System.out.println(
				"infoExecutionPeriodToExportDataFrom.getYear():"
					+ infoExecutionPeriodToExportDataFrom
						.getInfoExecutionYear()
						.getYear());

			IExecutionPeriod executionPeriodToExportDataFrom =
				executionPeriodDAO.readBySemesterAndExecutionYear(
					infoExecutionPeriodToExportDataFrom.getSemester(),
					executionYearDAO.readExecutionYearByName(
						infoExecutionPeriodToExportDataFrom
							.getInfoExecutionYear()
							.getYear()));

			if (executionPeriodToExportDataFrom == null) {
				// error - invalid execution period to export data from.
				System.out.println(
					"Failled reading executionPeriodToExportDataFrom!");
				return result;
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
					new PeriodState(PeriodState.OPEN));

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

			// first make sure it doesn't exist.
			executionPeriodDAO.writeExecutionPeriod(workingArea);
			System.out.println("Created working area.");

			result = new Boolean(true);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}