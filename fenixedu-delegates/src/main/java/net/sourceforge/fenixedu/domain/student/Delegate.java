/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.DegreeUnit;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PedagogicalCouncilUnit;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.util.email.PersonFunctionSender;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class Delegate extends Delegate_Base {

    public Delegate() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isActiveForExecutionYear(final ExecutionYear executionYear) {
        return getDelegateFunction() != null
                && getDelegateFunction().belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay());
    }

    public boolean isActiveForFirstExecutionYear(final ExecutionYear executionYear) {
        if (getDelegateFunction() != null && getDelegateFunction().getBeginDate() != null) {
            Interval interval =
                    new Interval(getDelegateFunction().getBeginDate().toDateTimeAtMidnight(), getDelegateFunction().getEndDate()
                            .toDateTimeAtMidnight().plusDays(1));
            return executionYear.overlapsInterval(interval);
        }
        return false;
    }

    public Degree getDegree() {
        return ((DegreeUnit) getDelegateFunction().getFunction().getUnit()).getDegree();
    }

    public void delete() {
        getDelegateFunction().delete();
        setDelegateFunction(null);
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    // TODO: controlo de acesso?
    public static void removeActiveDelegatePersonFunctionFromPersonByFunction(Person person, Function function) {
        YearMonthDay today = new YearMonthDay();
        List<PersonFunction> delegatesFunctions = PersonFunction.getActivePersonFunctions(function);
        if (!delegatesFunctions.isEmpty()) {
            for (PersonFunction personfunction : delegatesFunctions) {
                Person delegate = personfunction.getPerson();
                if (delegate.equals(person)) {
                    if (personfunction.getBeginDate().equals(today)) {
                        if (personfunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                            personfunction.getDelegate().delete();
                        } else {
                            personfunction.delete();
                        }
                    } else {
                        personfunction.setOccupationInterval(personfunction.getBeginDate(), today.minusDays(1));
                    }
                }
            }
        }
    }

    // TODO: controlo de acesso?
    public static void addDelegatePersonFunction(PedagogicalCouncilUnit unit, Person person, Function delegateFunction) {
        YearMonthDay currentDate = new YearMonthDay();

        // The following restriction tries to guarantee that a new delegate is
        // elected before this person function ends
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);

        /* Check if there is another active person function with this type */
        if (delegateFunction != null) {
            List<PersonFunction> delegateFunctions = PersonFunction.getActivePersonFunctions(delegateFunction);
            if (!delegateFunctions.isEmpty()) {
                for (PersonFunction personFunction : delegateFunctions) {
                    if (personFunction.getBeginDate().equals(currentDate)) {
                        if (personFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                            personFunction.getDelegate().delete();
                        } else {
                            personFunction.delete();
                        }
                    } else {
                        personFunction.setOccupationInterval(personFunction.getBeginDate(), currentDate.minusDays(1));
                    }
                }
            }
        }

        createDelegatePersonFunction(unit, person, currentDate, endDate, delegateFunction);
    }

    public static Function getActiveDelegateFunctionByType(DegreeUnit degreeUnit, FunctionType functionType) {
        YearMonthDay today = new YearMonthDay();
        for (Function function : degreeUnit.getFunctionsSet()) {
            if (functionType.equals(function.getFunctionType()) && function.isActive(today)) {
                return function;
            }
        }
        return null;
    }

    public static List<Student> getAllDelegatesByExecutionYearAndFunctionType(Degree degree, ExecutionYear executionYear,
            FunctionType functionType) {
        if (degree.isEmpty()) {
            return Collections.emptyList();
        }
        List<Student> result = new ArrayList<Student>();
        if (degree.getUnit() != null) {
            final List<PersonFunction> delegateFunctions =
                    getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(degree.getUnit(), executionYear, functionType);
            for (PersonFunction delegateFunction : delegateFunctions) {
                if (delegateFunction.belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay())) {
                    result.add(delegateFunction.getPerson().getStudent());
                }
            }
        }
        return result;
    }

    public static PersonFunction getActiveDelegatePersonFunctionByStudentAndFunctionType(Degree degree, Student student,
            ExecutionYear executionYear, FunctionType functionType) {
        if (degree.isEmpty()) {
            return null;
        }
        if (degree.getUnit() != null) {
            for (PersonFunction personFunction : getAllActiveDelegatePersonFunctionsByFunctionType(degree.getUnit(),
                    functionType, executionYear)) {
                if (personFunction.getPerson().getStudent().equals(student)) {
                    return personFunction;
                }
            }
        }
        return null;
    }

    public static List<Student> getAllActiveDelegatesByFunctionType(Degree degree, FunctionType functionType,
            ExecutionYear executionYear) {
        if (degree.isEmpty()) {
            return Collections.emptyList();
        }
        List<Student> result = new ArrayList<Student>();
        if (degree.getUnit() != null) {
            final List<PersonFunction> delegateFunctions =
                    getAllActiveDelegatePersonFunctionsByFunctionType(degree.getUnit(), functionType, executionYear);
            for (PersonFunction delegateFunction : delegateFunctions) {
                result.add(delegateFunction.getPerson().getStudent());
            }
        }
        return result;
    }

    /*
     * ACTIVE DELEGATES
     */
    public static List<Student> getAllActiveDelegates(Degree degree) {
        if (degree.isEmpty()) {
            return Collections.emptyList();
        }
        List<Student> result = new ArrayList<Student>();
        for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
            result.addAll(getAllActiveDelegatesByFunctionType(degree, functionType, null));
        }
        return result;
    }

    public static List<PersonFunction> getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(DegreeUnit degreeUnit,
            ExecutionYear executionYear, FunctionType functionType) {
        for (Function function : degreeUnit.getFunctionsSet()) {
            if (function.getFunctionType() != null && function.getFunctionType().equals(functionType)
                    && function.belongsToPeriod(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay())) {
                return PersonFunction.getPersonFunctions(function);
            }
        }
        return Collections.emptyList();
    }

    public static List<PersonFunction> getAllActiveDelegatePersonFunctionsByFunctionType(DegreeUnit degreeUnit,
            FunctionType functionType, ExecutionYear executionYear) {
        executionYear = executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        final Function function = getActiveDelegateFunctionByType(degreeUnit, functionType);
        if (function != null) {
            return PersonFunction.getActivePersonFunctionsStartingIn(function, executionYear);
        }
        return result;
    }

    public static Collection<Delegate> getDelegates(Student student) {
        Collection<Delegate> result = new ArrayList<Delegate>();
        for (Registration registration : student.getRegistrationsSet()) {
            for (Delegate delegate : registration.getDelegatesSet()) {
                result.add(delegate);
            }
        }
        return result;
    }

    public static PersonFunction getDelegateFunction(Student student, ExecutionYear executionYear) {
        PersonFunction delegateFunction = null;
        List<Registration> activeRegistrations = new ArrayList<Registration>(student.getActiveRegistrations());
        Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
        for (Registration registration : activeRegistrations) {
            delegateFunction = getMostSignificantDelegateFunctionForStudent(registration.getDegree(), student, executionYear);
            if (delegateFunction != null) {
                return delegateFunction;
            }
        }
        return delegateFunction;
    }

    /*
     * If student has delegate role, get the students he is responsible for
     */
    public static List<Student> getStudentsResponsibleForGivenFunctionType(Student student, FunctionType delegateFunctionType,
            ExecutionYear executionYear) {
        PersonFunction personFunction = getDelegateFunction(student, executionYear);
        Degree degree = personFunction != null ? personFunction.getUnit().getDegree() : null;
        if (degree != null && getAllActiveDelegatesByFunctionType(degree, delegateFunctionType, executionYear).contains(student)) {
            switch (delegateFunctionType) {
            case DELEGATE_OF_GGAE:
                return degree.getAllStudents();
            case DELEGATE_OF_INTEGRATED_MASTER_DEGREE:
                return degree.getAllStudents();
            case DELEGATE_OF_MASTER_DEGREE:
                return degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree
                        .getSecondCycleStudents(executionYear) : degree.getAllStudents();
            case DELEGATE_OF_DEGREE:
                return degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree
                        .getFirstCycleStudents(executionYear) : degree.getAllStudents();
            case DELEGATE_OF_YEAR: {
                final PersonFunction yearDelegateFunction =
                        getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                                FunctionType.DELEGATE_OF_YEAR);
                int curricularYear = yearDelegateFunction.getCurricularYear().getYear();
                return degree.getStudentsFromGivenCurricularYear(curricularYear, executionYear);
            }
            }
        }

        return new ArrayList<Student>();
    }

    public static List<PersonFunction> getAllDelegateFunctions(Student student) {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (FunctionType delegateFunctionType : FunctionType.getAllDelegateFunctionTypes()) {
            Set<Function> functions = Function.readAllFunctionsByType(delegateFunctionType);
            for (Function function : functions) {
                for (PersonFunction personFunction : PersonFunction.getPersonFunctions(function)) {
                    if (personFunction.getPerson().equals(student.getPerson())) {
                        result.add(personFunction);
                    }
                }
            }
        }
        return result;
    }

    public static List<PersonFunction> getAllActiveDelegateFunctions(Student student, FunctionType functionType) {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        Set<Function> functions = Function.readAllActiveFunctionsByType(functionType);
        for (Function function : functions) {
            for (PersonFunction personFunction : PersonFunction.getActivePersonFunctionsStartingIn(function, currentExecutionYear)) {
                if (personFunction.getPerson().equals(student.getPerson())) {
                    result.add(personFunction);
                }
            }

        }
        return result;
    }

    public static List<PersonFunction> getAllActiveDelegateFunctions(Student student) {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        List<PersonFunction> result = new ArrayList<PersonFunction>();
        for (FunctionType delegateFunctionType : FunctionType.getAllDelegateFunctionTypes()) {
            Set<Function> functions = Function.readAllActiveFunctionsByType(delegateFunctionType);
            for (Function function : functions) {
                for (PersonFunction personFunction : PersonFunction.getActivePersonFunctionsStartingIn(function, currentExecutionYear)) {
                    if (personFunction.getPerson().equals(student.getPerson())) {
                        result.add(personFunction);
                    }
                }
            }

        }
        return result;
    }

    public static PersonFunction getMostSignificantActiveDelegateFunctionForStudent(Degree degree, Student student,
            ExecutionYear executionYear) {
        if (Delegate
                .getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE, executionYear)
                .contains(student)) {
            final PersonFunction pf =
                    getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                            FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE);
            if (pf != null && pf.isActive()) {
                return pf;
            }
        }
        if (getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_MASTER_DEGREE, executionYear).contains(student)) {
            final PersonFunction pf =
                    getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                            FunctionType.DELEGATE_OF_MASTER_DEGREE);
            if (pf != null && pf.isActive()) {
                return pf;
            }
        }
        if (getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_DEGREE, executionYear).contains(student)) {
            final PersonFunction pf =
                    getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                            FunctionType.DELEGATE_OF_DEGREE);
            if (pf != null && pf.isActive()) {
                return pf;
            }
        }
        final PersonFunction pf =
                getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                        FunctionType.DELEGATE_OF_YEAR);
        return pf != null && pf.isActive() ? pf : null;
    }

    public static PersonFunction getMostSignificantDelegateFunctionForStudent(Degree degree, Student student,
            ExecutionYear executionYear) {
        if (Delegate
                .getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE, executionYear)
                .contains(student)) {
            final PersonFunction pf =
                    getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                            FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE);
            if (pf != null) {
                return pf;
            }
        }
        if (getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_MASTER_DEGREE, executionYear).contains(student)) {
            final PersonFunction pf =
                    getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                            FunctionType.DELEGATE_OF_MASTER_DEGREE);
            if (pf != null) {
                return pf;
            }
        }
        if (getAllActiveDelegatesByFunctionType(degree, FunctionType.DELEGATE_OF_DEGREE, executionYear).contains(student)) {
            final PersonFunction pf =
                    getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                            FunctionType.DELEGATE_OF_DEGREE);
            if (pf != null) {
                return pf;
            }
        }
        return getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                FunctionType.DELEGATE_OF_YEAR);
    }

    public static void createDelegatePersonFunction(Unit unit, Person person, YearMonthDay startDate, YearMonthDay endDate,
            Function function) {
        if (function == null) {
            throw new DomainException("error.delegates.noDelegateFunction");
        }
        PersonFunction personFunction = new PersonFunction(unit, person, function, startDate, endDate);
        new PersonFunctionSender(personFunction);
    }

    public static Set<CurricularCourse> getCurricularCoursesResponsibleForByFunctionType(Student student,
            PersonFunction delegateFunction, ExecutionYear executionYear) {
        if (delegateFunction != null) {
            executionYear =
                    executionYear != null ? executionYear : ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());
            Degree degree = delegateFunction.getUnit().getDegree();
            FunctionType delegateFunctionType = delegateFunction.getFunction().getFunctionType();
            if (getAllActiveDelegatesByFunctionType(degree, delegateFunctionType, executionYear).contains(student)) {
                switch (delegateFunctionType) {
                case DELEGATE_OF_GGAE:
                    return degree.getAllCurricularCourses(executionYear);
                case DELEGATE_OF_INTEGRATED_MASTER_DEGREE:
                    return degree.getAllCurricularCourses(executionYear);
                case DELEGATE_OF_MASTER_DEGREE:
                    return degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree
                            .getSecondCycleCurricularCourses(executionYear) : degree.getAllCurricularCourses(executionYear);
                case DELEGATE_OF_DEGREE:
                    return degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) ? degree
                            .getFirstCycleCurricularCourses(executionYear) : degree.getAllCurricularCourses(executionYear);
                case DELEGATE_OF_YEAR: {
                    final PersonFunction yearDelegateFunction =
                            getActiveDelegatePersonFunctionByStudentAndFunctionType(degree, student, executionYear,
                                    FunctionType.DELEGATE_OF_YEAR);
                    int curricularYear = yearDelegateFunction.getCurricularYear().getYear();
                    return degree.getCurricularCoursesFromGivenCurricularYear(curricularYear, executionYear);
                }
                }
            }
        }
        return new HashSet<CurricularCourse>();
    }
}
