package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 14/Fev/2003
 * 
 * @author jpvl
 */
public class ReadCurrentExecutionPeriod implements IServico
{
    private static ReadCurrentExecutionPeriod service = new ReadCurrentExecutionPeriod();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadCurrentExecutionPeriod getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadCurrentExecutionPeriod";
    }

    public InfoExecutionPeriod run()
    {

        InfoExecutionPeriod infoExecutionPeriod = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new RuntimeException(ex);
        }

        return infoExecutionPeriod;
    }

}
