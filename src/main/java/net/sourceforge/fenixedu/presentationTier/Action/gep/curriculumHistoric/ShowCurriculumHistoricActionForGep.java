package net.sourceforge.fenixedu.presentationTier.Action.gep.curriculumHistoric;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "gep", path = "/showCurriculumHistoric",
        input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
        functionality = DegreeCurricularPlanExecutionYearDispacthActionForGep.class)
@Forwards(@Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp"))
public class ShowCurriculumHistoricActionForGep extends ShowCurriculumHistoricAction {
}