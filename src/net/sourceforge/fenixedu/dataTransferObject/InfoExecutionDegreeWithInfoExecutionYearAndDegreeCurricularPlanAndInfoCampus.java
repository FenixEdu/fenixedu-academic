/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExecutionDegree;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus extends
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoExecutionDegree#copyFromDomain(Dominio.IExecutionDegree)
     */
    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoCampus(InfoCampus.newInfoFromDomain(executionDegree.getCampus()));
        }
    }

    public static InfoExecutionDegree newInfoFromDomain(IExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}