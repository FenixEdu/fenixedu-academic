/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ICursoExecucao;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionDegreeWithInfoDegreeCurricularPlan extends
        InfoExecutionDegree {

    public void copyFromDomain(ICursoExecucao executionDegree) {
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
    public static InfoExecutionDegree newInfoFromDomain(
            ICursoExecucao executionDegree) {
        InfoExecutionDegreeWithInfoDegreeCurricularPlan infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}