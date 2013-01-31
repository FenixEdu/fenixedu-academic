package net.sourceforge.fenixedu.presentationTier.Action.commons.resourceManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "resourceManager", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/resourceManager/welcomeScreen.jsp") })
public class VoidActionForResourceManager extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}