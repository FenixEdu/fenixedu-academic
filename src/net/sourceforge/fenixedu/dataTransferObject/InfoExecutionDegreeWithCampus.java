package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExecutionDegree;

/**
 * @author Fernanda Quitério Created on 27/Jul/2004
 *  
 */
public class InfoExecutionDegreeWithCampus extends InfoExecutionDegree {

    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoCampus(InfoCampus.newInfoFromDomain(executionDegree.getCampus()));
        }
    }

    public static InfoExecutionDegree newInfoFromDomain(IExecutionDegree executionDegree) {
        InfoExecutionDegreeWithCampus infoExecutionDegreeWithCampus = null;
        if (executionDegree != null) {
            infoExecutionDegreeWithCampus = new InfoExecutionDegreeWithCampus();
            infoExecutionDegreeWithCampus.copyFromDomain(executionDegree);
        }
        return infoExecutionDegreeWithCampus;
    }
}