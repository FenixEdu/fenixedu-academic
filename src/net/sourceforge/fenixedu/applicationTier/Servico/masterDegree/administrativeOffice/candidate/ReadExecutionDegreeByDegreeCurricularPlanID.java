package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */

/*
 * Given the id of a degreeCurricularPlan, this service returns its
 * executionDegree specified in executionDegreeIndex
 */

public class ReadExecutionDegreeByDegreeCurricularPlanID implements IService {

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionDegreeIndex)
            throws ExcepcaoPersistencia {
        List infoExecutionDegreeList = null;
        ISuportePersistente sp;
        List executionDegrees = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        executionDegrees = sp.getIPersistentExecutionDegree().readByDegreeCurricularPlan(
                degreeCurricularPlan.getIdInternal());

        infoExecutionDegreeList = new ArrayList();

        for (Iterator iter = executionDegrees.iterator(); iter.hasNext();) {
            IExecutionDegree executionDegree = (IExecutionDegree) iter.next();

            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        if (executionYear.equals("")) {
            return InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain((IExecutionDegree) degreeCurricularPlan.getExecutionDegrees()
                            .get(0));
        }

        IExecutionDegree executionDegree = (IExecutionDegree) CollectionUtils.find(degreeCurricularPlan
                .getExecutionDegrees(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IExecutionDegree executionDegree = (IExecutionDegree) arg0;
                if (executionDegree.getExecutionYear().getYear().equals(executionYear)) {
                    return true;
                }
                return false;
            }
        });

        return InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                .newInfoFromDomain(executionDegree);
    }

}