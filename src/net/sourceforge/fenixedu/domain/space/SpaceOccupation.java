package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class SpaceOccupation extends SpaceOccupation_Base {

    public SpaceOccupation() {
	super();
	setOjbConcreteClass(this.getClass().getName());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	super.setSpace(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    public abstract Group getAccessGroup();

    public void checkPermissionsToMakeOperations() {
	Space parentSpace = this.getSpace();
	while (parentSpace != null) {
	    if (getAccessGroup() != null
		    && getAccessGroup().isMember(AccessControl.getUserView().getPerson())) {
		return;
	    }
	    parentSpace = parentSpace.getSuroundingSpace();
	}
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    @Override
    public void setSpace(Space space) {
	if (space == null) {
	    throw new DomainException("error.space.occupation.no.space");
	}
	super.setSpace(space);
    }
}
