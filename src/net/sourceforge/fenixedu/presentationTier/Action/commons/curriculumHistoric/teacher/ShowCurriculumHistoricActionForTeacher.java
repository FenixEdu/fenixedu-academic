package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.teacher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "teacher",
		path = "/showCurriculumHistoric",
		input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "show-report", path = "view-curriculum-historic") })
public class ShowCurriculumHistoricActionForTeacher extends
		net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction {
}