/*
 * Created on 23/Abr/2003
 * 
 *  
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionPeriod;
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
 * @author João Mota
 * 
 *  
 */
public class ReadExecutionPeriodByOID implements IServico
{

    private static ReadExecutionPeriodByOID service = new ReadExecutionPeriodByOID();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionPeriodByOID getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionPeriodByOID";
    }

    public InfoExecutionPeriod run(Integer oid) throws FenixServiceException
    {

        InfoExecutionPeriod result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod =
                (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class, oid);
            if (executionPeriod != null)
            {
                result = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod);
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}
