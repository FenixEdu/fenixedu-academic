/*
 * Created on 2003/07/17
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Luis Crus & Sara Ribeiro
 */

public class CreateExecutionPeriod implements IService
{

    /**
     * The actor of this class.
     */
    public CreateExecutionPeriod()
    {
    }

	//SERVICO PARA SER REMOVIDO
    public Boolean run(InfoExecutionPeriod infoExecutionPeriodOfWorkingArea,
            InfoExecutionPeriod infoExecutionPeriodToExportDataFrom) throws FenixServiceException
    {
/*
        Boolean result = new Boolean(false);

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            IExecutionPeriod executionPeriodToExportDataFrom = executionPeriodDAO
                    .readBySemesterAndExecutionYear(infoExecutionPeriodToExportDataFrom.getSemester(),
                            executionYearDAO.readExecutionYearByName(infoExecutionPeriodToExportDataFrom
                                    .getInfoExecutionYear().getYear()));

            if (executionPeriodToExportDataFrom == null) { throw new InvalidExecutionPeriod(); }

            IExecutionYear executionYearToCreate = executionYearDAO
                    .readExecutionYearByName(infoExecutionPeriodOfWorkingArea.getInfoExecutionYear()
                            .getYear());

            if (executionYearToCreate == null)
            {
                // Create coresponding execution year
                executionYearToCreate = new ExecutionYear(infoExecutionPeriodOfWorkingArea
                        .getInfoExecutionYear().getYear());
                executionYearDAO.simpleLockWrite(executionYearToCreate);
                executionYearToCreate.setState(new PeriodState(PeriodState.NOT_OPEN));
                executionYearToCreate.setBeginDate(Calendar.getInstance().getTime());
                executionYearToCreate.setEndDate(Calendar.getInstance().getTime());

            }

            IExecutionPeriod executionPeriodToCreate = executionPeriodDAO
                    .readBySemesterAndExecutionYear(infoExecutionPeriodOfWorkingArea.getSemester(),
                            executionYearToCreate);

            if (executionPeriodToCreate == null)
            {
                // Create coresponding execution period
                executionPeriodToCreate = new ExecutionPeriod(
                        infoExecutionPeriodOfWorkingArea.getName(), executionYearToCreate);
                executionPeriodDAO.simpleLockWrite(executionPeriodToCreate);
                executionPeriodToCreate.setSemester(infoExecutionPeriodOfWorkingArea.getSemester());
                executionPeriodToCreate.setState(new PeriodState(PeriodState.NOT_OPEN));
                executionPeriodToCreate.setBeginDate(Calendar.getInstance().getTime());
                executionPeriodToCreate.setEndDate(Calendar.getInstance().getTime());

            }
            else
            {
                throw new ExistingExecutionPeriod();
            }

            sp.confirmarTransaccao();
            sp.iniciarTransaccao();

            // Export data to new execution period
            executionPeriodDAO.transferData(executionPeriodToCreate, executionPeriodToExportDataFrom);

            result = new Boolean(true);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex.getMessage());
        }

        return result;*/
        return null;
    }

    public class InvalidExecutionPeriod extends FenixServiceException
    {

        /**
         * 
         */
        InvalidExecutionPeriod()
        {
            super();
        }

    }

    public class ExistingExecutionPeriod extends FenixServiceException
    {

        /**
         * 
         */
        ExistingExecutionPeriod()
        {
            super();
        }

    }

}