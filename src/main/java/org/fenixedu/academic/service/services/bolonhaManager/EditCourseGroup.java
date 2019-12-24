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
 * Created on Dec 9, 2005
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCourseGroup {

    @Atomic
    public static void run(final String courseGroupID, final String contextID, final String name, final String nameEn,
            final String beginExecutionPeriodID, final String endExecutionPeriodID, final Boolean isOptional,
            final String programConclusionID) throws FenixServiceException {

        final CourseGroup courseGroup = (CourseGroup) FenixFramework.getDomainObject(courseGroupID);
        if (courseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        final Context context = FenixFramework.getDomainObject(contextID);
        if (context == null && !courseGroup.isRoot()) {
            throw new FenixServiceException("error.noContext");
        }

        ProgramConclusion programConclusion = null;
        if (!Strings.isNullOrEmpty(programConclusionID)) {
            programConclusion = FenixFramework.getDomainObject(programConclusionID);
        }

        courseGroup.edit(name, nameEn, context, getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID), isOptional, programConclusion);
    }

    private static ExecutionInterval getBeginExecutionPeriod(final String beginExecutionPeriodID) {
        if (beginExecutionPeriodID == null) {
            throw new DomainException("error.EditCourseGroup.beginExecutionPeriod.required");
        }

        return FenixFramework.getDomainObject(beginExecutionPeriodID);
    }

    private static ExecutionInterval getEndExecutionPeriod(String endExecutionPeriodID) {
        return (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionInterval> getDomainObject(endExecutionPeriodID);
    }
}
