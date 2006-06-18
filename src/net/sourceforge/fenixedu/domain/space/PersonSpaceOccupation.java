package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Person;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {
    
    protected PersonSpaceOccupation() {
        super();
    }

    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin, final YearMonthDay end) {
        this();
        setSpace(space);
        setPerson(person);
        setBegin(begin);
        setEnd(end);
    }

}
