package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceResponsibility;
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
	((ComparatorChain) COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageResponsabilityUnits")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created space responsibility", parameters = {
	    "space", "unit", "begin", "end" })
    public SpaceResponsibility(final Space space, final Unit unit, final YearMonthDay begin,
	    final YearMonthDay end) {

	super();	
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
	super.delete();
    }

    @Override
    public boolean isSpaceResponsibility() {
        return true;
    }
    
    @Override
    public void setBegin(YearMonthDay begin) {
	throw new DomainException("error.invalid.operation");
    }

    @Override
    public void setEnd(YearMonthDay end) {
	throw new DomainException("error.invalid.operation");
    }

    @Override
    public void setResource(Resource resource) {
        if(resource == null || !resource.isSpace()) {
            throw new DomainException("error.space.responsability.no.space");
        }
	super.setResource(resource);
    }
    
    @Override
    public void setParty(Party party) {
        if(party == null || !party.isUnit() || party.isAggregateUnit()) {
            throw new DomainException("error.space.responsability.no.unit");
        }
	super.setParty(party);
    }
    
    public void setUnit(Unit unit) {
	setParty(unit);
    }

    public Unit getUnit() {
	return (Unit) getParty();
    }
    
    public void setSpace(Space space) {
	setResource(space);
    }
    
    public Space getSpace() {
	return (Space) getResource();
    }
  
    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !getBegin().isAfter(end)) && (getEnd() == null || !getEnd().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.spaceResponsability.no.begin");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.begin.after.end");
	}
    }

    private void checkSpaceResponsabilityIntersection(final YearMonthDay begin, final YearMonthDay end, Unit unit, Space space) {
	checkBeginDateAndEndDate(begin, end);
	for (ResourceResponsibility resourceResponsibility : space.getResourceResponsibility()) {
	    if (resourceResponsibility.isSpaceResponsibility() &&
		    !resourceResponsibility.equals(this) && resourceResponsibility.getParty().equals(unit)
		    && ((SpaceResponsibility)resourceResponsibility).checkIntersections(begin, end)) {
		throw new DomainException("error.spaceResponsibility.unit.intersection");
	    }
	}
    }
}
