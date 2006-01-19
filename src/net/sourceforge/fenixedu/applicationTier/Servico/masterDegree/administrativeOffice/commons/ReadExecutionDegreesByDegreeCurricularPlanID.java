/*
 * Created on Feb 20, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 * @return List containing all InfoExecutionDegrees, corresponding to Degree
 *         Curricular Plan
 */
public class ReadExecutionDegreesByDegreeCurricularPlanID implements IService {

    public List run(Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        List infoExecutionDegreeList = new ArrayList();

        for (Iterator iter = degreeCurricularPlan.getExecutionDegrees().iterator(); iter.hasNext();) {
            ExecutionDegree executionDegree = (ExecutionDegree) iter.next();
            infoExecutionDegreeList.add(InfoExecutionDegreeWithInfoExecutionYear
                    .newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;

    }
}