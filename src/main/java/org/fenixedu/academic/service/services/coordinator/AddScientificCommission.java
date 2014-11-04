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

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.service.filter.ResponsibleDegreeCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddScientificCommission {

    protected void run(String executionDegreeId, Person person) {
        ExecutionDegree execution = FenixFramework.getDomainObject(executionDegreeId);

        new ScientificCommission(execution, person);
    }

    // Service Invokers migrated from Berserk

    private static final AddScientificCommission serviceInstance = new AddScientificCommission();

    @Atomic
    public static void runAddScientificCommission(String executionDegreeId, Person person) throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute();
        serviceInstance.run(executionDegreeId, person);
    }

}