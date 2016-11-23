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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateExecutionDegreesForExecutionYear {

    @Atomic
    public static List<DegreeCurricularPlan> run(final String[] degreeCurricularPlansIDs,
            final String[] bolonhaDegreeCurricularPlansIDs, final String executionYearID, final String campusName,
            final Boolean publishedExamMap) {
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
        final Space campus = readCampusByName(campusName);

        final Set<String> allDegreeCurricularPlanIDs = new HashSet<String>();
        if (degreeCurricularPlansIDs != null && degreeCurricularPlansIDs.length > 0) {
            allDegreeCurricularPlanIDs.addAll(Arrays.asList(degreeCurricularPlansIDs));
        }
        if (bolonhaDegreeCurricularPlansIDs != null && bolonhaDegreeCurricularPlansIDs.length > 0) {
            allDegreeCurricularPlanIDs.addAll(Arrays.asList(bolonhaDegreeCurricularPlansIDs));
        }

        final List<DegreeCurricularPlan> created = new ArrayList<DegreeCurricularPlan>();
        for (final String degreeCurricularPlanID : allDegreeCurricularPlanIDs) {
            final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
            if (degreeCurricularPlan == null) {
                continue;
            }

            degreeCurricularPlan.createExecutionDegree(executionYear, campus, publishedExamMap);
            created.add(degreeCurricularPlan);
        }

        return created;
    }

    private static Space readCampusByName(String campusName) {
        if (campusName == null) {
            return null;
        }
        for (Space campus : Space.getAllCampus()) {
            if (campusName.equalsIgnoreCase(campus.getName())) {
                return campus;
            }
        }
        return null;
    }

}