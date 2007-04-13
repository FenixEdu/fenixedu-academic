package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class SpaceResponsibility extends SpaceResponsibility_Base {

    public static final Comparator<SpaceResponsibility> COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL).addComparator(new BeanComparator("unit.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL).addComparator(new BeanComparator("idInternal"));
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created space responsibility", parameters = {
	    "space", "unit", "begin", "end" })
    public SpaceResponsibility(final Space space, final Unit unit, final YearMonthDay begin,
	    final YearMonthDay end) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setSpace(space);
	setUnit(unit);
	checkSpaceResponsabilityIntersection(begin, end, getUnit(), getSpace());
	super.setBegin(begin);
	super.setEnd(end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited space responsibility", parameters = {
	    "begin", "end" })
    public void setSpaceResponsibilityInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkSpaceResponsabilityIntersection(begin, end, getUnit(), getSpace());
	super.setBegin(begin);
	super.setEnd(end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted space responsibility", parameters = {})
    public void delete() {
	super.setSpace(null);
	super.setUnit(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    @Override
    public void setBegin(YearMonthDay begin) {
	checkSpaceResponsabilityIntersection(begin, getEnd(), getUnit(), getSpace());
	super.setBegin(begin);
    }

    @Override
    public void setEnd(YearMonthDay end) {
	checkSpaceResponsabilityIntersection(getBegin(), end, getUnit(), getSpace());
	super.setEnd(end);
    }

    @Override
    public void setUnit(Unit unit) {
	if (unit == null) {
	    throw new DomainException("error.space.responsability.no.unit");
	}
	if(unit.isAggregateUnit()) {
	    throw new DomainException("error.space.aggregate.unit");
	}
	super.setUnit(unit);
    }

    @Override
    public void setSpace(Space space) {
	if (space == null) {
	    throw new DomainException("error.space.responsability.no.space");
	}
	super.setSpace(space);
    }

    public boolean isActive(YearMonthDay currentDate) {
	return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
		.isBefore(currentDate)));
    }

    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBegin().isAfter(end)) && (this.getEnd() == null || !this
		.getEnd().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.spaceResponsability.no.begin");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.begin.after.end");
	}
    }

    private void checkSpaceResponsabilityIntersection(final YearMonthDay begin, final YearMonthDay end,
	    Unit unit, Space space) {

	checkBeginDateAndEndDate(begin, end);
	for (SpaceResponsibility spaceResponsibility : space.getSpaceResponsibility()) {
	    if (!spaceResponsibility.equals(this) && spaceResponsibility.getUnit().equals(unit)
		    && spaceResponsibility.checkIntersections(begin, end)) {
		throw new DomainException("error.spaceResponsibility.unit.intersection");
	    }
	}
    }
}
