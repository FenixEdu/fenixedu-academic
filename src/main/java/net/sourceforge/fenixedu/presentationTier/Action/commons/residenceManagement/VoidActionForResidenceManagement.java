package net.sourceforge.fenixedu.presentationTier.Action.commons.residenceManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "residenceManagement", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/residenceManagement/index.jsp") })
public class VoidActionForResidenceManagement extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}