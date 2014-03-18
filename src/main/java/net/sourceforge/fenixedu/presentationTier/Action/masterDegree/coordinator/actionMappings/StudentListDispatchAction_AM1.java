package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.StudentListDispatchAction;


@Mapping(path = "/studentListByDegree", module = "coordinator", input = "/student/indexStudent.jsp")
@Forwards(value = {
	@Forward(name = "ViewList", path = "/candidate/selectCandidateFromList.jsp"),
	@Forward(name = "ActionReady", path = "/candidate/visualizeCandidate.jsp"),
	@Forward(name = "ShowCourseList", path = "df.page.chooseCurricularCourse")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class)})
public class StudentListDispatchAction_AM1 extends StudentListDispatchAction {

}
