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
package org.fenixedu.academic.service.services.teacher;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;

import pt.ist.fenixframework.Atomic;

public class UpdateProfessorshipWithPerson {
    @Atomic
    public static Boolean run(Person person, ExecutionYear executionYear, final List<String> executionCourseResponsabilities)
            throws MaxResponsibleForExceed, InvalidCategory, NotAuthorizedException {
        AbstractModifyProfessorshipWithPerson.run(person);
        person.updateResponsabilitiesFor(executionYear.getExternalId(), executionCourseResponsabilities);
        return true;
    }
}
