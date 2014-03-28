package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.DegreeCurricularPlanExecutionYearDispacthActionForTeacher;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "teacher", path = "/showCurriculumHistoric",
        functionality = DegreeCurricularPlanExecutionYearDispacthActionForTeacher.class)
@Forwards(@Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp"))
public class ShowCurriculumHistoricActionForTeacher extends ShowCurriculumHistoricAction {
}