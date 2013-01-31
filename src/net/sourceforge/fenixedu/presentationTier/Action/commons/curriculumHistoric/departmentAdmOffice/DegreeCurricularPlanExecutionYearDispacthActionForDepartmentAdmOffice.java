package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "departmentAdmOffice",
		path = "/chooseExecutionYearAndDegreeCurricularPlan",
		input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
		attribute = "executionYearDegreeCurricularPlanForm",
		formBean = "executionYearDegreeCurricularPlanForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(
				name = "chooseExecutionYear",
				path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp",
				tileProperties = @Tile(
						title = "private.administrationofcreditsofdepartmentteachers.consultations.historyguidelines")),
		@Forward(
				name = "showActiveCurricularCourses",
				path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp",
				tileProperties = @Tile(
						title = "private.administrationofcreditsofdepartmentteachers.consultations.historyguidelines")) })
public class DegreeCurricularPlanExecutionYearDispacthActionForDepartmentAdmOffice
		extends
		net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction {
}