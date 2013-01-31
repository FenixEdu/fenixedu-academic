package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.departmentMember;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "departmentMember",
		path = "/tsdProcessValuation",
		attribute = "tsdProcessValuationForm",
		formBean = "tsdProcessValuationForm",
		scope = "request",
		parameter = "method")
@Forwards(
		value = {
				@Forward(
						name = "showTSDProcessValuationByTeachers",
						path = "/departmentMember/teacherServiceDistribution/showTeacherServiceDistributionValuationByTeachers.jsp"),
				@Forward(
						name = "showTSDProcessValuationByCharts",
						path = "/departmentMember/teacherServiceDistribution/showTeacherServiceDistributionValuationByCharts.jsp"),
				@Forward(
						name = "showTSDProcessValuation",
						path = "/departmentMember/teacherServiceDistribution/showTeacherServiceDistributionValuation.jsp"),
				@Forward(
						name = "showTSDProcessValuationByTeachersAndCourses",
						path = "/departmentMember/teacherServiceDistribution/showTeacherServiceDistributionValuationByTeachersAndCourses.jsp") })
public class TSDProcessValuationActionForDepartmentMember extends
		net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.TSDProcessValuationAction {
}