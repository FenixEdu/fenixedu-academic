package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.degreeChange;

import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "nape", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/caseHandlingDegreeChangeCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/scientificCouncil/candidacy/degreeChange/listIndividualCandidacyActivities.jsp") })
public class DegreeChangeIndividualCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeIndividualCandidacyProcessDA {

}
