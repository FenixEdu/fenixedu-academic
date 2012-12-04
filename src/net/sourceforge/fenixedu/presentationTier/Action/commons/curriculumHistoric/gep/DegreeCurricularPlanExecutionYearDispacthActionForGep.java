package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.gep;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "gep", path = "/chooseExecutionYearAndDegreeCurricularPlan", input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0", attribute = "executionYearDegreeCurricularPlanForm", formBean = "executionYearDegreeCurricularPlanForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp", tileProperties = @Tile(title = "private.gep.gepportal.consultationguidelines")),
	@Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp", tileProperties = @Tile(title = "private.gep.gepportal.consultationguidelines")) })
public class DegreeCurricularPlanExecutionYearDispacthActionForGep
	extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction {
}