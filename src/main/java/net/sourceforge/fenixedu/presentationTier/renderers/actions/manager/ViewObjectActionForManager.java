package net.sourceforge.fenixedu.presentationTier.renderers.actions.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/domain/view", scope = "session")
@Forwards(value = { @Forward(name = "show", path = "/commons/renderers/view.jsp") })
public class ViewObjectActionForManager extends net.sourceforge.fenixedu.presentationTier.renderers.actions.ViewObjectAction {
}