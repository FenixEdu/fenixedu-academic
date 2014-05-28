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
package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
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