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
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.space.EventSpaceOccupation;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.GOPSendMessageService;
import org.fenixedu.academic.service.filter.EditWrittenEvaluationAuthorization;
import org.fenixedu.academic.service.filter.ExecutionCourseCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.filter.ResourceAllocationManagerAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.Season;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditWrittenEvaluation {
    public static class EditWrittenEvaluationEvent extends DomainObjectEvent<WrittenEvaluation> {
        private final Date dayDate;
        private final Date beginDate;

        public EditWrittenEvaluationEvent(WrittenEvaluation instance, Date dayDate, Date beginDate) {
            super(instance);
            this.dayDate = dayDate;
            this.beginDate = beginDate;
        }

        public Date getDayDate() {
            return dayDate;
        }

        public Date getBeginDate() {
            return beginDate;
        }
    }

    protected void run(String executionCourseID, Date writtenEvaluationDate, Date writtenEvaluationStartTime,
            Date writtenEvaluationEndTime, List<String> executionCourseIDs, List<String> degreeModuleScopeIDs,
            List<String> roomIDs, String writtenEvaluationOID, Season examSeason, String writtenTestDescription,
            GradeScale gradeScale) throws FenixServiceException {

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationOID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

        final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(executionCourseIDs);
        final List<DegreeModuleScope> degreeModuleScopeToAssociate = readCurricularCourseScopesAndContexts(degreeModuleScopeIDs);

        List<Space> roomsToAssociate = null;
        if (roomIDs != null) {
            roomsToAssociate = readRooms(roomIDs);
        }

        Signal.emit("academic.writtenevaluation.edited", new EditWrittenEvaluationEvent(writtenEvaluation, writtenEvaluationDate,
                writtenEvaluationStartTime));

        final List<Space> previousRooms = writtenEvaluation.getAssociatedRooms();

        if (examSeason != null) {
            ((Exam) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
                    executionCoursesToAssociate, degreeModuleScopeToAssociate, roomsToAssociate, gradeScale, examSeason);
        } else if (writtenTestDescription != null) {
            final WrittenTest writtenTest = (WrittenTest) writtenEvaluation;
            final Date prevTestDate = writtenTest.getDayDate();
            final Date prevStartTime = writtenTest.getBeginningDate();
            final Date prevTestEnd = writtenTest.getEndDate();

            writtenTest.edit(writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
                    executionCoursesToAssociate, degreeModuleScopeToAssociate, roomsToAssociate, gradeScale,
                    writtenTestDescription);

            if (writtenTest.getRequestRoomSentDate() != null) {
                if (!prevTestDate.equals(writtenEvaluationDate) || !prevStartTime.equals(writtenEvaluationStartTime)
                        || !prevTestEnd.equals(writtenEvaluationEndTime)) {
                    if (!RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(AccessControl.getPerson().getUser())) {
                        GOPSendMessageService.requestChangeRoom(writtenTest, prevTestDate, prevStartTime, prevTestEnd);
                    }
                }
            }

        } else {
            throw new InvalidArgumentsServiceException();
        }

        if (roomsToAssociate != null) {
            for (final Space allocatableSpace : roomsToAssociate) {
                int intervalCount = 0;
                DateTime beginDateTime =
                        new DateTime(writtenEvaluationStartTime.getTime()).withSecondOfMinute(0).withMillisOfSecond(0);
                // YearMonthDay beginYMD = beginDateTime.toYearMonthDay();

                DateTime endDateTime =
                        new DateTime(writtenEvaluationEndTime.getTime()).withSecondOfMinute(0).withMillisOfSecond(0);
                // YearMonthDay endYMD = endDateTime.toYearMonthDay();

                for (Occupation occupation : allocatableSpace.getOccupationSet()) {
                    if (occupation instanceof EventSpaceOccupation) {
                        EventSpaceOccupation eventSpaceOccupation = (EventSpaceOccupation) occupation;
                        List<Interval> intervals =
                                eventSpaceOccupation.getEventSpaceOccupationIntervals(beginDateTime, endDateTime);
                        intervalCount += intervals.size();
                        if (intervalCount > 1) {
                            throw new DomainException("error.noRoom", allocatableSpace.getName());
                        }
                    }
                }
            }
        }
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

    // Service Invokers migrated from Berserk

    private static final EditWrittenEvaluation serviceInstance = new EditWrittenEvaluation();

    @Atomic
    public static void runEditWrittenEvaluation(String executionCourseID, Date writtenEvaluationDate,
            Date writtenEvaluationStartTime, Date writtenEvaluationEndTime, List<String> executionCourseIDs,
            List<String> degreeModuleScopeIDs, List<String> roomIDs, String writtenEvaluationOID, Season examSeason,
            String writtenTestDescription, GradeScale gradeScale) throws FenixServiceException, NotAuthorizedException {
        EditWrittenEvaluationAuthorization.instance.execute(writtenEvaluationOID);
        try {
            ResourceAllocationManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(executionCourseID, writtenEvaluationDate, writtenEvaluationStartTime, writtenEvaluationEndTime,
                    executionCourseIDs, degreeModuleScopeIDs, roomIDs, writtenEvaluationOID, examSeason, writtenTestDescription,
                    gradeScale);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, writtenEvaluationDate, writtenEvaluationStartTime,
                        writtenEvaluationEndTime, executionCourseIDs, degreeModuleScopeIDs, roomIDs, writtenEvaluationOID,
                        examSeason, writtenTestDescription, gradeScale);
            } catch (NotAuthorizedException ex2) {
                try {
                    ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                    serviceInstance.run(executionCourseID, writtenEvaluationDate, writtenEvaluationStartTime,
                            writtenEvaluationEndTime, executionCourseIDs, degreeModuleScopeIDs, roomIDs, writtenEvaluationOID,
                            examSeason, writtenTestDescription, gradeScale);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}