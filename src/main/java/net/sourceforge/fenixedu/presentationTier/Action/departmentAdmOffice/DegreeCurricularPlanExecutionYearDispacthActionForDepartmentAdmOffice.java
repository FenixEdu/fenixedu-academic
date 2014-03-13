package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeViewApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentAdmOfficeViewApp.class, path = "curriculum-historic", titleKey = "link.curriculumHistoric",
        bundle = "CurriculumHistoricResources")
@Mapping(module = "departmentAdmOffice", path = "/chooseExecutionYearAndDegreeCurricularPlan",
        input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
        formBean = "executionYearDegreeCurricularPlanForm")
@Forwards({ @Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp"),
        @Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp") })
public class DegreeCurricularPlanExecutionYearDispacthActionForDepartmentAdmOffice extends
        DegreeCurricularPlanExecutionYearDispacthAction {
}