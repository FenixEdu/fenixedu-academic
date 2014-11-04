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

import java.util.List;

import org.fenixedu.academic.domain.CoordinationTeamLog;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.service.filter.ResponsibleDegreeCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.Bundle;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveCoordinators {

    @Atomic
    public static void run(String executionDegreeID, List<String> coordinatorsToRemoveIDs) {

        for (final String coordinatorToRemoveID : coordinatorsToRemoveIDs) {
            final Coordinator coordinator = FenixFramework.getDomainObject(coordinatorToRemoveID);
            if (coordinator != null) {
                final Person person = coordinator.getPerson();

                CoordinationTeamLog.createLog(coordinator.getExecutionDegree().getDegree(), coordinator.getExecutionDegree()
                        .getExecutionYear(), Bundle.MESSAGING, "log.degree.coordinationteam.removemember", coordinator
                        .getPerson().getPresentationName(), coordinator.getExecutionDegree().getPresentationName());

                coordinator.delete();
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final RemoveCoordinators serviceInstance = new RemoveCoordinators();

    @Atomic
    public static void runRemoveCoordinators(String executionDegreeID, List<String> coordinatorsToRemoveIDs)
            throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeID);
        serviceInstance.run(executionDegreeID, coordinatorsToRemoveIDs);
    }

}