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
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.GOPSendMessageService;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
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

        List<AllocatableSpace> roomsToAssociate = null;
        if (roomIDs != null) {
            roomsToAssociate = readRooms(roomIDs);
        }

        if (writtenEvaluation.hasAnyVigilancies()
                && (writtenEvaluationDate != writtenEvaluation.getDayDate() || timeModificationIsBiggerThanFiveMinutes(
                        writtenEvaluationStartTime, writtenEvaluation.getBeginningDate()))) {

            notifyVigilants(writtenEvaluation, writtenEvaluationDate, writtenEvaluationStartTime);
        }

        final List<AllocatableSpace> previousRooms = writtenEvaluation.getAssociatedRooms();

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
            for (final AllocatableSpace allocatableSpace : roomsToAssociate) {
                int intervalCount = 0;
                DateTime beginDateTime =
                        new DateTime(writtenEvaluationStartTime.getTime()).withSecondOfMinute(0).withMillisOfSecond(0);
                // YearMonthDay beginYMD = beginDateTime.toYearMonthDay();

                DateTime endDateTime =
                        new DateTime(writtenEvaluationEndTime.getTime()).withSecondOfMinute(0).withMillisOfSecond(0);
                // YearMonthDay endYMD = endDateTime.toYearMonthDay();

                for (ResourceAllocation resource : allocatableSpace.getResourceAllocationsSet()) {
                    if (resource.isEventSpaceOccupation()) {
                        EventSpaceOccupation eventSpaceOccupation = (EventSpaceOccupation) resource;
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

    private List<AllocatableSpace> readRooms(final List<String> roomIDs) throws FenixServiceException {
        final List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        for (final String roomID : roomIDs) {
            final AllocatableSpace room = (AllocatableSpace) FenixFramework.getDomainObject(roomID);
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
            new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(),
                    new Recipient(new FixedSetGroup(tos)).asCollection(), subject, body, "");
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