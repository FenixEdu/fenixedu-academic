package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {

    public static final Comparator COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL)
		.addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL)
		.addComparator(new BeanComparator("person.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL)
		.addComparator(new BeanComparator("idInternal"));
    }

    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin,
	    final YearMonthDay end) {
	super();	
	setPerson(person);
	setSpace(space);
	setOccupationInterval(begin, end);
    }
    
    @Override  
    @Checked("SpacePredicates.checkPermissionsToManageOccupations")
    public void setPerson(Person person) {
	if (person == null) {
	    throw new DomainException("error.inexistente.person");
	}
	super.setPerson(person);
    }

    @Override
    @Checked("SpacePredicates.checkPermissionsToManageOccupations")
    public void setBegin(YearMonthDay begin) {
	checkPersonSpaceOccupationIntersection(begin, getEnd(), getPerson(), getSpace());
	super.setBegin(begin);
    }

    @Override
    @Checked("SpacePredicates.checkPermissionsToManageOccupations")
    public void setEnd(YearMonthDay end) {
	checkPersonSpaceOccupationIntersection(getBegin(), end, getPerson(), getSpace());
	super.setEnd(end);
    }

    @Checked("SpacePredicates.checkPermissionsToManageOccupations")
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkPersonSpaceOccupationIntersection(begin, end, getPerson(), getSpace());
	super.setBegin(begin);
	super.setEnd(end);
    }
    
    public void delete() {
	super.setPerson(null);
	super.delete();
    }

    public boolean contains(YearMonthDay currentDate) {
	return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
		.isBefore(currentDate)));
    }

    public Unit getPersonWorkingPlace() {
	return (getPerson().getEmployee() != null) ? getPerson().getEmployee().getLastWorkingPlace(
		getBegin(), getEnd()) : null;
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getPersonOccupationsAccessGroupWithChainOfResponsibility();
    }    

    public void checkPersonSpaceOccupationIntersection(final YearMonthDay begin, final YearMonthDay end,
	    Person person, Space space) {

	checkBeginDateAndEndDate(begin, end);
	List<PersonSpaceOccupation> personSpaceOccupations = person.getPersonSpaceOccupations();
	for (PersonSpaceOccupation personSpaceOccupation : personSpaceOccupations) {
	    if (!personSpaceOccupation.equals(this) && personSpaceOccupation.getSpace().equals(space)
		    && personSpaceOccupation.occupationsIntersection(begin, end)) {
		throw new DomainException("error.person.space.occupation.intersection");
	    }
	}
    }

    private boolean occupationsIntersection(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBegin().isAfter(end)) && (this.getEnd() == null || !this
		.getEnd().isBefore(begin)));
    }
    
    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
	if (begin == null) {
	    throw new DomainException("error.personSpaceOccupation.no.beginDate");
	}
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }
}
