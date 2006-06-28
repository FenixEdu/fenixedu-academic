package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;

public class Clocking extends Clocking_Base {

    public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date,
            Integer oracleSequence) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDate(date);
        setAssiduousness(assiduousness);
        setClockUnit(clockUnit);
        setOracleSequence(oracleSequence);
        setOjbConcreteClass(Clocking.class.getName());
    }

    public TimeOfDay getTimeOfDay() {
        return getDate().toTimeOfDay();
    }
}
