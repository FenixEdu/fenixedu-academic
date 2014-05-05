package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.spaces.domain.Space;

public abstract class SpaceOccupation extends SpaceOccupation_Base {

    protected SpaceOccupation() {
        super();
    }

    public abstract Group getAccessGroup();

    public void checkPermissionsToManageSpaceOccupations() {
        User user = Authenticate.getUser();
        Space r = getSpace();
        if (SpaceUtils.personIsSpacesAdministrator(user.getPerson())
                || r.getManagementAccessGroupWithChainOfResponsability() != null
                && r.getManagementAccessGroupWithChainOfResponsability().isMember(user)) {
            return;
        }

        final Group group = getAccessGroup();
        if (group != null && group.isMember(user)) {
            return;
        }

        throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    public void checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManager() {
        User user = Authenticate.getUser();
        final Group group = getAccessGroup();
        if (group != null && group.isMember(user)) {
            return;
        }

        throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    public Space getSpace() {
        return getSpaceSet().isEmpty() ? null : getSpaceSet().iterator().next();
    }
}
