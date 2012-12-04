package net.sourceforge.fenixedu.presentationTier.Action.commons.identificationCardManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "identificationCardManager", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/manageCardGeneration.do?method=firstPage") })
public class VoidActionForIdentificationCardManager extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}