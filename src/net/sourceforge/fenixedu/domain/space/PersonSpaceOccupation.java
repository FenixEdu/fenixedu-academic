package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Person;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {
    
    public static final Comparator PERSON_SPACE_OCCUPATION_COMPARATOR_BY_PERSON_NAME = new BeanComparator("person.name", Collator.getInstance());
    
    public PersonSpaceOccupation() {
        super();
    }

    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin, final YearMonthDay end) {
        this();
        setSpace(space);
        setPerson(person);
        setBegin(begin);
        setEnd(end);
    }
    
    public boolean isActive(YearMonthDay currentDate) {
        return (this.getEnd() == null || !this.getEnd().isBefore(currentDate));
    }
}
