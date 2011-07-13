package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "manager", path = "/insertExecutionCourse", input = "/insertExecutionCourse.do?method=prepareInsertExecutionCourse&page=0", attribute = "insertExecutionCourseForm", formBean = "insertExecutionCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "insertExecutionCourse", path = "/manager/executionCourseManagement/insertExecutionCourse.jsp"),
		@Forward(name = "firstPage", path = "/executionCourseManagement.do?method=firstPage") })
@Exceptions(value = {
		@ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class, key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class, key = "resources.Action.exceptions.ExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertExecutionCourseDispatchActionForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.InsertExecutionCourseDispatchAction {
}