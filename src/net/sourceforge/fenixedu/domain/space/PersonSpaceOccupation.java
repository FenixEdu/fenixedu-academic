package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {

    public static final Comparator COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("person.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("idInternal"));
    }

    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin,
	    final YearMonthDay end) {
	super();
	checkParameters(space, person, begin, end);
	setPerson(person);
	setSpace(space);
	//checkPermissionsToMakeOperations();
	super.setBegin(begin);
	super.setEnd(end);
    }

    private void checkParameters(final Space space, final Person person, YearMonthDay begin,
	    YearMonthDay end) {
	
	if (person == null) {
	    throw new DomainException("error.inexistente.person");
	}
	if (space == null) {
	    throw new DomainException("error.inexistente.space");
	}	
	checkPersonSpaceOccupationIntersection(begin, end, person, space);
    }

    @Override
    public void setBegin(YearMonthDay begin) {
	checkPermissionsToMakeOperations();
	checkPersonSpaceOccupationIntersection(begin, getEnd(), getPerson(), getSpace());
	super.setBegin(begin);
    }

    @Override
    public void setEnd(YearMonthDay end) {
	checkPermissionsToMakeOperations();
	checkPersonSpaceOccupationIntersection(getBegin(), end, getPerson(), getSpace());
	super.setEnd(end);
    }

    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkPermissionsToMakeOperations();
	checkPersonSpaceOccupationIntersection(begin, end, getPerson(), getSpace());
	super.setBegin(begin);
	super.setEnd(end);
    }
    
    public void delete() {
	checkPermissionsToMakeOperations();
	removePerson();
	super.delete();
    }

    public boolean contains(YearMonthDay currentDate) {
	return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
		.isBefore(currentDate)));
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getPersonOccupationsAccessGroup();
    }

    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBegin().isAfter(end)) && (this.getEnd() == null || !this
		.getEnd().isBefore(begin)));
    }

    private void checkPermissionsToMakeOperations() {
	if (getAccessGroup() == null
		|| !getAccessGroup().isMember(AccessControl.getUserView().getPerson())) {
	    throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	}
    }

    private void checkEndDate(final YearMonthDay begin, final YearMonthDay end) {
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }

    private void checkPersonSpaceOccupationIntersection(final YearMonthDay begin,
	    final YearMonthDay end, Person person, Space space) {
	
	checkEndDate(begin, end);
	List<PersonSpaceOccupation> personSpaceOccupations = person.getPersonSpaceOccupations();
	for (PersonSpaceOccupation personSpaceOccupation : personSpaceOccupations) {
	    if (!personSpaceOccupation.equals(this) && personSpaceOccupation.getSpace().equals(space)
		    && personSpaceOccupation.checkIntersections(begin, end)) {
		throw new DomainException("error.person.space.occupation.intersection");
	    }
	}
    }
}
