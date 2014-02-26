package net.sourceforge.fenixedu.presentationTier.Action.library;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "LibraryResources", path = "library", titleKey = "label.library", hint = "Library",
        accessGroup = "role(LIBRARY)")
@Mapping(path = "/index", module = "library", parameter = "/library/index.jsp")
public class LibraryApplication extends ForwardAction {

}
