package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.departmentAdmOffice;

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

@Mapping(module = "departmentAdmOffice", path = "/globalTSDProcessValuation", attribute = "globalTSDProcessValuationForm", formBean = "globalTSDProcessValuationForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "showGlobalTSDProcesssValuationByTeachersAndCourses", path = "/departmentMember/teacherServiceDistribution/showGlobalTeacherServiceDistributionsValuationByTeachersAndCourses.jsp"),
		@Forward(name = "showGlobalTSDProcesss", path = "/departmentMember/teacherServiceDistribution/showGlobalTeacherServiceDistributions.jsp"),
		@Forward(name = "showGlobalTSDProcesssValuationByTeachers", path = "/departmentMember/teacherServiceDistribution/showGlobalTeacherServiceDistributionsValuationByTeachers.jsp"),
		@Forward(name = "showGlobalTSDProcesssValuationByCourses", path = "/departmentMember/teacherServiceDistribution/showGlobalTeacherServiceDistributionsValuationByCourses.jsp") })
public class GlobalTSDProcessValuationActionForDepartmentAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.GlobalTSDProcessValuationAction {
}