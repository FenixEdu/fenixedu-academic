package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Fernanda Quitério
 * 14/Jan/2004
 *
 */
public class ReadNotClosedExecutionYears implements IServico
{

    private static ReadNotClosedExecutionYears service = new ReadNotClosedExecutionYears();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadNotClosedExecutionYears getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadNotClosedExecutionYears";
    }

    public List run() throws FenixServiceException
    {

        List result = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            List executionYears = persistentExecutionYear.readNotClosedExecutionYears();

            if (executionYears != null)
            {
                for (int i = 0; i < executionYears.size(); i++)
                {
                    result.add(
                        Cloner.get(
                            (IExecutionYear) executionYears.get(i)));
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