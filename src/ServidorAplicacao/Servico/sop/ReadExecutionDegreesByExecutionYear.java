package ServidorAplicacao.Servico.sop;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionDegreesByExecutionYear implements IService
{

    public ReadExecutionDegreesByExecutionYear()
    {
    }

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException
    {

        ArrayList infoExecutionDegreeList = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();

            IExecutionYear executionYear = null;
            if (infoExecutionYear == null)
            {
                IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
                executionYear = persistentExecutionYear.readCurrentExecutionYear();
            }
            else
            {
                executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
            }

            List executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear);

            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList();

            while (iterator.hasNext())
            {
                ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
                infoExecutionDegreeList.add(
                    Cloner.get(executionDegree));
            }

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }

}