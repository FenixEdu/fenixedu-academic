/*
 * Created on 4/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionDegreesByDegreeCurricularPlan implements IServico
{

    private static ReadExecutionDegreesByDegreeCurricularPlan service =
        new ReadExecutionDegreesByDegreeCurricularPlan();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionDegreesByDegreeCurricularPlan getService()
    {
        return service;
    }

    /**
	 * The constructor of this class.
	 */
    private ReadExecutionDegreesByDegreeCurricularPlan()
    {
    }

    /**
	 * Service name
	 */
    public final String getNome()
    {
        return "ReadExecutionDegreesByDegreeCurricularPlan";
    }

    /**
	 * Executes the service. Returns the current collection of
	 * infoExecutionDegrees.
	 */
    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException
    {
        ISuportePersistente sp;
        List allExecutionDegrees = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            IDegreeCurricularPlan degreeCurricularPlan =
                (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOID(
                    DegreeCurricularPlan.class, idDegreeCurricularPlan);
            allExecutionDegrees =
                sp.getICursoExecucaoPersistente().readByDegreeCurricularPlan(degreeCurricularPlan);

        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allExecutionDegrees == null || allExecutionDegrees.isEmpty())
            return allExecutionDegrees;

        // build the result of this service
        Iterator iterator = allExecutionDegrees.iterator();
        List result = new ArrayList(allExecutionDegrees.size());

        while (iterator.hasNext())
        {
            ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();

            InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) Cloner.get(executionDegree);

            //added by Tânia Pousão
            if (executionDegree.getCoordinatorsList() != null)
            {
                List infoCoordinatorList = new ArrayList();
                ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList().listIterator();
                while (iteratorCoordinator.hasNext())
                {
                    ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();

                    infoCoordinatorList.add(Cloner.copyICoordinator2InfoCoordenator(coordinator));
                }

                infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
            }

            result.add(infoExecutionDegree);
        }

        return result;
    }
}