package net.sourceforge.fenixedu.presentationTier.Action.nape.student.candidacies.registration;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/registeredDegreeCandidacies", module = "nape")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies", path = "/nape/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp") })
public class RegisteredDegreeCandidaciesDAForNAPE extends RegisteredDegreeCandidaciesDA {

}
