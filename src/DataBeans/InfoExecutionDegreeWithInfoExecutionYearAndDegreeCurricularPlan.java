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

    public void copyFromDomain(ICursoExecucao executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoExecutionYear(InfoExecutionYear
                    .newInfoFromDomain(executionDegree.getExecutionYear()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(
            ICursoExecucao executionDegree) {
        InfoExecutionDegreeWithInfoDegreeCurricularPlan infoExecutionDegree = null;
        if (infoExecutionDegree != null) {
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}