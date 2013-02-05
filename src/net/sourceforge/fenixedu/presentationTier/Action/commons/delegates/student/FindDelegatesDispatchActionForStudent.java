package net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.student;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "student", path = "/findDelegates", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showDelegates", path = "showDelegates") })
public class FindDelegatesDispatchActionForStudent extends
        net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.FindDelegatesDispatchAction {
}