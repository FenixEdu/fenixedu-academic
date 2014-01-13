package net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.registrations;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/registeredDegreeCandidacies", module = "gep")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/gep/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp", tileProperties = @Tile(
                title = "private.gep.registrations.registeredstudents1styear1sttime")) })
public class RegisteredDegreeCandidaciesDAForGEP extends RegisteredDegreeCandidaciesDA {

    @Override
    protected Set<Degree> getDegreesToSearch() {
        return Bennu.getInstance().getDegreesSet();
    }

}
