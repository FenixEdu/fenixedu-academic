package net.sourceforge.fenixedu.presentationTier.Action.commons.library;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "library", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/library/index.jsp") })
public class VoidActionForLibrary extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}