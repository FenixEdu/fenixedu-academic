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
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteExecutionDegreesOfDegreeCurricularPlan {

    @Atomic
    public static List<String> run(List<String> executionDegreesIds) throws FenixServiceException {
        List<String> undeletedExecutionDegreesYears = new ArrayList<String>();

        for (final String executionDegreeId : executionDegreesIds) {
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

            if (executionDegree != null) {
                if (executionDegree.isDeletable()) {
                    executionDegree.delete();
                } else {
                    undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
                }
            }
        }

        return undeletedExecutionDegreesYears;
    }

}