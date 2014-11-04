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
package org.fenixedu.academic.service.services.publico;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionYear;

import pt.ist.fenixframework.Atomic;

public class ReadExecutionDegreesByExecutionYearAndDegreeInitials {

    @Atomic
    public static InfoExecutionDegree run(final InfoExecutionYear infoExecutionYear, final String degreeInitials,
            final String nameDegreeCurricularPlan) {

        final DegreeCurricularPlan degreeCurricularPlan =
                DegreeCurricularPlan.readByNameAndDegreeSigla(nameDegreeCurricularPlan, degreeInitials);
        final ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, infoExecutionYear.getYear());
        return getInfoExecutionDegree(executionDegree);
    }

    public static InfoExecutionDegree getInfoExecutionDegree(final ExecutionDegree executionDegree) {
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}