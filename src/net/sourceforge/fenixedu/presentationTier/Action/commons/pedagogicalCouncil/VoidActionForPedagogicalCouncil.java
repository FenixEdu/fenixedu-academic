package net.sourceforge.fenixedu.presentationTier.Action.commons.pedagogicalCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "pedagogicalCouncil", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/pedagogicalCouncil/index.jsp") })
public class VoidActionForPedagogicalCouncil extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}