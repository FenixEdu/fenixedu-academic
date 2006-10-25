package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public abstract class SpaceInformation extends SpaceInformation_Base implements
	Comparable<SpaceInformation> {

    public abstract String getPresentationName();

    public abstract FactoryExecutor getSpaceFactoryEditor();

    protected SpaceInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public void delete() {
	if (getSpace().getSpaceInformationsCount() == 1) {
	    throw new DomainException("space.must.have.at.least.one.space.information");
	}
	deleteWithoutCheckNumberOfSpaceInformations();
    }

    protected void deleteWithoutCheckNumberOfSpaceInformations() {
	super.setSpace(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    public int compareTo(SpaceInformation spaceInformation) {
	if (getValidUntil() == null) {
	    return 1;
	} else if (spaceInformation.getValidUntil() == null) {
	    return -1;
	} else {
	    return getValidUntil().compareTo(spaceInformation.getValidUntil());
	}
    }

    protected void checkSpaceInformationsIntersection(final YearMonthDay begin, final YearMonthDay end) {
	checkEndDate(begin, end);
	for (SpaceInformation information : getSpace().getSpaceInformationsSet()) {
	    if (!information.equals(this) && information.spaceInformationsIntersection(begin, end)) {
		throw new DomainException("error.space.information.intersection");
	    }
	}
    }

    public boolean isActive(YearMonthDay currentDate) {
	return (!this.getValidFrom().isAfter(currentDate) && (this.getValidUntil() == null || !this
		.getValidUntil().isBefore(currentDate)));
    }

    @Override
    public void setSpace(Space space) {
	if (space == null) {
	    throw new DomainException("error.space.information.no.space");
	}
	super.setSpace(space);
    }

    @Override
    public void setValidFrom(YearMonthDay begin) {
	checkSpaceInformationsIntersection(begin, getValidUntil());
	super.setValidFrom(begin);
    }

    @Override
    public void setValidUntil(YearMonthDay end) {
	checkSpaceInformationsIntersection(getValidFrom(), end);
	super.setValidUntil(end);
    }

    protected void setTimeInterval(final YearMonthDay begin, final YearMonthDay end) {
	setNewValidUntilDateIfNecessary(begin.minusDays(1));
	checkSpaceInformationsIntersection(begin, end);
	super.setValidFrom(begin);
	super.setValidUntil(end);
    }

    private void checkEndDate(final YearMonthDay begin, final YearMonthDay end) {
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }

    private boolean spaceInformationsIntersection(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getValidFrom().isAfter(end)) && (this.getValidUntil() == null || !this
		.getValidUntil().isBefore(begin)));
    }

    private void setValidUntilWithoutCheck(YearMonthDay end) {
	super.setValidUntil(end);
    }

    private void setNewValidUntilDateIfNecessary(YearMonthDay day) {
	for (SpaceInformation information : getSpace().getSpaceInformationsSet()) {
	    if (!information.equals(this) && information.getValidUntil() == null) {
		if (!information.getValidFrom().isBefore(day)) {
		    throw new DomainException("error.space.information.duration");
		} else {
		    information.setValidUntilWithoutCheck(day);
		}
	    }
	}
    }

    protected YearMonthDay getNextPossibleValidFromDate() {
	SpaceInformation mostRecentSpaceInformation = getSpace().getMostRecentSpaceInformation();
	if (mostRecentSpaceInformation.getValidUntil() != null) {
	    return mostRecentSpaceInformation.getValidUntil().plusDays(1);
	} else {
	    return mostRecentSpaceInformation.getValidFrom().plusDays(2);
	}
    }
}
