package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SpaceResponsability extends SpaceResponsability_Base {
    
    public SpaceResponsability(final Unit unit, final YearMonthDay begin, final YearMonthDay end) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
