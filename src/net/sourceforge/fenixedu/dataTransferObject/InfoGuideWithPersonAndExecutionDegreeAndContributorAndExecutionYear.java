/*
 * Created on 2005/04/18
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGuide;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear extends InfoGuideWithPersonAndExecutionDegreeAndContributor {

    public void copyFromDomain(IGuide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            IExecutionYear executionYear = guide.getExecutionDegree().getExecutionYear();
            InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);
            getInfoExecutionDegree().setInfoExecutionYear(infoExecutionYear);
        }
    }

    public static InfoGuide newInfoFromDomain(IGuide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear();
        if (guide != null) {
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }
}