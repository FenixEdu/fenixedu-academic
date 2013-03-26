package net.sourceforge.fenixedu.presentationTier.Action.commons.alumni;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "alumni", path = "/administrativeOfficeServicesSection", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Success", path = "df.page.administrativeOfficeServicesIndex") })
public class VoidActionForAlumni extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}