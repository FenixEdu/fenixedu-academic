/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ExecutionDegree;
import Dominio.IExecutionDegree;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionDegreeWithInfoDegreeCurricularPlan extends InfoExecutionDegree {

    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                    .newInfoFromDomain(executionDegree.getCurricularPlan()));
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
        executionDegree.setCurricularPlan(InfoDegreeCurricularPlanWithDegree
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