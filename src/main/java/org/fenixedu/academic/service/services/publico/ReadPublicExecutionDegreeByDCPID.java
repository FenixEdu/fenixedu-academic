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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadPublicExecutionDegreeByDCPID {

    @Atomic
    public static List<InfoExecutionDegree> run(String degreeCurricularPlanID) throws FenixServiceException {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();
        for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            result.add(copyExecutionDegree2InfoExecutionDegree(executionDegree));
        }

        return result;
    }

    @Atomic
    public static InfoExecutionDegree run(String degreeCurricularPlanID, String executionYearID) {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);

        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear.getYear());
        if (executionDegree == null) {
            return null;
        }

        return copyExecutionDegree2InfoExecutionDegree(executionDegree);
    }

    protected static InfoExecutionDegree copyExecutionDegree2InfoExecutionDegree(ExecutionDegree executionDegree) {
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}