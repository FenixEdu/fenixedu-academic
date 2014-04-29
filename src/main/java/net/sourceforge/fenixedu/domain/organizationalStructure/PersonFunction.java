package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.PersonFunctionSender;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class PersonFunction extends PersonFunction_Base {

    public final static Comparator<PersonFunction> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();

    public final static Comparator<PersonFunction> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();

    static {
        ((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"));
        ((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name"));
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
    }

    public PersonFunction(Party parentParty, Party childParty, Function function, YearMonthDay begin, YearMonthDay end,
            Double credits) {
        super();
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(function);
        setCredits(credits);
        setOccupationInterval(begin, end);
    }

    public PersonFunction(Party parentParty, Party childParty, Function function, ExecutionInterval executionInterval,
            Double credits) {
        super();
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(function);
        setCredits(credits);
        setOccupationInterval(executionInterval);
    }

    public PersonFunction(Party parentParty, Party childParty, Function function, YearMonthDay begin, YearMonthDay end) {
        this(parentParty, childParty, function, begin, end, 0.0);
    }

    public PersonFunction(Person parentParty, Person childParty, Function function, YearMonthDay begin, YearMonthDay end) {
        super();
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(function);
        setCredits(0.0);
        setOccupationInterval(begin, end);
    }

    protected PersonFunction() {
        super();
    }

    @Override
    public YearMonthDay getBeginDate() {
        return getExecutionInterval() != null ? getExecutionInterval().getBeginDateYearMonthDay() : super.getBeginDate();
    }

    @Override
    public YearMonthDay getEndDate() {
        return getExecutionInterval() != null ? getExecutionInterval().getEndDateYearMonthDay() : super.getEndDate();
    }

    @Override
    protected boolean checkDateInterval() {
        return getExecutionInterval() != null ? true : super.checkDateInterval();
    }

    public void edit(YearMonthDay begin, YearMonthDay end, Double credits) {
        setCredits(credits);
        setOccupationInterval(begin, end);
    }

    @Override
    public void setChildParty(Party childParty) {
        if (childParty == null || !childParty.isPerson()) {
            throw new DomainException("error.invalid.child.party");
        }
        super.setChildParty(childParty);
    }

    @Override
    public void setParentParty(Party parentParty) {
        if (parentParty == null || !parentParty.isUnit()) {
            throw new DomainException("error.invalid.parent.party");
        }
        super.setParentParty(parentParty);
    }

    public void setParentParty(Person parentParty) {
        if (parentParty == null) {
            throw new DomainException("error.invalid.parent.party");
        }
        super.setParentParty(parentParty);
    }

    public void setOccupationInterval(ExecutionInterval executionInterval) {
        checkPersonFunctionDatesIntersection(getPerson(), getUnit(), getFunction(), executionInterval.getBeginDateYearMonthDay(),
                executionInterval.getEndDateYearMonthDay());
        super.setExecutionInterval(executionInterval);
    }

    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
        checkPersonFunctionDatesIntersection(getPerson(), getUnit(), getFunction(), beginDate, endDate);
        super.setBeginDate(beginDate);
        super.setEndDate(endDate);
    }

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
        throw new DomainException("error.invalid.operation");
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
        throw new DomainException("error.invalid.operation");
    }

    @Override
    public Double getCredits() {
        if (super.getCredits() == null) {
            return 0d;
        }
        return super.getCredits();
    }

    public Person getPerson() {
        return (Person) getChildParty();
    }

    public Unit getUnit() {
        return getParentParty() instanceof Unit ? (Unit) getParentParty() : null;
    }

    public Function getFunction() {
        return (Function) getAccountabilityType();
    }

    @Override
    public boolean isPersonFunction() {
        return true;
    }

    private void checkPersonFunctionDatesIntersection(Person person, Unit unit, Function function, YearMonthDay begin,
            YearMonthDay end) {
        checkBeginDateAndEndDate(begin, end);
        for (PersonFunction personFunction : person.getPersonFunctions(unit)) {
            if (!personFunction.equals(this) && personFunction.getFunction().equals(function)
                    && personFunction.checkDatesIntersections(begin, end)) {
                throw new DomainException("error.personFunction.dates.intersection.for.same.function");
            }
        }
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
        return ((end == null || !getBeginDate().isAfter(end)) && (getEndDate() == null || !getEndDate().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
        if (begin == null) {
            throw new DomainException("error.personFunction.no.beginDate");
        }
        if (end == null) {
            throw new DomainException("error.personFunction.no.endDate");
        }
        if (end != null && begin.isAfter(end)) {
            throw new DomainException("error.personFunction.endDateBeforeBeginDate");
        }
    }

    public boolean hasCredits() {
        return getCredits() > 0d;
    }

    public static PersonFunction createYearDelegatePersonFunction(DegreeUnit unit, Person person, YearMonthDay startDate,
            YearMonthDay endDate, Function function, CurricularYear curricularYear) {
        if (function == null) {
            throw new DomainException("error.delegates.noDelegateFunction");
        }
        PersonFunction personFunction = new PersonFunction(unit, person, function, startDate, endDate);
        personFunction.setCurricularYear(curricularYear);
        new PersonFunctionSender(personFunction);
        return personFunction;
    }

    public static void createDelegatePersonFunction(Unit unit, Person person, YearMonthDay startDate, YearMonthDay endDate,
            Function function) {
        if (function == null) {
            throw new DomainException("error.delegates.noDelegateFunction");
        }
        PersonFunction personFunction = new PersonFunction(unit, person, function, startDate, endDate);
        new PersonFunctionSender(personFunction);
    }

    @Override
    @Atomic
    public void delete() {
        if (hasCurricularYear()) {
            setCurricularYear(null);
        }
        if (hasDelegate()) {
            setDelegate(null);
        }
        if (hasSender()) {
            setSender(null);
        }
        if (!getDelegateStudentsGroupSet().isEmpty()) {
            throw new DomainException("error.personFunction.cannotDeletePersonFunctionUsedInAccessControl");
        }
        setExecutionInterval(null);
        super.delete();
    }

    public boolean getCanBeEditedByDepartmentAdministrativeOffice() {
        ExecutionSemester executionSemester = ExecutionSemester.readByYearMonthDay(getBeginDate());
        if (isPersonFunctionShared() && executionSemester.isInValidCreditsPeriod(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
            List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
            units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEGREE_UNIT));
            units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
            for (Iterator<Unit> iterator = units.iterator(); iterator.hasNext();) {
                Unit unit = iterator.next();
                if (unit.getUnitName().getIsExternalUnit()) {
                    iterator.remove();
                }
            }
            return units.contains(getUnit());
        }
        return false;
    }

    @Deprecated
    public boolean hasSender() {
        return getSender() != null;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

    @Deprecated
    public boolean hasExecutionInterval() {
        return getExecutionInterval() != null;
    }

    @Deprecated
    public boolean hasDelegate() {
        return getDelegate() != null;
    }

}
