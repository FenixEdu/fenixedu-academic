/*
 * Created on Feb 20, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 * @return List containing all InfoExecutionDegrees, corresponding to Degree
 *         Curricular Plan
 */
public class ReadExecutionDegreesByDegreeCurricularPlanID implements IService {
    public ReadExecutionDegreesByDegreeCurricularPlanID() {

    }

    public List run(Integer degreeCurricularPlanID) throws FenixServiceException {
        List infoExecutionDegreeList = null;
        try {
            ISuportePersistente sp;
            infoExecutionDegreeList = null;
            List executionDegrees = null;

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            executionDegrees = sp.getIPersistentExecutionDegree().readByDegreeCurricularPlan(
                    degreeCurricularPlan);

            infoExecutionDegreeList = new ArrayList();

            for (Iterator iter = executionDegrees.iterator(); iter.hasNext();) {
                IExecutionDegree executionDegree = (IExecutionDegree) iter.next();
                //CLONER
                //InfoExecutionDegree infoExecutionDegree =
                //    (InfoExecutionDegree) Cloner.get(executionDegree);
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYear
                        .newInfoFromDomain(executionDegree);
                infoExecutionDegreeList.add(infoExecutionDegree);
            }

            return infoExecutionDegreeList;

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }

}