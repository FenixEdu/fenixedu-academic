/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
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
        for (PersonFunction personFunction : PersonFunction.getPersonFunctions(person, unit, false, null, null)) {
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

    @Override
    @Atomic
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        if (getCurricularYear() != null) {
            setCurricularYear(null);
        }
        setExecutionInterval(null);
        super.delete();
    }

    public static List<Function> getActiveInherentPersonFunctions(Person person) {
        final List<Function> inherentFunctions = new ArrayList<Function>();
        for (final PersonFunction accountability : getActivePersonFunctions(person)) {
            inherentFunctions.addAll(accountability.getFunction().getInherentFunctionsSet());
        }
        return inherentFunctions;
    }

    public static List<PersonFunction> getManagementFunctions(Teacher teacher, ExecutionSemester executionSemester) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : (Collection<PersonFunction>) teacher.getPerson().getParentAccountabilities(
                AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)) {
            if (personFunction.belongsToPeriod(executionSemester.getBeginDateYearMonthDay(),
                    executionSemester.getEndDateYearMonthDay())) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }

    public static List<PersonFunction> getPersonFunctions(Person person, Unit unit, boolean includeSubUnits, Boolean active,
            Boolean virtual, AccountabilityTypeEnum accountabilityTypeEnum) {
        final List<PersonFunction> result = new ArrayList<PersonFunction>();

        Collection<Unit> allSubUnits = Collections.emptyList();
        if (includeSubUnits) {
            allSubUnits = unit.getAllSubUnits();
        }

        final YearMonthDay today = new YearMonthDay();
        final AccountabilityTypeEnum accountabilityTypeEnum1 = accountabilityTypeEnum;

        for (final PersonFunction personFunction : PersonFunction.getPersonFunctions(person, accountabilityTypeEnum1)) {
            if (active != null && personFunction.isActive(today) == !active) {
                continue;
            }

            if (virtual != null && personFunction.getFunction().isVirtual() == !virtual) {
                continue;
            }

            final Unit functionUnit = personFunction.getUnit();
            if (unit == null || functionUnit.equals(unit) || includeSubUnits && allSubUnits.contains(functionUnit)) {
                result.add(personFunction);
            }
        }

        return result;
    }

    public static List<PersonFunction> getPersonFunctions(Person person, Party party, boolean includeSubUnits, Boolean active,
            Boolean virtual, AccountabilityTypeEnum accountabilityTypeEnum) {
        if (party.isUnit()) {
            return PersonFunction.getPersonFunctions(person, (Unit) party, includeSubUnits, active, virtual,
                    AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
        }
        final List<PersonFunction> result = new ArrayList<PersonFunction>();

        final YearMonthDay today = new YearMonthDay();
        for (final PersonFunction personFunction : PersonFunction.getPersonFunctions(person, accountabilityTypeEnum)) {
            if (active != null && personFunction.isActive(today) == !active) {
                continue;
            }
            if (virtual != null && personFunction.getFunction().isVirtual() == !virtual) {
                continue;
            }
            if (personFunction.getParentParty().isPerson()) {
                final Person functionPerson = (Person) personFunction.getParentParty();
                if (party == null || functionPerson.equals(party)) {
                    result.add(personFunction);
                }
            }
        }

        return result;
    }

    public static List<PersonFunction> getPersonFuntions(Person person, AccountabilityTypeEnum accountabilityTypeEnum,
            YearMonthDay begin, YearMonthDay end) {
        final List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (final Accountability accountability : (Collection<PersonFunction>) person.getParentAccountabilities(
                accountabilityTypeEnum, PersonFunction.class)) {
            if (accountability.belongsToPeriod(begin, end)) {
                result.add((PersonFunction) accountability);
            }
        }
        return result;
    }

    public static List<PersonFunction> getActivePersonFunctions(Person person) {
        return PersonFunction.getPersonFunctions(person, null, false, true, false);
    }

    public static List<PersonFunction> getInactivePersonFunctions(Person person) {
        return PersonFunction.getPersonFunctions(person, null, false, false, false);
    }

    /**
     * The main difference between this method and {@link #getActivePersonFunctions()} is that person functions with a virtual
     * function are also included. This method also collects person functions from the given unit and all subunits.
     * 
     * @see Function#isVirtual()
     */
    public static List<PersonFunction> getAllActivePersonFunctions(Person person, Unit unit) {
        return PersonFunction.getPersonFunctions(person, unit, true, true, null);
    }

    public static Collection<PersonFunction> getPersonFunctions(Person person, Function function) {
        final Collection<PersonFunction> personFunctions =
                (Collection<PersonFunction>) person.getParentAccountabilities(AccountabilityTypeEnum.MANAGEMENT_FUNCTION,
                        PersonFunction.class);
        final Iterator<PersonFunction> iterator = personFunctions.iterator();

        while (iterator.hasNext()) {
            final PersonFunction element = iterator.next();
            if (element.getFunction() == function) {
                continue;
            }
            iterator.remove();
        }

        return personFunctions;
    }

    public static List<PersonFunction> getPersonFuntions(Person person, YearMonthDay begin, YearMonthDay end) {
        return PersonFunction.getPersonFuntions(person, AccountabilityTypeEnum.MANAGEMENT_FUNCTION, begin, end);
    }

    public static List<PersonFunction> getPersonFunctions(Person person, Unit unit, boolean includeSubUnits, Boolean active,
            Boolean virtual) {
        return PersonFunction.getPersonFunctions(person, unit, includeSubUnits, active, virtual,
                AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
    }

    public static Collection<PersonFunction> getPersonFunctions(Person person, AccountabilityTypeEnum accountabilityTypeEnum) {
        return (Collection<PersonFunction>) person.getParentAccountabilities(accountabilityTypeEnum, PersonFunction.class);
    }

    public static PersonFunction getActiveGGAEDelegatePersonFunction(Person person) {
        for (final PersonFunction personFunction : PersonFunction.getActivePersonFunctions(person)) {
            if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
                return personFunction;
            }
        }
        return null;
    }

    public static List<PersonFunction> getAllGGAEDelegatePersonFunctions(Person person) {
        final List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (final PersonFunction personFunction : (Collection<PersonFunction>) person.getParentAccountabilities(
                AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)) {
            if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
                result.add(personFunction);
            }
        }
        return result;
    }

    public static Person getActiveUnitCoordinator(Unit unit) {
        return PersonFunction.getActiveUnitCoordinator(unit, new YearMonthDay());
    }

    public static Person getActiveUnitCoordinator(Unit unit, YearMonthDay yearMonthDay) {
        for (final Accountability accountability : unit.getUnitCoordinatorFunction().getAccountabilitiesSet()) {
            if (accountability.isPersonFunction() && accountability.isActive(yearMonthDay)) {
                return ((PersonFunction) accountability).getPerson();
            }
        }

        return null;
    }

    public static List<PersonFunction> getPersonFunctions(Function function) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (Accountability accountability : function.getAccountabilitiesSet()) {
            if (accountability.isPersonFunction()) {
                personFunctions.add((PersonFunction) accountability);
            }
        }
        return personFunctions;
    }

    public static List<PersonFunction> getActivePersonFunctions(Function function) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        YearMonthDay currentDate = new YearMonthDay();
        for (Accountability accountability : function.getAccountabilitiesSet()) {
            if (accountability.isPersonFunction() && accountability.isActive(currentDate)) {
                personFunctions.add((PersonFunction) accountability);
            }
        }
        return personFunctions;
    }

    public static List<PersonFunction> getActivePersonFunctionsByPerson(Function function, Person person) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        YearMonthDay currentDate = new YearMonthDay();
        for (Accountability accountability : function.getAccountabilitiesSet()) {
            if (accountability.isPersonFunction() && accountability.isActive(currentDate)) {
                PersonFunction personFunction = (PersonFunction) accountability;
                if (personFunction.getPerson().equals(person)) {
                    personFunctions.add(personFunction);
                }
            }
        }
        return personFunctions;
    }

    public static List<PersonFunction> getActivePersonFunctionsStartingIn(Function function, ExecutionYear executionYear) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (Accountability accountability : function.getAccountabilitiesSet()) {
            if (accountability.isPersonFunction()) {
                if (accountability.getBeginDate().isBefore(executionYear.getEndDateYearMonthDay())
                        && (accountability.getEndDate() == null || accountability.getEndDate().isAfter(
                                executionYear.getBeginDateYearMonthDay()))) {
                    personFunctions.add((PersonFunction) accountability);
                }
            }
        }
        return personFunctions;
    }
}
