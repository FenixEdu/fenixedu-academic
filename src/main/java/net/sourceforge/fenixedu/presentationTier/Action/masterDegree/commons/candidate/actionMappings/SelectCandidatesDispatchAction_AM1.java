package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.candidate.SelectCandidatesDispatchAction;


@Mapping(path = "/displayListToSelectCandidates", module = "coordinator", input = "/candidate/displayCandidateListBySituation.jsp", attribute = "chooseCandidateSituationForm", formBean = "chooseCandidateSituationForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/candidate/displayCandidateListBySituation.jsp"),
	@Forward(name = "CancelConfirmation", path = "/displayListToSelectCandidates.do?method=prepareSelectCandidates"),
	@Forward(name = "RequestConfirmation", path = "/candidate/warning.jsp"),
	@Forward(name = "FinalPresentation", path = "/candidate/displayCandidatesFinalList.jsp"),
	@Forward(name = "ChooseSuccess", path = "/displayListToSelectCandidates.do?method=preparePresentation&page=0"),
	@Forward(name = "OrderCandidates", path = "/displayListToSelectCandidates.do?method=getSubstituteCandidates&page=0"),
	@Forward(name = "Cancel", path = "/displayListToSelectCandidates.do?method=prepareSelectCandidates&page=0"),
	@Forward(name = "OrderCandidatesReady", path = "/candidate/displayChosenSelection.jsp"),
	@Forward(name = "NumerusClaususNotDefined", path = "/candidate/numerusClaususNotDefined.jsp"),
	@Forward(name = "BackError", path = "/backErrorCandidateApprovalPage.do", redirect = true),
	@Forward(name = "PrintReady", path = "/candidate/approvalDispatchTemplate.jsp")})
public class SelectCandidatesDispatchAction_AM1 extends SelectCandidatesDispatchAction {

}
