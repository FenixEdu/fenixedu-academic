package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.degreeChange;

import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		path = "/caseHandlingDegreeChangeCandidacyProcess",
		module = "nape",
		formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp") })
public class DegreeChangeCandidacyProcessDA extends
		net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeCandidacyProcessDA {

}
