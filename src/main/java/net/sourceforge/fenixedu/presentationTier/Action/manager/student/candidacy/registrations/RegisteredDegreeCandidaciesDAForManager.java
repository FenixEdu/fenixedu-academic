package net.sourceforge.fenixedu.presentationTier.Action.manager.student.candidacy.registrations;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/registeredDegreeCandidacies", module = "manager")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/manager/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp") })
public class RegisteredDegreeCandidaciesDAForManager extends RegisteredDegreeCandidaciesDA {

    @Override
    protected Set<Degree> getDegreesToSearch() {
        return Bennu.getInstance().getDegreesSet();
    }

}
