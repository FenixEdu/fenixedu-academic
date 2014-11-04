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
package org.fenixedu.academic.service.services.pedagogicalCouncil.delegates;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.elections.DelegateElection;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.DegreeUnit;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PedagogicalCouncilUnit;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.predicate.RolePredicates;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class RemoveDelegate {

    @Atomic
    public static void run(Student student) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        run(student, FunctionType.DELEGATE_OF_YEAR);
    }

    @Atomic
    public static void run(PersonFunction personFunction, LocalDate newEndDate) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        Student student = personFunction.getPerson().getStudent();

        if (!personFunction.getBeginDate().isBefore(newEndDate)) {
            throw new FenixServiceException("error.personFunction.endDateBeforeBeginDate");
        } else {
            try {
                personFunction.setOccupationInterval(personFunction.getBeginDate(), new YearMonthDay(newEndDate));
            } catch (DomainException e) {
                throw new FenixServiceException(e.getMessage());
            }
            if (Delegate.getAllActiveDelegateFunctions(student).isEmpty()) {
                RoleType.revoke(RoleType.DELEGATE, student.getPerson().getUser());
            }
        }

    }

    @Atomic
    public static void run(PersonFunction personFunction) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        Student student = personFunction.getPerson().getStudent();
        YearMonthDay yesterday = new YearMonthDay().minusDays(1);
        if (!personFunction.getBeginDate().isBefore(yesterday)) {
            throw new FenixServiceException("error.personFunction.endDateBeforeBeginDate");
        } else {
            personFunction.setOccupationInterval(personFunction.getBeginDate(), yesterday);
            if (Delegate.getAllActiveDelegateFunctions(student).isEmpty()) {
                RoleType.revoke(RoleType.DELEGATE, student.getPerson().getUser());
            }
        }

    }

    @Atomic
    public static void run(Student student, FunctionType delegateFunctionType) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final DegreeUnit degreeUnit = student.getLastActiveRegistration().getDegree().getUnit();

        if (delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR)) {

            for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
                removeActiveDelegatePersonFunctionFromStudentByFunctionType(degreeUnit, student, functionType);
            }

            /* Remove delegate role from this student */
            RoleType.revoke(RoleType.DELEGATE, student.getPerson().getUser());

            /*
             * Remove this student from the election in wich he was elected (if
             * he has it)
             */
            DelegateElection election = DelegateElection.getLastElectedDelegateElection(student);
            if (election != null && election.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
                election.setElectedStudent(null);
            }
        } else {
            removeActiveDelegatePersonFunctionFromStudentByFunctionType(degreeUnit, student, delegateFunctionType);
        }
    }

    @Atomic
    public static void run(Person person, Function delegateFunction) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        PedagogicalCouncilUnit unit = (PedagogicalCouncilUnit) delegateFunction.getUnit();

        Delegate.removeActiveDelegatePersonFunctionFromPersonByFunction(person, delegateFunction);
    }

    private static void removeActiveDelegatePersonFunctionFromStudentByFunctionType(DegreeUnit degreeUnit, Student student,
            FunctionType functionType) {
        YearMonthDay today = new YearMonthDay();
        List<PersonFunction> delegatesFunctions =
                Delegate.getAllActiveDelegatePersonFunctionsByFunctionType(degreeUnit, functionType, null);
        if (!delegatesFunctions.isEmpty()) {
            for (PersonFunction function : delegatesFunctions) {
                Student delegateStudent = function.getPerson().getStudent();
                if (delegateStudent.equals(student)) {
                    function.setOccupationInterval(function.getBeginDate(), today.minusDays(1));
                }
            }
        }
    }
}