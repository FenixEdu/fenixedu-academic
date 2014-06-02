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
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.EditWrittenEvaluationAuthorization;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResourceAllocationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Season;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.core.service.GOPSendMessageService;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditWrittenEvaluation {

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

        if (writtenEvaluation.hasAnyVigilancies()
                && (writtenEvaluationDate != writtenEvaluation.getDayDate() || timeModificationIsBiggerThanFiveMinutes(
                        writtenEvaluationStartTime, writtenEvaluation.getBeginningDate()))) {

            notifyVigilants(writtenEvaluation, writtenEvaluationDate, writtenEvaluationStartTime);
        }

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
                    if (!AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
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

    private boolean timeModificationIsBiggerThanFiveMinutes(Date writtenEvaluationStartTime, Date beginningDate) {
        int hourDiference = Math.abs(writtenEvaluationStartTime.getHours() - beginningDate.getHours());
        int minuteDifference = Math.abs(writtenEvaluationStartTime.getMinutes() - beginningDate.getMinutes());

        return hourDiference > 0 || minuteDifference > 5;
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

    private void notifyVigilants(WrittenEvaluation writtenEvaluation, Date dayDate, Date beginDate) {

        final HashSet<Person> tos = new HashSet<Person>();

        // VigilantGroup group =
        // writtenEvaluation.getAssociatedVigilantGroups().iterator().next();
        for (VigilantGroup group : writtenEvaluation.getAssociatedVigilantGroups()) {
            tos.clear();
            DateTime date = writtenEvaluation.getBeginningDateTime();
            String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
            String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

            String subject =
                    String.format("[ %s - %s - %s %s ]", new Object[] { writtenEvaluation.getName(), group.getName(),
                            beginDateString, time });
            String body =
                    String.format(
                            "Caro Vigilante,\n\nA prova de avalia��o: %1$s %2$s - %3$s foi alterada para  %4$td-%4$tm-%4$tY - %5$tH:%5$tM.",
                            new Object[] { writtenEvaluation.getName(), beginDateString, time, dayDate, beginDate });

            for (Vigilancy vigilancy : writtenEvaluation.getVigilancies()) {
                Person person = vigilancy.getVigilantWrapper().getPerson();
                tos.add(person);
            }
            Sender sender = Bennu.getInstance().getSystemSender();
            new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(), new Recipient(UserGroup.of(Person
                    .convertToUsers(tos))).asCollection(), subject, body, "");
        }
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