/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.CursoExecucao;
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
    
    public void copyToDomain(InfoExecutionDegree infoExecutionDegree, ICursoExecucao executionDegree) {
        super.copyToDomain(infoExecutionDegree, executionDegree);
        executionDegree.setCurricularPlan(InfoDegreeCurricularPlanWithDegree.newDomainFromInfo(infoExecutionDegree.getInfoDegreeCurricularPlan()));
    }
    
    public static ICursoExecucao newDomainFromInfo(InfoExecutionDegree infoExecutionDegree) {
        ICursoExecucao executionDegree = null;
        if(infoExecutionDegree != null) {
            executionDegree = new CursoExecucao();
            InfoExecutionDegreeWithInfoDegreeCurricularPlan infoExecutionDegreeWithInfoDegreeCurricularPlan = new InfoExecutionDegreeWithInfoDegreeCurricularPlan();
            infoExecutionDegreeWithInfoDegreeCurricularPlan.copyToDomain(infoExecutionDegree, executionDegree);
        }
        return executionDegree;
    }
}