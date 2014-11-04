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
/*
 * Created on Feb 20, 2004
 * 
 */
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice.commons;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 * @return List containing all InfoExecutionDegrees, corresponding to Degree
 *         Curricular Plan
 * 
 */
public class ReadExecutionDegreesByDegreeCurricularPlanID {

    @Atomic
    public static List<InfoExecutionDegree> run(String degreeCurricularPlanID) {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();

        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            result.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static List<InfoExecutionDegree> runReadExecutionDegreesByDegreeCurricularPlanID(String degreeCurricularPlanID)
            throws NotAuthorizedException {
        return run(degreeCurricularPlanID);
    }

}