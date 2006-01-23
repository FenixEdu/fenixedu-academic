/*
 * Created on Feb 20, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 * @return List containing all InfoExecutionDegrees, corresponding to Degree
 *         Curricular Plan
 */
public class ReadExecutionDegreesByDegreeCurricularPlanID extends Service {

    public List run(Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(DegreeCurricularPlan.class,
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