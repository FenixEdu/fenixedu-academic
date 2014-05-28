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

import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixframework.Atomic;

public class RemoveExecutionDegreeToScheduling {

    public class SchedulingContainsProposalsException extends FenixServiceException {
    }

    protected void run(final Scheduleing scheduleing, final ExecutionDegree executionDegree) throws FenixServiceException {
        if (!scheduleing.getProposalsSet().isEmpty()
                || (executionDegree.getScheduling() != null && executionDegree.getScheduling().getProposalsSet().isEmpty())) {
            throw new SchedulingContainsProposalsException();
        }
        scheduleing.getExecutionDegrees().remove(executionDegree);
    }

    // Service Invokers migrated from Berserk

    private static final RemoveExecutionDegreeToScheduling serviceInstance = new RemoveExecutionDegreeToScheduling();

    @Atomic
    public static void runRemoveExecutionDegreeToScheduling(Scheduleing scheduleing, ExecutionDegree executionDegree)
            throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
            serviceInstance.run(scheduleing, executionDegree);
        } catch (NotAuthorizedException ex1) {
            try {
                CoordinatorAuthorizationFilter.instance.execute();
                serviceInstance.run(scheduleing, executionDegree);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}