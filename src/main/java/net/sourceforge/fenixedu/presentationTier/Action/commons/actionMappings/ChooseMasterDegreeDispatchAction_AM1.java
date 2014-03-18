package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseMasterDegreeDispatchAction;


@Mapping(path = "/chooseMasterDegreeToCourseStudentList", module = "masterDegreeAdministrativeOffice", input = "/lists/chooseMasterDegreeToDegreeStudentList.jsp", attribute = "chooseMasterDegreeForm", formBean = "chooseMasterDegreeForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/lists/chooseMasterDegreeToCourseStudentList.jsp"),
	@Forward(name = "ChooseSuccess", path = "/chooseCurricularCourseToCourseStudentList.do?method=prepareChooseCurricularCourse")})
public class ChooseMasterDegreeDispatchAction_AM1 extends ChooseMasterDegreeDispatchAction {

}
