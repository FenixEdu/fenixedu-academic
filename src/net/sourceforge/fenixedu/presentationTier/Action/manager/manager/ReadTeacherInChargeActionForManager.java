package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "manager",
		path = "/readTeacherInCharge",
		input = "/readCurricularCourse.do",
		attribute = "teacherForm",
		formBean = "teacherForm",
		scope = "request")
@Forwards(value = { @Forward(name = "readExecutionCourseTeachers", path = "/manager/readTeachers_bd.jsp", tileProperties = @Tile(
		navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
public class ReadTeacherInChargeActionForManager extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.ReadTeacherInChargeAction {
}