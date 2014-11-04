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
 * Created on Nov 8, 2005
 *  by jdnf
 */
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.service.filter.ExecutionCourseCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteEvaluation {

    /**
     * @param Integer
     *            executionCourseID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    protected void run(String executionCourseID, String evaluationID) throws FenixServiceException {
        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationID);
        if (evaluation == null) {
            throw new FenixServiceException("error.noEvaluation");
        }
        evaluation.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteEvaluation serviceInstance = new DeleteEvaluation();

    @Atomic
    public static void runDeleteEvaluation(String executionCourseID, String evaluationID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, evaluationID);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, evaluationID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}