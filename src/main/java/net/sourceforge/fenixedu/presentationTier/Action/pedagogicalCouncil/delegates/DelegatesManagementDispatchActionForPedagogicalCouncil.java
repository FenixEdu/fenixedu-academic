package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.DelegatesManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalDelegatesApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PedagogicalDelegatesApp.class, path = "manage", titleKey = "link.delegatesManagement")
@Mapping(module = "pedagogicalCouncil", path = "/delegatesManagement")
@Forwards(
        value = {
                @Forward(name = "showGGAEDelegates", path = "/pedagogicalCouncil/findDelegates.do?method=searchByDelegateType"),
                @Forward(
                        name = "showPossibleDelegates",
                        path = "/pedagogicalCouncil/electionsPeriodsManagement.do?method=showVotingResults&forwardTo=showPossibleDelegates"),
                @Forward(name = "createEditDelegates", path = "/pedagogicalCouncil/delegates/createEditDelegates.jsp"),
                @Forward(name = "prepareViewGGAEDelegates",
                        path = "/pedagogicalCouncil/delegatesManagement.do?method=prepareViewGGAEDelegates"),
                @Forward(name = "prepareViewDelegates",
                        path = "/pedagogicalCouncil/delegatesManagement.do?method=prepareViewDelegates"),
                @Forward(name = "createEditGGAEDelegates", path = "/pedagogicalCouncil/delegates/createEditGGAEDelegates.jsp") })
public class DelegatesManagementDispatchActionForPedagogicalCouncil extends DelegatesManagementDispatchAction {
}