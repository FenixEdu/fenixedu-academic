package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.gep;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "gep",
		path = "/showCurriculumHistoric",
		input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp") })
public class ShowCurriculumHistoricActionForGep extends
		net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction {
}