package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {

    public static final Comparator<PersonSpaceOccupation> COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(new BeanComparator("person.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    @Checked("SpacePredicates.checkPermissionsToManagePersonSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(
         actionName="Created person occupation", 
	 parameters={"space","person","begin","end"}
    )
    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin,
	    final YearMonthDay end) {
	
	super();
	setResource(space);
	setPerson(person);
	checkPersonSpaceOccupationIntersection(begin, end, person, space);
	super.setBegin(begin);
	super.setEnd(end);
    }
    
    @Checked("SpacePredicates.checkPermissionsToManagePersonSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(
         actionName="Edited person occupation", 
	 parameters={"begin","end"}
    )
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkPersonSpaceOccupationIntersection(begin, end, getPerson(), getSpace());
	super.setBegin(begin);
	super.setEnd(end);
    }
    
    @Checked("SpacePredicates.checkPermissionsToManagePersonSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(
	 actionName="Deleted person occupation", 
         parameters={}
    )
    public void delete() {
	super.setPerson(null);
	super.delete();
    }
      
    @Override
    public boolean isPersonSpaceOccupation() {
        return true;
    }
    
    @Override       
    public void setPerson(Person person) {
	if (person == null) {
	    throw new DomainException("error.inexistente.person");
	}
	super.setPerson(person);
    }

    @Override      
    public void setBegin(YearMonthDay begin) {
	 throw new DomainException("error.invalid.operation");
    }

    @Override     
    public void setEnd(YearMonthDay end) {
	 throw new DomainException("error.invalid.operation");
    }
    
    public boolean contains(YearMonthDay currentDate) {
	return (!getBegin().isAfter(currentDate) && (getEnd() == null || !getEnd().isBefore(currentDate)));
    }

    public Unit getPersonWorkingPlace() {
	return (getPerson().getEmployee() != null) ? getPerson().getEmployee().getLastWorkingPlace(getBegin(), getEnd()) : null;
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
	return ((end == null || !getBegin().isAfter(end)) && (getEnd() == null || !getEnd().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
	if (begin == null) {
	    throw new DomainException("error.personSpaceOccupation.no.beginDate");
	}
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBegin();
	final YearMonthDay end = getEnd();	
	return start != null && (end == null || end.isAfter(start));
    }
}
