package net.sourceforge.fenixedu.presentationTier.Action.commons.directiveCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "directiveCouncil", path = "/gratuitySection", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Success", path = "firstPageGratuitySection") })
public class VoidActionForDirectiveCouncil extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}