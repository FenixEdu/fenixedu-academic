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
public class InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
        extends InfoExecutionDegreeWithInfoDegreeCurricularPlan {

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree copyFromDomain(
            ICursoExecucao executionDegree) {
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoDegreeCurricularPlan
                .copyFromDomain(executionDegree);
        if (infoExecutionDegree != null) {
            infoExecutionDegree.setInfoExecutionYear(InfoExecutionYear
                    .copyFromDomain(executionDegree.getExecutionYear()));
        }
        return infoExecutionDegree;
    }
}