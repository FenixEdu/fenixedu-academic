package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.degreeTransfer;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		path = "/caseHandlingDegreeTransferCandidacyProcess",
		module = "nape",
		formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp") })
public class DegreeTransferCandidacyProcessDA extends
		net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer.DegreeTransferCandidacyProcessDA {

	public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
		final Integer degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

		if (degreeCurricularPlanOID != null) {
			return rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
		}

		return null;
	}

}
