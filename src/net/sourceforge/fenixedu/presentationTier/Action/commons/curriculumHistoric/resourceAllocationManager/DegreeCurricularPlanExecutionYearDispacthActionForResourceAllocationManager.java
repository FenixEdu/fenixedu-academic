package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.resourceAllocationManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "resourceAllocationManager", path = "/chooseExecutionYearAndDegreeCurricularPlan", input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0", attribute = "executionYearDegreeCurricularPlanForm", formBean = "executionYearDegreeCurricularPlanForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseExecutionYear", path = "view-degree-curricular-plan-execution-year"),
	@Forward(name = "showActiveCurricularCourses", path = "show-active-curricular-scopes") })
public class DegreeCurricularPlanExecutionYearDispacthActionForResourceAllocationManager
	extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction {
}