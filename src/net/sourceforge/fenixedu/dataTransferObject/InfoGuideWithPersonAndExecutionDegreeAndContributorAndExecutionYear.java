/*
 * Created on 2005/04/18
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Guide;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear extends InfoGuideWithPersonAndExecutionDegreeAndContributor {

    public void copyFromDomain(Guide guide) {
        super.copyFromDomain(guide);
    }

    public static InfoGuide newInfoFromDomain(Guide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear();
        if (guide != null) {
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }
}