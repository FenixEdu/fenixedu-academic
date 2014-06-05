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

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CoordinationTeamLog;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.Bundle;
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
                if (!person.hasAnyCoordinators()) {
                    person.removeRoleByType(RoleType.COORDINATOR);
                }
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