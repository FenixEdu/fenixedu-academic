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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditWrittenEvaluationEnrolmentPeriod {

    protected void run(String executionCourseID, String writtenEvaluationID, Date beginDate, Date endDate, Date beginTime,
            Date endTime) throws FenixServiceException {
        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        writtenEvaluation.editEnrolmentPeriod(beginDate, endDate, beginTime, endTime);
    }

    // Service Invokers migrated from Berserk

    private static final EditWrittenEvaluationEnrolmentPeriod serviceInstance = new EditWrittenEvaluationEnrolmentPeriod();

    @Atomic
    public static void runEditWrittenEvaluationEnrolmentPeriod(String executionCourseID, String writtenEvaluationID,
            Date beginDate, Date endDate, Date beginTime, Date endTime) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, writtenEvaluationID, beginDate, endDate, beginTime, endTime);
    }

}