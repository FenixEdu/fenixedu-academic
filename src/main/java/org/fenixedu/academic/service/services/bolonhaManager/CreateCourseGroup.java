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
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateCourseGroup {

    @Atomic
    public static void run(final String degreeCurricularPlanID, final String parentCourseGroupID, final String name,
            final String nameEn, final String beginExecutionPeriodID, final String endExecutionPeriodID,
            String programConclusionID) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        final CourseGroup parentCourseGroup = (CourseGroup) FenixFramework.getDomainObject(parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        if (beginExecutionPeriodID == null) {
            throw new FenixServiceException("error.CreateCourseGroup.beginExecutionPeriod.required");
        }

        final ExecutionInterval beginExecutionPeriod = FenixFramework.getDomainObject(beginExecutionPeriodID);

        final ExecutionInterval endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionInterval> getDomainObject(endExecutionPeriodID);

        ProgramConclusion programConclusion = null;
        if (!Strings.isNullOrEmpty(programConclusionID)) {
            programConclusion = FenixFramework.getDomainObject(programConclusionID);
        }

        degreeCurricularPlan.createCourseGroup(parentCourseGroup, name, nameEn, beginExecutionPeriod, endExecutionPeriod,
                programConclusion);
    }
}
