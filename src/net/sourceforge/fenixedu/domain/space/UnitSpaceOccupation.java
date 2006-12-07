package net.sourceforge.fenixedu.domain.space;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class UnitSpaceOccupation extends UnitSpaceOccupation_Base {

    public final static Comparator<UnitSpaceOccupation> COMPARATOR_BY_OCCUPATION_INTERVAL_AND_UNIT = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_OCCUPATION_INTERVAL_AND_UNIT).addComparator(new BeanComparator(
		"begin"));
	((ComparatorChain) COMPARATOR_BY_OCCUPATION_INTERVAL_AND_UNIT).addComparator(new BeanComparator(
		"unit.name"));
	((ComparatorChain) COMPARATOR_BY_OCCUPATION_INTERVAL_AND_UNIT).addComparator(new BeanComparator(
		"idInternal"));
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageUnitSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created unit space occupation", parameters = {
	    "space", "unit", "begin", "end" })
    public UnitSpaceOccupation(Unit unit, Space space, YearMonthDay begin, YearMonthDay end) {
	super();
	setSpace(space);
	setUnit(unit);
	super.setBegin(begin);
	super.setEnd(end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageUnitSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted unit space occupation", parameters = {})
    public void delete() {
	super.setUnit(null);
	super.delete();
    }
    
    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageUnitSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited unit space occupation", parameters = {
	    "begin", "end" })
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkUnitSpaceOccupationIntersection(begin, end, getSpace(), getUnit());
	super.setBegin(begin);
	super.setEnd(end);
    }

    @Override
    public void setUnit(Unit unit) {
	if (unit == null) {
	    throw new DomainException("error.unitSpaceOccupation.empty.unit");
	}
	super.setUnit(unit);
    }

    @Override
    public Group getAccessGroup() {
	return null;
    }

    @Override
    public void setBegin(YearMonthDay begin) {
	checkUnitSpaceOccupationIntersection(begin, getEnd(), getSpace(), getUnit());
	super.setBegin(begin);
    }

    @Override
    public void setEnd(YearMonthDay end) {
	checkUnitSpaceOccupationIntersection(getBegin(), end, getSpace(), getUnit());
	super.setEnd(end);
    }
    
    public boolean isActive(YearMonthDay currentDate) {
	return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
		.isBefore(currentDate)));
    }

    private void checkUnitSpaceOccupationIntersection(YearMonthDay begin, YearMonthDay end, Space space,
	    Unit unit) {
	checkBeginDateAndEndDate(begin, end);
	for (UnitSpaceOccupation unitSpaceOccupation : space.getUnitSpaceOccupations()) {
	    if (!unitSpaceOccupation.equals(this) && unitSpaceOccupation.getUnit().equals(unit)
		    && unitSpaceOccupation.occupationsIntersection(begin, end)) {
		throw new DomainException("error.unitSpaceOccupation.intersection");
	    }
	}
    }

    private boolean occupationsIntersection(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBegin().isAfter(end)) && (this.getEnd() == null || !this
		.getEnd().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
	if (begin == null) {
	    throw new DomainException("error.unitSpaceOccupation.no.beginDate");
	}
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }
}
