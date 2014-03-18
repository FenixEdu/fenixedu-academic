package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction;


@Mapping(path = "/contributorSection", module = "masterDegreeAdministrativeOffice")
@Forwards(value = {
	@Forward(name = "Success", path = "/contributor/indexContributor.jsp")})
public class VoidAction_AM7 extends VoidAction {

}
