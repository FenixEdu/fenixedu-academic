/*
 * Created on Sep 14, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;

public class ExamTest extends DomainTestBase {

    private Exam exam;
    private Season season;
    private OccupationPeriod period;
    private CurricularCourse curricularCourse;
    private CurricularCourseScope curricularCourseScope;

    private List<ExecutionCourse> executionCoursesToAssociate;
    private List<CurricularCourseScope> curricularCourseScopesToAssociate;
    private List<OldRoom> rooms;

    private Exam otherExam;
    
    private Date examDate;
    private Calendar examStartTime;
    private Calendar examEndTime;

    protected void setUp() throws Exception {
        super.setUp();

        season = new Season();
        season.setSeason(Season.SEASON1);

        setupTestData();
    }

    public void testCreate() {
//
//        try {
//            new Exam(examDate, examStartTime.getTime(), examEndTime.getTime(), executionCoursesToAssociate, 
//                    curricularCourseScopesToAssociate, rooms, period, season);
//            fail("Expected DomainException: There is already an 'Exam' to that season and curricular course scope.");
//        } catch (DomainException e) {
//            checkExamAssociationsSize(executionCoursesToAssociate.size(), 1,
//                    curricularCourseScopesToAssociate.size(), 1, 1, 2);
//        }
//
//        try {
//            new Exam(examDate, examEndTime.getTime(), examStartTime.getTime(),
//                    executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period, season);
//            fail("Expected DomainException: The exam start time is after the exam end time.");
//        } catch (DomainException e) {
//            checkExamAssociationsSize(executionCoursesToAssociate.size(), 1,
//                    curricularCourseScopesToAssociate.size(), 1, 1, 2);
//        }
//
//        // Remove otherExam from CurricularCourseScope which has already an exam
//        otherExam.removeAssociatedCurricularCourseScope(curricularCourseScope);
//
//        CurricularCourseScope newCurricularCourseScope = new CurricularCourseScope();
//        newCurricularCourseScope.setIdInternal(2);
//        newCurricularCourseScope.setCurricularCourse(curricularCourse);
//
//        otherExam.addAssociatedCurricularCourseScope(newCurricularCourseScope);
//
//        final Exam newExam = new Exam(examDate, examStartTime.getTime(), examEndTime
//                .getTime(), executionCoursesToAssociate, curricularCourseScopesToAssociate,
//                rooms, period, season);
//        checkExamAttributes(newExam, examDate, examStartTime.getTime(), examEndTime.getTime(), season,
//                executionCoursesToAssociate, curricularCourseScopesToAssociate);
    }

    public void testEdit() {
//
//        try {
//            exam.edit(examDate, examStartTime.getTime(), examEndTime.getTime(), executionCoursesToAssociate, 
//                    curricularCourseScopesToAssociate, rooms, period, season);
//            fail("Expected DomainException: There is already an 'Exam' to that season and curricular course scope.");
//        } catch (DomainException e) {
//            checkExamAttributes(exam, exam.getDayDate(), exam.getBeginningDate(), exam.getEndDate(),
//                    exam.getSeason(), exam.getAssociatedExecutionCourses(), exam
//                            .getAssociatedCurricularCourseScope());
//        }
//
//        // Remove otherExam from CurricularCourseScope which has already an exam
//        otherExam.removeAssociatedCurricularCourseScope(curricularCourseScope);
//
//        CurricularCourseScope newCurricularCourseScope = new CurricularCourseScope();
//        newCurricularCourseScope.setIdInternal(2);
//        newCurricularCourseScope.setCurricularCourse(curricularCourse);
//
//        otherExam.addAssociatedCurricularCourseScope(newCurricularCourseScope);
//
//        try {
//            exam.edit(examDate, examStartTime.getTime(), examEndTime.getTime(), executionCoursesToAssociate, 
//                    curricularCourseScopesToAssociate, rooms, period, season);
//            checkExamAttributes(exam, examDate, examStartTime.getTime(), examEndTime.getTime(), season,
//                    executionCoursesToAssociate, curricularCourseScopesToAssociate);
//            // For each OldRoom is created a new RoomOccupation in the Exam
//            assertEquals("Unexpected room occupation in exam!", rooms.size(), exam
//                    .getAssociatedRoomOccupationCount());
//
//        } catch (DomainException e) {
//            fail("Unexpected DomainException: The 'Exam' should have been edit correctly.");
//        }
    }

    public void testDelete() {
        try {
            exam.delete();
            fail("Expected DomainException: 'Exam' has ExamRoomStudents.");
        } catch (DomainException e) {
            assertEquals("Invalid ExamStudentRooms Count!", exam.getWrittenEvaluationEnrolmentsCount(), 1);
            assertEquals("Invalid ExecutionCourses Count!", exam.getAssociatedExecutionCoursesCount(), 2);
            assertEquals("Invalid CurricularCoursesScope Count!", exam
                    .getAssociatedCurricularCourseScopeCount(), 1);
            assertEquals("Invalid RoomOccupations Count!", exam.getAssociatedRoomOccupationCount(), 2);
        }

        exam.getWrittenEvaluationEnrolments().clear();
        try {

            exam.delete();

            assertEquals("Invalid ExamStudentRooms Count!", exam.getWrittenEvaluationEnrolmentsCount(), 0);
            assertEquals("Invalid ExecutionCourses Count!", exam.getAssociatedExecutionCoursesCount(), 0);
            assertEquals("Invalid CurricularCoursesScope Count!", exam
                    .getAssociatedCurricularCourseScope().size(), 0);
            assertEquals("Invalid RoomOccupations Count!", exam.getAssociatedRoomOccupation().size(), 0);

        } catch (DomainException e) {
            fail("Unexpected DomainException: 'Exam' should have been deleted.");
        }
    }

    private void setupTestData() {

        ExecutionCourse executionCourse1 = new ExecutionCourse("name1", "acronym2", ExecutionPeriod.readActualExecutionPeriod());
        executionCourse1.setIdInternal(1);
        ExecutionCourse executionCourse2 = new ExecutionCourse("name2", "acronym2", ExecutionPeriod.readActualExecutionPeriod());
        executionCourse2.setIdInternal(2);

        curricularCourse = new CurricularCourse();
        curricularCourse.setIdInternal(1);
        curricularCourse.addAssociatedExecutionCourses(executionCourse1);
        curricularCourse.addAssociatedExecutionCourses(executionCourse2);

        curricularCourseScope = new CurricularCourseScope();
        curricularCourseScope.setIdInternal(1);
        curricularCourseScope.setCurricularCourse(curricularCourse);

        Calendar startTime = createHour(10, 0);
        Calendar endTime = createHour(13, 0);

        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        executionCourses.add(executionCourse1);
        executionCourses.add(executionCourse2);

        final List<CurricularCourseScope> curricularCourseScopes = new ArrayList<CurricularCourseScope>();
        curricularCourseScopes.add(curricularCourseScope);

//        exam = new Exam(createDate(2005, 9, 15), startTime.getTime(), endTime.getTime(),
//                executionCourses, curricularCourseScopes, null, null, season);
//        exam.setIdInternal(1);
//        exam.setDayDate(createDate(2005, 9, 15));
//        exam.setBeginningDate(startTime.getTime());
//        exam.setEndDate(endTime.getTime());
//        exam.setSeason(season);

//        exam.addAssociatedExecutionCourses(executionCourse1);
//        exam.addAssociatedExecutionCourses(executionCourse2);

//        exam.addAssociatedCurricularCourseScope(curricularCourseScope);

        period = new OccupationPeriod(startTime.getTime(), endTime.getTime());

        OldRoom room1 = new OldRoom();
        room1.setIdInternal(1);
        OldRoom room2 = new OldRoom();
        room2.setIdInternal(2);

        DiaSemana dayOfWeek = new DiaSemana(exam.getDay().get(Calendar.DAY_OF_WEEK));
        room1.createRoomOccupation(period, startTime, endTime, dayOfWeek, RoomOccupation.DIARIA, null,
                exam);
        room2.createRoomOccupation(period, startTime, endTime, dayOfWeek, RoomOccupation.DIARIA, null,
                exam);

        // Registration Enrolled in exam
        WrittenEvaluationEnrolment writtenEvaluationEnrolment = new WrittenEvaluationEnrolment();
        writtenEvaluationEnrolment.setIdInternal(1);

        Registration registration = new Registration();
        registration.setIdInternal(1);

        writtenEvaluationEnrolment.setWrittenEvaluation(exam);
        writtenEvaluationEnrolment.setStudent(registration);
        writtenEvaluationEnrolment.setRoom(room1);

        // Domain entities used by Create and Edit
        executionCoursesToAssociate = new ArrayList<ExecutionCourse>(1);

        final ExecutionCourse executionCourseToAssociate = new ExecutionCourse("name", "acronym", ExecutionPeriod.readActualExecutionPeriod());
        executionCourseToAssociate.setIdInternal(3);
        executionCourseToAssociate.addAssociatedCurricularCourses(curricularCourse);

        executionCoursesToAssociate.add(executionCourseToAssociate);

//        otherExam = new Exam(createDate(2005, 9, 15), startTime.getTime(), endTime.getTime(),
//                executionCoursesToAssociate, curricularCourseScopes, null, null, season);
//        otherExam.setIdInternal(2);
//        otherExam.setSeason(season);
//        otherExam.addAssociatedExecutionCourses(executionCourseToAssociate);
//        otherExam.addAssociatedCurricularCourseScope(curricularCourseScope);

        curricularCourseScopesToAssociate = new ArrayList<CurricularCourseScope>(1);
        curricularCourseScopesToAssociate.add(curricularCourseScope);

        rooms = new ArrayList<OldRoom>(2);

        OldRoom roomToAssociate1 = new OldRoom();
        roomToAssociate1.setIdInternal(3);

        OldRoom roomToAssociate2 = new OldRoom();
        roomToAssociate2.setIdInternal(4);

        rooms.add(roomToAssociate1);
        rooms.add(roomToAssociate2);

        examDate = createDate(2005, 10, 10);
        examStartTime = createHour(10, 0);
        examEndTime = createHour(10, 0);
    }

    private void checkExamAttributes(final Exam examToCheck, final Date examDate,
            final Date examStartTime, final Date examEndTime, final Season newSeason,
            List<ExecutionCourse> associatedExecutionCourses,
            List<CurricularCourseScope> associatedCurricularCourseScope) {
        assertEquals("Unexpected exam date!", examToCheck.getDayDate(), examDate);
        assertEquals("Unexpected exam start time!", examToCheck.getBeginningDate(), examStartTime);
        assertEquals("Unexpected exam end time!", examToCheck.getEndDate(), examEndTime);
        assertEquals("Unexpected exam season!", examToCheck.getSeason(), newSeason);

        assertTrue("Unexpected execution course associations!", examToCheck
                .getAssociatedExecutionCourses().containsAll(associatedExecutionCourses));
        assertTrue("Unexpected curricular course scope associations!", examToCheck
                .getAssociatedCurricularCourseScope().containsAll(associatedCurricularCourseScope));
    }

    private void checkExamAssociationsSize(final int executionCoursesToAssociateSize,
            final int expectedExecutionCoursesToAssociateSize,
            final int curricularCourseScopesToAssociateSize,
            final int expectedCurricularCourseScopesToAssociateSize,
            final int executionCourseEvaluationsSize,
            final int curricularCourseScopeEvaluationsSize) {
        assertEquals("Unexpected ExecutionCourse size!", executionCoursesToAssociateSize,
                expectedExecutionCoursesToAssociateSize);
        final ExecutionCourse executionCourse = executionCoursesToAssociate.get(0);
        assertEquals("Unexpected ExecutionCourse Evaluations size!", executionCourse
                .getAssociatedEvaluationsCount(), executionCourseEvaluationsSize);

        assertEquals("Unexpected CurricularCourseScope size!", curricularCourseScopesToAssociateSize,
                expectedCurricularCourseScopesToAssociateSize);
        final CurricularCourseScope curricularCourseScope = curricularCourseScopesToAssociate.get(0);
        assertEquals("Unexpected CurricularCourseScope Evaluations size!", curricularCourseScope
                .getAssociatedWrittenEvaluationsCount(), curricularCourseScopeEvaluationsSize);
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private Calendar createHour(int hour, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar;
    }
}
