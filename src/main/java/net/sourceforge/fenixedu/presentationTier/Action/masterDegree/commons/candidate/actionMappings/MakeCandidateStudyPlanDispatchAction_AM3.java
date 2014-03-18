package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.MakeCandidateStudyPlanDispatchAction;


@Mapping(path = "/displayCandidateListToMakeStudyPlan", module = "masterDegreeAdministrativeOffice", input = "/candidate/displayCandidateListToMakeStudyPlan.jsp", attribute = "chooseSecondMasterDegreeForm", formBean = "chooseSecondMasterDegreeForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/candidate/displayCandidateListToMakeStudyPlan.jsp"),
	@Forward(name = "PrepareSecondChooseMasterDegreeReady", path = "/candidate/secondChooseMasterDegree.jsp"),
	@Forward(name = "ChooseReady", path = "/displayCourseListToStudyPlan.do?method=chooseMasterDegree&page=0"),
	@Forward(name = "ChooseSuccess", path = "/displayCourseListToStudyPlan.do?method=prepareSelectCourseList&page=0"),
	@Forward(name = "PrintReady", path = "/candidate/studyPlanTemplate.jsp"),
	@Forward(name = "BackError", path = "/backErrorPage.do", redirect = true)})
public class MakeCandidateStudyPlanDispatchAction_AM3 extends MakeCandidateStudyPlanDispatchAction {

}
