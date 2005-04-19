/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGuide;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegreeAndContributor extends InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegree {

    public void copyFromDomain(IGuide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setInfoContributor(InfoContributor.newInfoFromDomain(guide.getContributor()));
        }
    }

    public static InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegreeAndContributor newInfoFromDomain(IGuide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegreeAndContributor infoGuide = null;
        if (guide != null) {
            infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegreeAndContributor();
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }

}