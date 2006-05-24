package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class Clocking extends Clocking_Base {

    public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDate(date);
        setAssiduousness(assiduousness);
        setClockUnit(clockUnit);
        setOjbConcreteClass(Clocking.class.getName());
    }
}
