/*
 * Created on 10/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionPeriod implements IServico
{

    private static ReadExecutionPeriod service = new ReadExecutionPeriod();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionPeriod getService()
    {
        return service;
    }

    /**
	 * The constructor of this class.
	 */
    private ReadExecutionPeriod()
    {
    }

    /**
	 * Service name
	 */
    public final String getNome()
    {
        return "ReadExecutionPeriodForExecutionCourseAssociation";
    }

    /**
	 * Executes the service. Returns the current infoExecutionPeriod.
	 */
    public InfoExecutionPeriod run(Integer executionPeriodId) throws FenixServiceException
    {
        ISuportePersistente sp;
        InfoExecutionPeriod infoExecutionPeriod = null;
        IExecutionPeriod executionPeriod = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            ExecutionPeriod executionPeriodToRead = new ExecutionPeriod();
            executionPeriodToRead.setIdInternal(executionPeriodId);

            executionPeriod =
                (IExecutionPeriod) sp.getIPersistentExecutionPeriod().readByOId(
                    executionPeriodToRead,
                    false);

        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (executionPeriod == null)
        {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }

        infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod);
        return infoExecutionPeriod;
    }
}
