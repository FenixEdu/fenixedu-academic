package net.sourceforge.fenixedu.presentationTier.Action.commons.bolonhaManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "bolonhaManager", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/bolonhaManager/index.jsp") })
public class VoidActionForBolonhaManager extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}