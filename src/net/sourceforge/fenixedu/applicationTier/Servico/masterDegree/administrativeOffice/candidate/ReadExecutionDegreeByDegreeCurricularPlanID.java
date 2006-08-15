package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */

/*
 * Given the id of a degreeCurricularPlan, this service returns its
 * executionDegree persistentSupportecified in executionDegreeIndex
 */

public class ReadExecutionDegreeByDegreeCurricularPlanID extends Service {

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionDegreeIndex)
            throws ExcepcaoPersistencia {
        List infoExecutionDegreeList = null;
        List executionDegrees = null;

        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(
                        degreeCurricularPlanID);

        executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        infoExecutionDegreeList = new ArrayList();

        for (Iterator iter = executionDegrees.iterator(); iter.hasNext();) {
            ExecutionDegree executionDegree = (ExecutionDegree) iter.next();

            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);
            infoExecutionDegreeList.add(infoExecutionDegree);
        }

        Collections.sort(infoExecutionDegreeList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((InfoExecutionDegree) o2).getInfoExecutionYear().getBeginDate().compareTo(
                        ((InfoExecutionDegree) o1).getInfoExecutionYear().getBeginDate());
            }
        });

        return ((InfoExecutionDegree) infoExecutionDegreeList.get(executionDegreeIndex.intValue() - 1));
    }

    /**
     * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali</a>
     * 
     * @param degreeCurricularPlanID
     * @param executionDegree
     * @return
     * @throws ExcepcaoPersistencia
     */
    public InfoExecutionDegree run(Integer degreeCurricularPlanID, final String executionYear)
            throws ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(
                        degreeCurricularPlanID);

        if (executionYear.equals("")) {
            return InfoExecutionDegree
                    .newInfoFromDomain(degreeCurricularPlan.getExecutionDegrees()
                            .get(0));
        }

        ExecutionDegree executionDegree = (ExecutionDegree) CollectionUtils.find(degreeCurricularPlan
                .getExecutionDegrees(), new Predicate() {

            public boolean evaluate(Object arg0) {
                ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                if (executionDegree.getExecutionYear().getYear().equals(executionYear)) {
                    return true;
                }
                return false;
            }
        });

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}