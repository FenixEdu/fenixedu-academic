package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import Dominio.IExecutionDegree;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */

/*
 * Given the id of a  degreeCurricularPlan, this service returns
 * its executionDegree specified in executionDegreeIndex
 */

public class ReadExecutionDegreeByDegreeCurricularPlanID implements IService {

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionDegreeIndex)
            throws ExcepcaoPersistencia {
        List infoExecutionDegreeList = null;
        ISuportePersistente sp;
        List executionDegrees = null;

        sp = SuportePersistenteOJB.getInstance();
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        executionDegrees = sp.getIPersistentExecutionDegree().readByDegreeCurricularPlan(
                degreeCurricularPlan);

        infoExecutionDegreeList = new ArrayList();

        for (Iterator iter = executionDegrees.iterator(); iter.hasNext();) {
            IExecutionDegree executionDegree = (IExecutionDegree) iter.next();

            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree);
            infoExecutionDegreeList.add(infoExecutionDegree);
        }

        Collections.sort(infoExecutionDegreeList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((InfoExecutionDegree) o1).getInfoExecutionYear().getBeginDate().compareTo(
                        ((InfoExecutionDegree) o1).getInfoExecutionYear().getBeginDate());
            }
        });

        return ((InfoExecutionDegree) infoExecutionDegreeList.get(executionDegreeIndex.intValue() - 1));
    }
}