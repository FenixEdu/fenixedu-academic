package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;

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
        checkParameters(space, unit, begin);
        setSpace(space);
        setUnit(unit);
        setBegin(begin);
        setEnd(end);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    private void checkParameters(Space space, Unit unit, YearMonthDay begin) {
        if (space == null) {
            throw new DomainException("error.spaceResponsability.no.space");
        }
        if (unit == null) {
            throw new DomainException("error.spaceResponsability.no.unit");
        }
        if (begin == null) {
            throw new DomainException("error.spaceResponsability.no.begin");
        }
    }
}
