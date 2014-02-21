package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.FindDelegatesDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.delegate.DelegateApplication.DelegateConsultApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DelegateConsultApp.class, path = "delegates", titleKey = "link.delegates")
@Mapping(module = "delegate", path = "/findDelegates")
@Forwards(value = { @Forward(name = "showDelegates", path = "/commons/delegates/showDelegates.jsp"),
        @Forward(name = "searchDelegates", path = "/commons/delegates/searchDelegates.jsp") })
public class FindDelegatesDA extends FindDelegatesDispatchAction {
}