/*
 * Created on 11/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionCourse implements IServico
{

    private static ReadExecutionCourse service = new ReadExecutionCourse();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionCourse getService()
    {
        return service;
    }

    /**
	 * The constructor of this class.
	 */
    private ReadExecutionCourse()
    {
    }

    /**
	 * Service name
	 */
    public final String getNome()
    {
        return "ReadExecutionCourse";
    }

    /**
	 * Executes the service. Returns the current InfoExecutionCourse.
	 */
    public InfoExecutionCourse run(Integer idInternal) throws FenixServiceException
    {

        IExecutionCourse executionCourse = null;
        InfoExecutionCourse infoExecutionCourse = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            executionCourse =
                (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOId(
                    new ExecutionCourse(idInternal),
                    false);

            if (executionCourse == null)
            {
                throw new NonExistingServiceException();
            }

            infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoExecutionCourse;
    }
}