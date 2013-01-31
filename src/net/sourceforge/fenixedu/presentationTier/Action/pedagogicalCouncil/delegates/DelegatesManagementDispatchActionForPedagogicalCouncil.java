package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.DelegatesManagementDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/delegatesManagement", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "showGGAEDelegates", path = "/findDelegates.do?method=searchByDelegateType", tileProperties = @Tile(
				title = "private.pedagogiccouncil.delegates.searchdelegates")),
		@Forward(
				name = "showPossibleDelegates",
				path = "/electionsPeriodsManagement.do?method=showVotingResults&forwardTo=showPossibleDelegates",
				tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.delegatecorps")),
		@Forward(
				name = "createEditDelegates",
				path = "/pedagogicalCouncil/delegates/createEditDelegates.jsp",
				tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.delegatecorps")),
		@Forward(
				name = "prepareViewGGAEDelegates",
				path = "/delegatesManagement.do?method=prepareViewGGAEDelegates",
				tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.delegatecorps")),
		@Forward(
				name = "prepareViewDelegates",
				path = "/delegatesManagement.do?method=prepareViewDelegates",
				tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.delegatecorps")),
		@Forward(
				name = "createEditGGAEDelegates",
				path = "/pedagogicalCouncil/delegates/createEditGGAEDelegates.jsp",
				tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.delegatecorps")) })
public class DelegatesManagementDispatchActionForPedagogicalCouncil extends DelegatesManagementDispatchAction {
}