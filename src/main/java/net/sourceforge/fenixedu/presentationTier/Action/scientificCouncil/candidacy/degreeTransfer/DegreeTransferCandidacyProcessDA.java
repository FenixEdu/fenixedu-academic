package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeTransfer;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/caseHandlingDegreeTransferCandidacyProcess", module = "scientificCouncil",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp", tileProperties = @Tile(
        title = "private.scientificcouncil.applications")) })
public class DegreeTransferCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer.DegreeTransferCandidacyProcessDA {

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            return AbstractDomainObject.fromExternalId(degreeCurricularPlanOID);
        }

        return null;
    }

}
