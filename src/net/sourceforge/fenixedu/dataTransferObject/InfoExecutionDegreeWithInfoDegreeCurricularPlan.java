/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
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
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }

    public void copyToDomain(InfoExecutionDegree infoExecutionDegree, IExecutionDegree executionDegree) {
        super.copyToDomain(infoExecutionDegree, executionDegree);
        executionDegree.setDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                .newDomainFromInfo(infoExecutionDegree.getInfoDegreeCurricularPlan()));
    }

    public static IExecutionDegree newDomainFromInfo(InfoExecutionDegree infoExecutionDegree) {
        IExecutionDegree executionDegree = null;
        if (infoExecutionDegree != null) {
            executionDegree = new ExecutionDegree();
            InfoExecutionDegreeWithInfoDegreeCurricularPlan infoExecutionDegreeWithInfoDegreeCurricularPlan = new InfoExecutionDegreeWithInfoDegreeCurricularPlan();
            infoExecutionDegreeWithInfoDegreeCurricularPlan.copyToDomain(infoExecutionDegree,
                    executionDegree);
        }
        return executionDegree;
    }
}