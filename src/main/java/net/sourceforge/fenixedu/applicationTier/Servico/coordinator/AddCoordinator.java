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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddCoordinator {

    @Atomic
    public static Boolean run(final String executionDegreeId, final String istUsername) throws FenixServiceException {

        final Person person = Person.readPersonByUsername(istUsername);
        if (person == null) {
            throw new FenixServiceException("error.noEmployeeForIstUsername");
        }

        final Employee employee = person.getEmployee();
        if (employee == null) {
            throw new FenixServiceException("error.noEmployeeForIstUsername");
        }

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        if (coordinator == null) {
            Coordinator.createCoordinator(executionDegree, person, Boolean.FALSE);
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static Boolean runAddCoordinator(String executionDegreeId, String istUsername) throws FenixServiceException,
            NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return run(executionDegreeId, istUsername);
    }

}