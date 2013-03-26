package net.sourceforge.fenixedu.presentationTier.Action.commons.nape;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "nape", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/nape/index.jsp") })
public class VoidActionForNape extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}