package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.departmentMember;

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

@Mapping(module = "departmentMember", path = "/tsdCourseValuation", input = "/tsdCourseValuation.do?method=loadTSDCourses&page=0&fromValidation=1", attribute = "tsdCourseValuationForm", formBean = "tsdCourseValuationForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "courseValuationShifts", path = "/departmentMember/teacherServiceDistribution/courseValuationShifts.jsp"),
		@Forward(name = "defineSchoolClassCalculationMethod", path = "/departmentMember/teacherServiceDistribution/editSchoolClassCalculationMethod.jsp"),
		@Forward(name = "courseValuationWeights", path = "/departmentMember/teacherServiceDistribution/courseValuationWeights.jsp"),
		@Forward(name = "courseValuationStudents", path = "/departmentMember/teacherServiceDistribution/courseValuationStudents.jsp"),
		@Forward(name = "courseValuationHours", path = "/departmentMember/teacherServiceDistribution/courseValuationHours.jsp") })
public class TSDCourseValuationActionForDepartmentMember extends net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.TSDCourseValuationAction {
}