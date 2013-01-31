package net.sourceforge.fenixedu.presentationTier.Action.commons.internationalRelatOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "internationalRelatOffice", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "indexPage") })
public class VoidActionForInternationalRelatOffice extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}