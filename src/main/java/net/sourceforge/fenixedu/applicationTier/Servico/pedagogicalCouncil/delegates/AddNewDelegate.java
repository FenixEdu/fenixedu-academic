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
package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.delegates;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.YearDelegate;
import net.sourceforge.fenixedu.predicates.RolePredicates;
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
            PersonFunction personFunction = degreeUnit.addYearDelegatePersonFunction(student, curricularYear);
            studentPerson.addPersonRoleByRoleType(RoleType.DELEGATE);

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
            degreeUnit.addDelegatePersonFunction(student, delegateFunctionType);
            studentPerson.addPersonRoleByRoleType(RoleType.DELEGATE);

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
            unit.addDelegatePersonFunction(person, delegateFunction);

            person.addPersonRoleByRoleType(RoleType.DELEGATE);

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }
}