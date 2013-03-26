package net.sourceforge.fenixedu.presentationTier.Action.commons.externalSupervision;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "externalSupervision", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "externalSupervisor-index") })
public class VoidActionForExternalSupervision extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}