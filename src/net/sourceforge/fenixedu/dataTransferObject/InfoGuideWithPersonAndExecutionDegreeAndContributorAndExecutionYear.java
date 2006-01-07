/*
 * Created on 2005/04/18
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Guide;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear extends InfoGuideWithPersonAndExecutionDegreeAndContributor {

    public void copyFromDomain(Guide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            ExecutionYear executionYear = guide.getExecutionDegree().getExecutionYear();
            InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);
            getInfoExecutionDegree().setInfoExecutionYear(infoExecutionYear);
        }
    }

    public static InfoGuide newInfoFromDomain(Guide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear();
        if (guide != null) {
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }
}