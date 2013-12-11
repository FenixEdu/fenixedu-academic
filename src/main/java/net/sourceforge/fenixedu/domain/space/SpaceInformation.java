package net.sourceforge.fenixedu.domain.space;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public abstract class SpaceInformation extends SpaceInformation_Base implements Comparable<SpaceInformation> {

    public abstract String getPresentationName();

    public abstract RoomClassification getRoomClassification();

    public abstract FactoryExecutor getSpaceFactoryEditor();

    protected SpaceInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        if (getSpace().getSpaceInformationsSet().size() == 1) {
            throw new DomainException("space.must.have.at.least.one.space.information");
        }
        deleteWithoutCheckNumberOfSpaceInformations();
    }

    public void deleteWithoutCheckNumberOfSpaceInformations() {
        super.setSpace(null);
        setRootDomainObject(null);
        deleteDomainObject();
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

    protected void setFirstTimeInterval(final YearMonthDay begin, final YearMonthDay end) {
        setNewValidUntilDateIfNecessary(begin.minusDays(1));
        editTimeInterval(begin, end);
    }

    protected void editTimeInterval(final YearMonthDay begin, final YearMonthDay end) {
        checkSpaceInformationsIntersection(begin, end);
        super.setValidFrom(begin);
        super.setValidUntil(end);
    }

    @Override
    public void setSpace(Space space) {
        if (space == null) {
            throw new DomainException("error.space.information.no.space");
        }
        super.setSpace(space);
    }

    @Override
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
        checkBeginDateAndEndDate(begin, end);
        for (SpaceInformation information : getSpace().getSpaceInformationsSet()) {
            if (!information.equals(this) && information.spaceInformationsIntersection(begin, end)) {
                throw new DomainException("error.space.information.intersection");
            }
        }
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getValidFrom().isAfter(currentDate) && (this.getValidUntil() == null || !this.getValidUntil().isBefore(
                currentDate)));
    }

    protected YearMonthDay getNextPossibleValidFromDate() {
        SpaceInformation mostRecentSpaceInformation = getSpace().getMostRecentSpaceInformation();
        if (mostRecentSpaceInformation.getValidUntil() != null) {
            return mostRecentSpaceInformation.getValidUntil().plusDays(1);
        } else {
            return mostRecentSpaceInformation.getValidFrom().plusDays(2);
        }
    }

    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
        if (beginDate == null) {
            throw new DomainException("error.contract.no.beginDate");
        }
        if (endDate != null && endDate.isBefore(beginDate)) {
            throw new DomainException("error.begin.after.end");
        }
    }

    private boolean spaceInformationsIntersection(YearMonthDay begin, YearMonthDay end) {
        return ((end == null || !this.getValidFrom().isAfter(end)) && (this.getValidUntil() == null || !this.getValidUntil()
                .isBefore(begin)));
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

    @Deprecated
    public boolean hasEmails() {
        return getEmails() != null;
    }

    @Deprecated
    public boolean hasValidFrom() {
        return getValidFrom() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasValidUntil() {
        return getValidUntil() != null;
    }

    @Deprecated
    public boolean hasBlueprintNumber() {
        return getBlueprintNumber() != null;
    }

    @Deprecated
    public boolean hasCapacity() {
        return getCapacity() != null;
    }

    @Deprecated
    public boolean hasSpace() {
        return getSpace() != null;
    }

}
