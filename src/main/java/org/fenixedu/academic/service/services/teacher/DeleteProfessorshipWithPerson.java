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
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class DeleteProfessorshipWithPerson {

    @Atomic
    public static Boolean run(Person person, ExecutionCourse executionCourse) throws NotAuthorizedException {
        try {

            final Person loggedPerson = AccessControl.getPerson();

            Professorship selectedProfessorship = null;
            selectedProfessorship = person.getProfessorshipByExecutionCourse(executionCourse);

            if ((loggedPerson == null) || (selectedProfessorship == null) || loggedPerson == selectedProfessorship.getPerson()
                    || selectedProfessorship.getResponsibleFor()) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }

        Professorship professorshipToDelete = person.getProfessorshipByExecutionCourse(executionCourse);

        professorshipToDelete.delete();

        return Boolean.TRUE;
    }
}
