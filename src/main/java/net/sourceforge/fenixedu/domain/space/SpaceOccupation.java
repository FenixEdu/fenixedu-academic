package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;

public abstract class SpaceOccupation extends SpaceOccupation_Base {

    protected SpaceOccupation() {
        super();
    }

    public abstract Group getAccessGroup();

    public void checkPermissionsToManageSpaceOccupations() {
        User user = Authenticate.getUser();
        if (getSpace().personHasPermissionsToManageSpace(user)) {
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
        return (Space) getResource();
    }

    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
        if (!resource.isSpace()) {
            throw new DomainException("error.allocation.invalid.resource.type");
        }
    }

    @Override
    public boolean isSpaceOccupation() {
        return true;
    }
}
