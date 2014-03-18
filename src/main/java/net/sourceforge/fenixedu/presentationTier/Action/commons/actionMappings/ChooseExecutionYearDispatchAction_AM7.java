package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearDispatchAction;


@Mapping(path = "/chooseExecutionYearToCreateGuide", module = "masterDegreeAdministrativeOffice", input = "/chooseExecutionYear.jsp", attribute = "chooseExecutionYearForm", formBean = "chooseExecutionYearForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/guide/chooseExecutionYearToCreateGuide.jsp"),
	@Forward(name = "ChooseSuccess", path = "/createGuideDispatchAction.do?method=prepare&page=0")})
public class ChooseExecutionYearDispatchAction_AM7 extends ChooseExecutionYearDispatchAction {

}
