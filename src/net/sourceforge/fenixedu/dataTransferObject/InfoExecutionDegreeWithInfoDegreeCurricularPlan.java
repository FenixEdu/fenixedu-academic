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
public class InfoExecutionDegreeWithInfoDegreeCurricularPlan extends InfoExecutionDegree {

    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(IExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoDegreeCurricularPlan infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoDegreeCurricularPlan();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }

}
