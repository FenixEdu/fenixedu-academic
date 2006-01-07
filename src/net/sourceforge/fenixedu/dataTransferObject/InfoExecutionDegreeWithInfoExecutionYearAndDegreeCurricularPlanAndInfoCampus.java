/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus extends
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree#copyFromDomain(Dominio.ExecutionDegree)
     */
    public void copyFromDomain(ExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoCampus(InfoCampus.newInfoFromDomain(executionDegree.getCampus()));
        }
    }

    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}