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

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree copyFromDomain(
            ICursoExecucao executionDegree) {
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                .copyFromDomain(executionDegree);
        if (infoExecutionDegree != null) {
            infoExecutionDegree
                    .setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                            .copyFromDomain(executionDegree.getCurricularPlan()));
        }
        return infoExecutionDegree;
    }
}