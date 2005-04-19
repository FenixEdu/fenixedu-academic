/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGuide;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegree extends InfoGuideWithPerson {

    public void copyFromDomain(IGuide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan.newInfoFromDomain(guide.getExecutionDegree()));
        }
    }

    public static InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegree newInfoFromDomain(IGuide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegree infoGuide = null;
        if (guide != null) {
            infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegree();
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }

}