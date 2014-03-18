package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.MakeCandidateStudyPlanDispatchAction;


@Mapping(path = "/displayCandidateListToMakeStudyPlan", module = "coordinator", input = "/candidate/displayCandidateListToMakeStudyPlan.jsp", attribute = "chooseSecondMasterDegreeForm", formBean = "chooseSecondMasterDegreeForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/candidate/displayCandidateListToMakeStudyPlan.jsp")})
public class MakeCandidateStudyPlanDispatchAction_AM1 extends MakeCandidateStudyPlanDispatchAction {

}
