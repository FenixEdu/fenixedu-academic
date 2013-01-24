package net.sourceforge.fenixedu.presentationTier.Action.commons.academicAdministration;

import net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/administration/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/academicAdministration/index.jsp") })
public class VoidActionForAcademicAdministration extends VoidAction {

}