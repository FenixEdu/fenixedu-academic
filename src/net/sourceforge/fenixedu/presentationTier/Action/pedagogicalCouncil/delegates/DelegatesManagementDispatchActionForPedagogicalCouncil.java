package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.DelegatesManagementDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "pedagogicalCouncil", path = "/delegatesManagement", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "showGGAEDelegates", path = "/findDelegates.do?method=searchByDelegateType"),
	@Forward(name = "showPossibleDelegates", path = "/electionsPeriodsManagement.do?method=showVotingResults&forwardTo=showPossibleDelegates"),
	@Forward(name = "createEditDelegates", path = "/pedagogicalCouncil/delegates/createEditDelegates.jsp"),
	@Forward(name = "prepareViewGGAEDelegates", path = "/delegatesManagement.do?method=prepareViewGGAEDelegates"),
	@Forward(name = "prepareViewDelegates", path = "/delegatesManagement.do?method=prepareViewDelegates"),
	@Forward(name = "createEditGGAEDelegates", path = "/pedagogicalCouncil/delegates/createEditGGAEDelegates.jsp") })
public class DelegatesManagementDispatchActionForPedagogicalCouncil extends DelegatesManagementDispatchAction {
}