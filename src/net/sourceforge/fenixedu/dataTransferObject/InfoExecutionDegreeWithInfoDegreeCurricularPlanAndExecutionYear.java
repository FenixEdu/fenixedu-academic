/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExecutionDegree;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear extends InfoExecutionDegreeWithInfoDegreeCurricularPlan {

    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear()));
        }
    }

    public static InfoExecutionDegree newInfoFromDomain(IExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}