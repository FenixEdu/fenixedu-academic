/*
 * Created on Feb 20, 2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 * @return List containing all InfoExecutionDegrees, corresponding to Degree Curricular Plan
 */
public class ReadExecutionDegreesByDegreeCurricularPlanID implements IService
{
    public ReadExecutionDegreesByDegreeCurricularPlanID()
    {

    }

    public List run(Integer degreeCurricularPlanID) throws FenixServiceException
    {
        List infoExecutionDegreeList = null;
        try
        {
            ISuportePersistente sp;
            infoExecutionDegreeList = null;
            List executionDegrees = null;

            sp = SuportePersistenteOJB.getInstance();
            IDegreeCurricularPlan degreeCurricularPlan =
                (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOID(
                    DegreeCurricularPlan.class, degreeCurricularPlanID);

            executionDegrees =
                sp.getICursoExecucaoPersistente().readByDegreeCurricularPlan(degreeCurricularPlan);

            infoExecutionDegreeList = new ArrayList();

            for (Iterator iter = executionDegrees.iterator(); iter.hasNext();)
            {
                ICursoExecucao executionDegree = (ICursoExecucao) iter.next();
                InfoExecutionDegree infoExecutionDegree =
                    (InfoExecutionDegree) Cloner.get(executionDegree);
                infoExecutionDegreeList.add(infoExecutionDegree);
            }

            return infoExecutionDegreeList;

        }
        catch (ExcepcaoPersistencia e)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }

}
