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
package org.fenixedu.academic.service.services.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.service.filter.ExecutionCourseCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.filter.ResourceAllocationManagerAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.Season;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateWrittenEvaluation {

    protected void run(String executionCourseID, Date writtenEvaluationDate, Date writtenEvaluationStartTime,
            Date writtenEvaluationEndTime, List<String> executionCourseIDs, List<String> degreeModuleScopeIDs,
            List<String> roomIDs, GradeScale gradeScale, Season examSeason, String writtenTestDescription)
            throws FenixServiceException {
        final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(executionCourseIDs);
        final List<DegreeModuleScope> degreeModuleScopesToAssociate = readCurricularCourseScopesAndContexts(degreeModuleScopeIDs);

        List<Space> roomsToAssociate = null;
        if (roomIDs != null) {
            roomsToAssociate = readRooms(roomIDs);
        }

        // creating the new written evaluation, according to the service
        // arguments
        WrittenEvaluation eval = null;
        if (examSeason != null) {
            eval =
                    new Exam(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
                            executionCoursesToAssociate, degreeModuleScopesToAssociate, roomsToAssociate, gradeScale, examSeason);
        } else if (writtenTestDescription != null) {
            eval =
                    new WrittenTest(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
                            executionCoursesToAssociate, degreeModuleScopesToAssociate, roomsToAssociate, gradeScale,
                            writtenTestDescription);
        } else {
            throw new InvalidArgumentsServiceException();
        }
    }

    private List<ExecutionCourse> readExecutionCourses(final List<String> executionCourseIDs) throws FenixServiceException {
        if (executionCourseIDs.isEmpty()) {
            throw new FenixServiceException("error.invalidExecutionCourse");
        }

        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final String executionCourseID : executionCourseIDs) {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
            if (executionCourse == null) {
                throw new FenixServiceException("error.invalidExecutionCourse");
            }
            result.add(executionCourse);
        }
        return result;
    }

    private List<DegreeModuleScope> readCurricularCourseScopesAndContexts(final List<String> degreeModuleScopeIDs)
            throws FenixServiceException {

        List<DegreeModuleScope> result = new ArrayList<DegreeModuleScope>();
        for (String key : degreeModuleScopeIDs) {
            DegreeModuleScope degreeModuleScope = DegreeModuleScope.getDegreeModuleScopeByKey(key);
            if (degreeModuleScope != null) {
                result.add(degreeModuleScope);
            }
        }

        if (result.isEmpty()) {
            throw new FenixServiceException("error.invalidCurricularCourseScope");
        }

        return result;
    }

    private List<Space> readRooms(final List<String> roomIDs) throws FenixServiceException {
        final List<Space> result = new ArrayList<Space>();
        for (final String roomID : roomIDs) {
            final Space room = (Space) FenixFramework.getDomainObject(roomID);
            if (room == null) {
                throw new FenixServiceException("error.noRoom");
            }
            result.add(room);
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final CreateWrittenEvaluation serviceInstance = new CreateWrittenEvaluation();

    @Atomic
    public static void runCreateWrittenEvaluation(String executionCourseID, Date writtenEvaluationDate,
            Date writtenEvaluationStartTime, Date writtenEvaluationEndTime, List<String> executionCourseIDs,
            List<String> degreeModuleScopeIDs, List<String> roomIDs, GradeScale gradeScale, Season examSeason,
            String writtenTestDescription) throws FenixServiceException, NotAuthorizedException {
        try {
            ResourceAllocationManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(executionCourseID, writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
                    executionCourseIDs, degreeModuleScopeIDs, roomIDs, gradeScale, examSeason, writtenTestDescription);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(executionCourseID, writtenEvaluationDate, writtenEvaluationStartTime,
                        writtenEvaluationEndTime, executionCourseIDs, degreeModuleScopeIDs, roomIDs, gradeScale, examSeason,
                        writtenTestDescription);
            } catch (NotAuthorizedException ex2) {
                try {
                    ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                    serviceInstance.run(executionCourseID, writtenEvaluationDate, writtenEvaluationStartTime,
                            writtenEvaluationEndTime, executionCourseIDs, degreeModuleScopeIDs, roomIDs, gradeScale, examSeason,
                            writtenTestDescription);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}