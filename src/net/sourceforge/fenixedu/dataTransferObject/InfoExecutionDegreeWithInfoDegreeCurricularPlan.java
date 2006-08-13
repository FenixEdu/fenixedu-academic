/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author João Mota
 * 
 */
public class InfoExecutionDegreeWithInfoDegreeCurricularPlan extends InfoExecutionDegree {

    public void copyFromDomain(ExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoDegreeCurricularPlan infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoDegreeCurricularPlan();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }

}
