package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class SpaceResponsibility extends SpaceResponsibility_Base {
    
    public static final Comparator COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL = new  ComparatorChain();
    static {
        ((ComparatorChain)COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL).addComparator(new BeanComparator("begin"));
        ((ComparatorChain)COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL).addComparator(new BeanComparator("unit.name", Collator.getInstance()));               
    }   
    
    public SpaceResponsibility(final Space space, final Unit unit, final YearMonthDay begin,
            final YearMonthDay end) {

        super();
        checkParameters(space, unit, begin, end);
        setSpace(space);
        setUnit(unit);
        setBegin(begin);
        setEnd(end);
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete() {
        removeSpace();
        removeUnit();
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    public void setSpaceResponsibilityInterval(final YearMonthDay begin, final YearMonthDay end) {               
        checkSpaceResponsabilityIntersection(begin, end, getUnit(), getSpace());
        super.setBegin(begin);
        super.setEnd(end);
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
    
    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this
                .getEnd().isBefore(currentDate)));
    }
    
    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
        return ((end == null && !begin.isAfter(this.getBegin())) || (end != null && 
                (!this.getBegin().isAfter(end) && (this.getEnd() == null || !this.getEnd().isBefore(begin)))));
    }    

    private void checkParameters(Space space, Unit unit, YearMonthDay begin, YearMonthDay end) {
        if (space == null) {
            throw new DomainException("error.spaceResponsability.no.space");
        }
        if (unit == null) {
            throw new DomainException("error.spaceResponsability.no.unit");
        }
        if (begin == null) {
            throw new DomainException("error.spaceResponsability.no.begin");
        }
        checkSpaceResponsabilityIntersection(begin, end, unit, space);
    }
    
    private void checkEndDate(final YearMonthDay begin, final YearMonthDay end) {
        if(end != null && !end.isAfter(begin)) {
            throw new DomainException("error.begin.after.end");
        }
    }    
    
    private void checkSpaceResponsabilityIntersection(final YearMonthDay begin, final YearMonthDay end, Unit unit, Space space) {
        checkEndDate(begin, end);   
        List<SpaceResponsibility> spaceResponsibilities = space.getSpaceResponsibility();         
        for (SpaceResponsibility spaceResponsibility : spaceResponsibilities) {
            if(!spaceResponsibility.equals(this) &&
                    spaceResponsibility.getUnit().equals(unit) && spaceResponsibility.checkIntersections(begin, end)) {
                throw new DomainException("error.spaceResponsibility.unit.intersection");
            }
        }
    }
}
