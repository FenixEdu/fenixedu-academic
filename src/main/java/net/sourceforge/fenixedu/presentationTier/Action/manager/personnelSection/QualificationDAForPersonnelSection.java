package net.sourceforge.fenixedu.presentationTier.Action.manager.personnelSection;

import net.sourceforge.fenixedu.presentationTier.Action.manager.QualificationDA;
import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.PersonManagementAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "personnelSection", path = "/qualification", functionality = PersonManagementAction.class)
@Forwards({ @Forward(name = "qualification", path = "/manager/qualifications/qualification.jsp"),
        @Forward(name = "showQualifications", path = "/manager/qualifications/showQualifications.jsp"),
        @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp") })
public class QualificationDAForPersonnelSection extends QualificationDA {
}