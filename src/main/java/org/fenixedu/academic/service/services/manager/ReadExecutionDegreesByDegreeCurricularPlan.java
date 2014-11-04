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
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class ReadExecutionDegreesByDegreeCurricularPlan {

    @Atomic
    public static List<InfoExecutionDegree> run(final DegreeCurricularPlan degreeCurricularPlan) throws FenixServiceException {
        return getExecutionCourses(degreeCurricularPlan);
    }

    public static List<InfoExecutionDegree> getExecutionCourses(final DegreeCurricularPlan degreeCurricularPlan) {
        final Collection<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegreesSet();

        final List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            result.add(infoExecutionDegree);
        }

        return result;
    }

}