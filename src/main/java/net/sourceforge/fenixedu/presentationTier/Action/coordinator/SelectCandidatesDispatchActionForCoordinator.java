package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.SelectCandidatesDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/displayListToSelectCandidates", module = "coordinator",
        input = "/candidate/displayCandidateListBySituation_bd.jsp", formBean = "chooseCandidateSituationForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "PrepareSuccess", path = "/coordinator/candidate/displayCandidateListBySituation_bd.jsp"),
        @Forward(name = "CancelConfirmation",
                path = "/coordinator/displayListToSelectCandidates.do?method=prepareSelectCandidates"),
        @Forward(name = "RequestConfirmation", path = "/coordinator/candidate/warning_bd.jsp"),
        @Forward(name = "FinalPresentation", path = "/coordinator/candidate/displayCandidatesFinalList_bd.jsp"),
        @Forward(name = "ChooseSuccess", path = "/coordinator/displayListToSelectCandidates.do?method=preparePresentation&page=0"),
        @Forward(name = "OrderCandidates",
                path = "/coordinator/displayListToSelectCandidates.do?method=getSubstituteCandidates&page=0"),
        @Forward(name = "Cancel", path = "/coordinator/displayListToSelectCandidates.do?method=prepareSelectCandidates&page=0"),
        @Forward(name = "OrderCandidatesReady", path = "/coordinator/candidate/displayChosenSelection_bd.jsp"),
        @Forward(name = "NumerusClaususNotDefined", path = "/coordinator/candidate/numerusClaususNotDefined_bd.jsp"),
        @Forward(name = "BackError", path = "/coordinator/candidate/backErrorPage_bd.jsp"),
        @Forward(name = "PrintReady", path = "/coordinator/candidate/approvalDispatchTemplate.jsp") })
public class SelectCandidatesDispatchActionForCoordinator extends SelectCandidatesDispatchAction {

}
