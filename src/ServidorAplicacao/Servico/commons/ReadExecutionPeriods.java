/*
 * Created on 28/04/2003
 *  
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ReadExecutionPeriods implements IServico
{

    private static ReadExecutionPeriods service = new ReadExecutionPeriods();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionPeriods getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionPeriods";
    }

    public List run() throws FenixServiceException
    {

        List result = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

            List executionPeriods = executionPeriodDAO.readByCriteria(new ExecutionPeriod());

            if (executionPeriods != null)
            {
                for (int i = 0; i < executionPeriods.size(); i++)
                {
                    result.add(
                        Cloner.get(
                            (IExecutionPeriod) executionPeriods.get(i)));
                }
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}
