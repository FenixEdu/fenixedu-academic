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
package org.fenixedu.academic.service.services.commons.student;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoPerson;
import org.fenixedu.academic.dto.InfoStudent;
import org.fenixedu.academic.service.filter.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import org.fenixedu.academic.service.filter.StudentAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentsByPerson {

    protected List run(InfoPerson infoPerson) {
        final List<InfoStudent> result = new ArrayList<InfoStudent>();

        Person person = (Person) FenixFramework.getDomainObject(infoPerson.getExternalId());
        for (final Registration registration : person.getStudents()) {
            result.add(InfoStudent.newInfoFromDomain(registration));
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsByPerson serviceInstance = new ReadStudentsByPerson();

    @Atomic
    public static List runReadStudentsByPerson(InfoPerson infoPerson) throws NotAuthorizedException {
        try {
            StudentAuthorizationFilter.instance.execute();
            return serviceInstance.run(infoPerson);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                return serviceInstance.run(infoPerson);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}