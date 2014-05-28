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
/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCourseGroup {

    @Atomic
    public static void run(final String courseGroupID, final String contextID, final String name, final String nameEn,
            final String beginExecutionPeriodID, final String endExecutionPeriodID) throws FenixServiceException {

        final CourseGroup courseGroup = (CourseGroup) FenixFramework.getDomainObject(courseGroupID);
        if (courseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        final Context context = FenixFramework.getDomainObject(contextID);
        if (context == null && !courseGroup.isRoot()) {
            throw new FenixServiceException("error.noContext");
        }

        courseGroup.edit(name, nameEn, context, getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID));
    }

    private static ExecutionSemester getBeginExecutionPeriod(final String beginExecutionPeriodID) {
        if (beginExecutionPeriodID == null) {
            return ExecutionSemester.readActualExecutionSemester();
        } else {
            return FenixFramework.getDomainObject(beginExecutionPeriodID);
        }
    }

    private static ExecutionSemester getEndExecutionPeriod(String endExecutionPeriodID) {
        return (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionSemester> getDomainObject(endExecutionPeriodID);
    }
}
