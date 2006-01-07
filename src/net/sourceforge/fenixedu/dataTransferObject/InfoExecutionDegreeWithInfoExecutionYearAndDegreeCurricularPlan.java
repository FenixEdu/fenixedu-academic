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
public class InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan extends
        InfoExecutionDegreeWithInfoDegreeCurricularPlan {

    public void copyFromDomain(ExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}