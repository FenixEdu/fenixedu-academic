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
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.elections.DelegateElection;
import org.fenixedu.academic.domain.elections.YearDelegateElection;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.DegreeUnit;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PedagogicalCouncilUnit;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.YearDelegate;
import org.fenixedu.academic.domain.util.email.PersonFunctionSender;
import org.fenixedu.academic.predicate.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class AddNewDelegate {

    /* Year Delegates */
    @Atomic
    public static void run(Student student, YearDelegateElection election) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        election.setElectedStudent(student);

        run(student, election.getCurricularYear(), election.getDegree());
    }

    /* Year Delegates */
    @Atomic
    public static void run(Student student, CurricularYear curricularYear, Degree degree) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final DegreeUnit degreeUnit = degree.getUnit();
        final Person studentPerson = student.getPerson();
        Registration lastActiveRegistration = student.getActiveRegistrationFor(degree);
        if (lastActiveRegistration == null || !lastActiveRegistration.getDegree().equals(degree)) {
            throw new FenixServiceException("error.delegates.studentNotBelongsToDegree");
        }

        try {
            YearMonthDay currentDate = new YearMonthDay();

            // The following restriction tries to guarantee that a new delegate is
            // elected before this person function ends
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);

            Function function = Delegate.getActiveDelegateFunctionByType(degreeUnit, FunctionType.DELEGATE_OF_YEAR);

            /* Check if there is another active person function with this type */
            if (function != null) {

                List<PersonFunction> delegateFunctions = PersonFunction.getActivePersonFunctions(function);

                for (PersonFunction personFunction : delegateFunctions) {
                    if (personFunction.getCurricularYear().equals(curricularYear)
                            || personFunction.getDelegate().getRegistration().getStudent().equals(student)) {
                        Student oldStudent = personFunction.getPerson().getStudent();

                        if (personFunction.getBeginDate().equals(currentDate)) {
                            personFunction.getDelegate().delete();
                        } else {
                            personFunction.setOccupationInterval(personFunction.getBeginDate(), currentDate.minusDays(1));
                        }
                        final DelegateElection election =
                                YearDelegateElection.getYearDelegateElectionWithLastCandidacyPeriod(degreeUnit.getDegree(),
                                        ExecutionYear.readCurrentExecutionYear(), curricularYear);

                        if (election != null && election.getElectedStudent() == oldStudent) {
                            election.setElectedStudent(null);
                        }
                    }
                }

            }
            PersonFunction personFunction =
                    createYearDelegatePersonFunction(degreeUnit, student.getPerson(), currentDate, endDate, function,
                            curricularYear);
            RoleType.grant(RoleType.DELEGATE, studentPerson.getUser());

            new YearDelegate(lastActiveRegistration, personFunction);

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }

    /* All other delegates */
    @Atomic
    public static void run(Student student, Degree degree, FunctionType delegateFunctionType) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final DegreeUnit degreeUnit = degree.getUnit();
        final Person studentPerson = student.getPerson();

        try {
            Registration lastActiveRegistration = student.getLastActiveRegistration();
            if (lastActiveRegistration == null || !lastActiveRegistration.getDegree().equals(degreeUnit.getDegree())) {
                throw new DomainException("error.delegates.studentNotBelongsToDegree");
            }

            YearMonthDay currentDate = new YearMonthDay();

            // The following restriction tries to guarantee that a new delegate is
            // elected before this person function ends
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            YearMonthDay endDate = currentExecutionYear.getEndDateYearMonthDay().plusYears(1);

            Function function = Delegate.getActiveDelegateFunctionByType(degreeUnit, delegateFunctionType);

            /* Check if there is another active person function with this type */
            if (function != null) {
                List<PersonFunction> delegateFunctions = PersonFunction.getActivePersonFunctions(function);
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

            Delegate.createDelegatePersonFunction(degreeUnit, student.getPerson(), currentDate, endDate, function);
            RoleType.grant(RoleType.DELEGATE, studentPerson.getUser());
        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }

    /* GGAE Delegates */
    @Atomic
    public static void run(Person person, Function delegateFunction) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final PedagogicalCouncilUnit unit = (PedagogicalCouncilUnit) delegateFunction.getUnit();

        try {
            Delegate.addDelegatePersonFunction(unit, person, delegateFunction);

            RoleType.grant(RoleType.DELEGATE, person.getUser());

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
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
}