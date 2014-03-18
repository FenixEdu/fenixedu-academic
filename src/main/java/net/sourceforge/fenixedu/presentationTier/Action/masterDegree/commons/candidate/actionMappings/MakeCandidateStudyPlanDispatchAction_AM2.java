package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.MakeCandidateStudyPlanDispatchAction;


@Mapping(path = "/displayCourseListToStudyPlan", module = "coordinator", input = "/candidate/displayListOfCoursesToChoose.jsp", attribute = "chooseCourseListForm", formBean = "chooseCourseListForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/candidate/displayListOfCoursesToChoose.jsp"),
	@Forward(name = "ChooseSuccess", path = "/candidate/displayCandidateEnrolments.jsp"),
	@Forward(name = "PrintReady", path = "/candidate/studyPlanTemplate.jsp"),
	@Forward(name = "BackError", path = "/backError.do", redirect = true)})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.NoChoiceMadeActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChoiceMadeActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.NotAuthorizedActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException.class)})
public class MakeCandidateStudyPlanDispatchAction_AM2 extends MakeCandidateStudyPlanDispatchAction {

}
