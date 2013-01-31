package net.sourceforge.fenixedu.presentationTier.Action.manager.personnelSection;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "personnelSection", path = "/qualification", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "qualification", path = "/manager/qualifications/qualification.jsp", tileProperties = @Tile(
				title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")),
		@Forward(name = "showQualifications", path = "/manager/qualifications/showQualifications.jsp", tileProperties = @Tile(
				title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")),
		@Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp", tileProperties = @Tile(
				title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")) })
public class QualificationDAForPersonnelSection extends net.sourceforge.fenixedu.presentationTier.Action.manager.QualificationDA {
}