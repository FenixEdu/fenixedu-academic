package net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.registrations;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/registeredDegreeCandidacies", module = "gep")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies", path = "/gep/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp") })
public class RegisteredDegreeCandidaciesDAForGEP extends RegisteredDegreeCandidaciesDA {

}
