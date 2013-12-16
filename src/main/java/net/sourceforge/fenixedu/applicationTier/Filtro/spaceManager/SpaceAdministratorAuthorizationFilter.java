package net.sourceforge.fenixedu.applicationTier.Filtro.spaceManager;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class SpaceAdministratorAuthorizationFilter {

    public static final SpaceAdministratorAuthorizationFilter instance = new SpaceAdministratorAuthorizationFilter();

    public void execute(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        if (userView != null) {
            Person person = userView.getPerson();
            if (!AllocatableSpace.personIsSpacesAdministrator(person)) {
                throw new NotAuthorizedException();
            }
        } else {
            throw new NotAuthorizedException();
        }
    }

}
