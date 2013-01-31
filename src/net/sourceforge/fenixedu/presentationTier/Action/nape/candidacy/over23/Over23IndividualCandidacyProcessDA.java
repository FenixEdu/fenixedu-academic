package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.over23;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		path = "/caseHandlingOver23IndividualCandidacyProcess",
		module = "nape",
		formBeanClass = Over23IndividualCandidacyProcessDA.CandidacyForm.class)
@Forwards({ @Forward(name = "intro", path = "/nape/candidacy/mainCandidacyProcess.jsp"),
		@Forward(name = "list-allowed-activities", path = "/nape/candidacy/over23/listIndividualCandidacyActivities.jsp") })
public class Over23IndividualCandidacyProcessDA extends
		net.sourceforge.fenixedu.presentationTier.Action.candidacy.over23.Over23IndividualCandidacyProcessDA {
}
