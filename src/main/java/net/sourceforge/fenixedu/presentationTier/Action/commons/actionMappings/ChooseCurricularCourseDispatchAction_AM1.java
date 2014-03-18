package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseCurricularCourseDispatchAction;


@Mapping(path = "/listStudentsByCourse", module = "coordinator")
@Forwards(value = {
	@Forward(name = "ChooseSuccess", path = "df.page.displayStudentListByCourse"),
	@Forward(name = "PrepareSuccess", path = "df.page.displayStudentListByDegree"),
	@Forward(name = "NoStudents", path = "/studentListByDegree.do?method=getCurricularCourses"),
	@Forward(name = "NotAuthorized", path = "/student/notAuthorized.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class)})
public class ChooseCurricularCourseDispatchAction_AM1 extends ChooseCurricularCourseDispatchAction {

}
