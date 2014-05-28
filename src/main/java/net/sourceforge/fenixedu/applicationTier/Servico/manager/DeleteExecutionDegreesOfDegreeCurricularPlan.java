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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteExecutionDegreesOfDegreeCurricularPlan {

    @Atomic
    public static List<String> run(List<String> executionDegreesIds) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        List<String> undeletedExecutionDegreesYears = new ArrayList<String>();

        for (final String executionDegreeId : executionDegreesIds) {
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

            if (executionDegree != null) {
                if (executionDegree.canBeDeleted()) {
                    executionDegree.delete();
                } else {
                    undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
                }
            }
        }

        return undeletedExecutionDegreesYears;
    }

}