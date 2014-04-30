package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.personnelSection;

import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.ProfessionalInformationDA;
import net.sourceforge.fenixedu.presentationTier.Action.personnelSection.PersonManagementAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "personnelSection", path = "/professionalInformation", functionality = PersonManagementAction.class)
@Forwards(value = { @Forward(name = "showProfessionalInformation",
        path = "/manager/personManagement/contracts/showProfessionalInformation.jsp") })
public class ProfessionalInformationDAForPersonnelSection extends ProfessionalInformationDA {
}