package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.personnelSection;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "personnelSection", path = "/professionalInformation", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showProfessionalInformation", path = "/manager/personManagement/contracts/showProfessionalInformation.jsp", tileProperties = @Tile(title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")) })
public class ProfessionalInformationDAForPersonnelSection extends
	net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.ProfessionalInformationDA {
}