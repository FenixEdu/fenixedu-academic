/*
 * Created on 29/Jun/2004
 *
 */
package DataBeans;

import Dominio.ICursoExecucao;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus extends
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoExecutionDegree#copyFromDomain(Dominio.ICursoExecucao)
     */
    public void copyFromDomain(ICursoExecucao executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoCampus(InfoCampus.newInfoFromDomain(executionDegree.getCampus()));
        }
    }

    public static InfoExecutionDegree newInfoFromDomain(ICursoExecucao executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}