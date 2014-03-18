package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearDispatchAction;


@Mapping(path = "/chooseExecutionYearToCandidateRegistration", module = "masterDegreeAdministrativeOffice", input = "/chooseExecutionYear.jsp", attribute = "chooseExecutionYearForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/marksManagement/chooseExecutionYearToManageMarks.jsp"),
	@Forward(name = "ChooseSuccess", path = "/chooseMasterDegreeToCandidateRegistration.do?method=prepareChooseMasterDegree")})
public class ChooseExecutionYearDispatchAction_AM6 extends ChooseExecutionYearDispatchAction {

}
