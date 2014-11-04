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
package org.fenixedu.academic.service.services.coordinator;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.service.filter.ResponsibleDegreeCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddCoordinator {

    @Atomic
    public static Boolean run(final String executionDegreeId, final String username) throws FenixServiceException {

        final Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new FenixServiceException("error.noUserForUsername");
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
    public static Boolean runAddCoordinator(String executionDegreeId, String username) throws FenixServiceException,
            NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return run(executionDegreeId, username);
    }

}