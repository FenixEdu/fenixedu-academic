package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction;


@Mapping(path = "/printGuidePage", module = "masterDegreeAdministrativeOffice")
@Forwards(value = {
	@Forward(name = "Success", path = "/guide/printGuidePage.jsp")})
public class VoidAction_AM4 extends VoidAction {

}
