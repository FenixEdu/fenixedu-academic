package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DegreeCurricularPlanExecutionYearDispacthActionForDepartmentAdmOffice;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/showCurriculumHistoric",
        input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
        functionality = DegreeCurricularPlanExecutionYearDispacthActionForDepartmentAdmOffice.class)
@Forwards({ @Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp") })
public class ShowCurriculumHistoricActionForDepartmentAdmOffice extends ShowCurriculumHistoricAction {
}