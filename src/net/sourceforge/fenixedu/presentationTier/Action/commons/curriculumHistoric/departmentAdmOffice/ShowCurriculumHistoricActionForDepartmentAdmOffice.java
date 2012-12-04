package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/showCurriculumHistoric", input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")) })
public class ShowCurriculumHistoricActionForDepartmentAdmOffice extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction {
}