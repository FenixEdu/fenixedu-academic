package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearDispatchAction;


@Mapping(path = "/chooseExecutionYearToSelectCandidates", module = "masterDegreeAdministrativeOffice", input = "/chooseExecutionYear.jsp", attribute = "chooseExecutionYearForm", formBean = "chooseExecutionYearForm")
@Forwards(value = {
	@Forward(name = "DisplayMasterDegreeList", path = "/candidate/displayMasterDegreesToEditCandidates.jsp"),
	@Forward(name = "MasterDegreeReady", path = "/candidate/displayCurricularPlanByChosenMasterDegreeToEditCandidates.jsp"),
	@Forward(name = "PrepareSuccess", path = "/chooseExecutionYear.jsp"),
	@Forward(name = "ChooseSuccess", path = "/displayListToSelectCandidates.do?method=prepareSelectCandidates&page=0")})
public class ChooseExecutionYearDispatchAction_AM4 extends ChooseExecutionYearDispatchAction {

}
