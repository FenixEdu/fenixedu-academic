package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearDispatchAction;


@Mapping(path = "/chooseExecutionYearToListCourseStudents", module = "masterDegreeAdministrativeOffice", input = "/chooseExecutionYear.jsp", attribute = "chooseExecutionYearToListCourseStudents", formBean = "chooseExecutionYearForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/lists/chooseExecutionYearToCourseStudentList.jsp"),
	@Forward(name = "ChooseSuccess", path = "/chooseMasterDegreeToCourseStudentList.do?method=prepareChooseMasterDegree&page=0")})
public class ChooseExecutionYearDispatchAction_AM1 extends ChooseExecutionYearDispatchAction {

}
