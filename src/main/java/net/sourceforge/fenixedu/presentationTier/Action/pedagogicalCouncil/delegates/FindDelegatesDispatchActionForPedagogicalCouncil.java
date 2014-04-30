package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.FindDelegatesDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalDelegatesApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PedagogicalDelegatesApp.class, path = "find", titleKey = "link.findDelegates")
@Mapping(module = "pedagogicalCouncil", path = "/findDelegates")
@Forwards({ @Forward(name = "showDelegates", path = "/commons/delegates/showDelegates.jsp"),
        @Forward(name = "searchDelegates", path = "/commons/delegates/searchDelegates.jsp") })
public class FindDelegatesDispatchActionForPedagogicalCouncil extends FindDelegatesDispatchAction {
}