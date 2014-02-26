package net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.registrations;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepRegisteredDegreeCandidaciesApp;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = GepRegisteredDegreeCandidaciesApp.class, path = "list",
        titleKey = "label.registeredDegreeCandidacies.first.time.list")
@Mapping(path = "/registeredDegreeCandidacies", module = "gep")
@Forwards(@Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/gep/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp"))
public class RegisteredDegreeCandidaciesDAForGEP extends RegisteredDegreeCandidaciesDA {

    @Override
    protected Set<Degree> getDegreesToSearch() {
        return Bennu.getInstance().getDegreesSet();
    }

}
