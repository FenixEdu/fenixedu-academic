package net.sourceforge.fenixedu.presentationTier.Action.commons.student.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.CurriculumDispatchAction;


@Mapping(path = "/viewCurriculum", module = "coordinator", input = "/viewStudentCurriculum.do?method=prepareView&page=0", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards(value = {
	@Forward(name = "ShowStudentCurricularPlans", path = "df.page.showStudentCurricularPlans"),
	@Forward(name = "ShowStudentCurriculum", path = "df.page.showStudentCurriculum"),
	@Forward(name = "ShowStudentCurriculumForCoordinator", path = "df.page.showStudentCurriculumForCoordinator"),
	@Forward(name = "NotAuthorized", path = "df.page.notAuthorized")})
public class CurriculumDispatchAction_AM2 extends CurriculumDispatchAction {

}
