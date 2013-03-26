package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PersonSpaceOccupation extends PersonSpaceOccupation_Base {

    public static final Comparator<PersonSpaceOccupation> COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL =
            new Comparator<PersonSpaceOccupation>() {
                @Override
                public int compare(final PersonSpaceOccupation o1, final PersonSpaceOccupation o2) {
                    final int b = compare(o1.getBegin(), o2.getBegin());
                    if (b != 0) {
                        return b;
                    }
                    final int n = compare(o1.getPerson(), o2.getPerson());
                    return n == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : n;
                }

                private int compare(final Person p1, final Person p2) {
                    return Collator.getInstance().compare(p1.getName(), p2.getName());
                }

                private int compare(YearMonthDay y1, YearMonthDay y2) {
                    if (y1 == null && y2 == null) {
                        return 0;
                    }
                    if (y1 == null) {
                        return -1;
                    }
                    return y2 == null ? 1 : y1.compareTo(y2);
                }
            };

    @Checked("SpacePredicates.checkPermissionsToManagePersonSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created person occupation", parameters = { "space", "person", "begin",
            "end" })
    public PersonSpaceOccupation(final Space space, final Person person, final YearMonthDay begin, final YearMonthDay end) {

        super();
        setResource(space);
        setPerson(person);
        checkPersonSpaceOccupationIntersection(begin, end, person, space);
        super.setBegin(begin);
        super.setEnd(end);
    }

    @Checked("SpacePredicates.checkPermissionsToManagePersonSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited person occupation", parameters = { "begin", "end" })
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
        checkPersonSpaceOccupationIntersection(begin, end, getPerson(), getSpace());
        super.setBegin(begin);
        super.setEnd(end);
    }

    @Override
    @Checked("SpacePredicates.checkPermissionsToManagePersonSpaceOccupations")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted person occupation", parameters = {})
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
        return (getBegin() == null || !getBegin().isAfter(currentDate)) && (getEnd() == null || !getEnd().isBefore(currentDate));
    }

    public Unit getPersonWorkingPlace() {
        return (getPerson().getEmployee() != null) ? getPerson().getEmployee().getLastWorkingPlace(getBegin(), getEnd()) : null;
    }

    @Override
    public Group getAccessGroup() {
        return getSpace().getPersonOccupationsAccessGroupWithChainOfResponsibility();
    }

    public void checkPersonSpaceOccupationIntersection(final YearMonthDay begin, final YearMonthDay end, Person person,
            Space space) {

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
