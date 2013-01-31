package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeCandidacyForGraduatedPerson;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess",
		module = "scientificCouncil",
		formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp", tileProperties = @Tile(
		title = "private.scientificcouncil.applications")) })
public class DegreeCandidacyForGraduatedPersonProcessDA extends
		net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonProcessDA {

	public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
		final Integer degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

		if (degreeCurricularPlanOID != null) {
			return rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
		}

		return null;
	}

}
