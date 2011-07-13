package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

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

@Mapping(module = "manager", path = "/saveTeachersBody", input = "/readTeacherInCharge.do", attribute = "teacherForm", formBean = "teacherForm", scope = "request")
@Forwards(value = {
		@Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do"),
		@Forward(name = "readTeacherInCharge", path = "/readTeacherInCharge.do") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException.class, key = "resources.Action.exceptions.InvalidArgumentsActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class SaveTeachersBodyActionForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.SaveTeachersBodyAction {
}