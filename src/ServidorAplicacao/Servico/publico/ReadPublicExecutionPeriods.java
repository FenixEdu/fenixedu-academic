/*
 * Created on 18/07/2003
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadPublicExecutionPeriods implements IService
{

    public List run() throws FenixServiceException
    {

        List result = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp
                    .getIPersistentExecutionPeriod();

            List executionPeriods = executionPeriodDAO.readPublic();

            if (executionPeriods != null)
            {
                for (int i = 0; i < executionPeriods.size(); i++)
                {
                    result.add(Cloner.get((IExecutionPeriod) executionPeriods
                            .get(i)));
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
