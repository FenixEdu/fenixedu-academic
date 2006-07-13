package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {
    
    public static final Comparator COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL = new  ComparatorChain();
    static {
        ((ComparatorChain)COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("person.name", Collator.getInstance()));
        ((ComparatorChain)COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("begin"));
        ((ComparatorChain)COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("end"));
    }            
    
    public PersonSpaceOccupation() {
        super();
    }    
    
    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin, final YearMonthDay end) {
        this();                 
        checkParameters(space, person, begin, end);
        setPerson(person);
        setSpace(space);
        super.setBegin(begin);
        super.setEnd(end);
    }

    private void checkParameters(final Space space, final Person person, YearMonthDay begin, YearMonthDay end) {
        if(person == null) {
            throw new DomainException("error.inexistente.person");
        }
        if(space == null) {
            throw new DomainException("error.inexistente.space");
        }
        checkPersonSpaceOccupationIntersection(begin, end, person, space);
    }

    @Override
    public void setBegin(YearMonthDay begin) {
        checkPersonSpaceOccupationIntersection(begin, getEnd(), getPerson(), getSpace());   
        super.setBegin(begin);
    }

    @Override
    public void setEnd(YearMonthDay end) {
        checkPersonSpaceOccupationIntersection(getBegin(), end, getPerson(), getSpace());
        super.setEnd(end);
    }

    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {               
        checkPersonSpaceOccupationIntersection(begin, end, getPerson(), getSpace());
        super.setBegin(begin);
        super.setEnd(end);
    }
    
    public void delete() {
        removePerson();
        removeSpace();
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    private void checkPersonSpaceOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
        if(end != null && !end.isAfter(begin)) {
            throw new DomainException("error.begin.after.end");
        }
    }
    
    private void checkPersonSpaceOccupationIntersection(final YearMonthDay begin, final YearMonthDay end, Person person, Space space) {
        checkPersonSpaceOccupationInterval(begin, end);   
        List<PersonSpaceOccupation> personSpaceOccupations = person.getPersonSpaceOccupations();        
        for (PersonSpaceOccupation personSpaceOccupation : personSpaceOccupations) {
            if(!personSpaceOccupation.equals(this) &&
                    personSpaceOccupation.getSpace().equals(space) && personSpaceOccupation.intersect(begin, end)) {
                throw new DomainException("error.person.space.occupation.intersection");
            }
        }
    }

    public boolean contains(YearMonthDay currentDate) {
        return (!this.getBegin().isBefore(currentDate) && 
                (this.getEnd() == null || !this.getEnd().isBefore(currentDate)));
    }
    
    private boolean intersect(YearMonthDay begin, YearMonthDay end) {
        return ((end == null && !begin.isAfter(this.getBegin())) || (end != null && 
                (!this.getBegin().isAfter(end) && (this.getEnd() == null || !this.getEnd().isBefore(begin)))));
    }     
}
