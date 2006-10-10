/*
 * Created on Jul 20, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class SummaryTest extends DomainTestBase {

    private ExecutionCourse executionCourse;

    private Teacher teacher;

    private Professorship professorship;

    private Shift shift;

    private OldRoom room;

    private Summary summary;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(1);

//        teacher = new Teacher();
        teacher.setIdInternal(1);

        professorship = new Professorship();
        professorship.setIdInternal(1);
        professorship.setExecutionCourse(executionCourse);
        professorship.setTeacher(teacher);

//        shift = new Shift();
        shift.setIdInternal(1);

        room = new OldRoom();
        room.setIdInternal(1);

        summary = new Summary();
        summary.setIdInternal(1);
        summary.setTitle(new MultiLanguageString("title"));
        summary.setSummaryText(new MultiLanguageString("summaryText"));
        summary.setStudentsNumber(20);
        summary.setIsExtraLesson(true);
        summary.setLastModifiedDate(Calendar.getInstance().getTime());
        summary.setTeacherName(null);

        summary.setSummaryDate(SummaryUtils.createSummaryDate(2005, 5, 5));
        summary.setSummaryHour(SummaryUtils.createSummaryHour(10, 0));

        summary.setExecutionCourse(executionCourse);
        summary.setProfessorship(professorship);
        summary.setTeacher(null);
        summary.setRoom(room);
        summary.setShift(shift);

    }

    public void testEdit() {
        Date summaryDate = summary.getSummaryDate();
        Date summaryHour = summary.getSummaryHour();

        try {
            Professorship professorshipTest = null;
//            summary.edit("newTitle", "newSummaryText", 40, false, professorshipTest);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfSummaryAttributesAreCorrect(this.summary, "title", "summaryText", 20, true, summaryDate,
                    summaryHour, this.executionCourse, this.professorship, null, null, this.shift,
                    this.room);
        }

        try {
            Teacher teacherTest = null;
//            summary.edit("newTitle", "newSummaryText", 40, false, teacherTest);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfSummaryAttributesAreCorrect(this.summary, "title", "summaryText", 20, true, summaryDate,
                    summaryHour, this.executionCourse, this.professorship, null, null, this.shift,
                    this.room);
        }

        try {
            String teacherName = null;
//            summary.edit("newTitle", "newSummaryText", 40, false, teacherName);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfSummaryAttributesAreCorrect(this.summary, "title", "summaryText", 20, true, summaryDate,
                    summaryHour, this.executionCourse, this.professorship, null, null, this.shift,
                    this.room);
        }

        try {
//            summary.edit("newTitle", null, 40, null, "");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfSummaryAttributesAreCorrect(this.summary, "title", "summaryText", 20, true, summaryDate,
                    summaryHour, this.executionCourse, this.professorship, null, null, this.shift,
                    this.room);
        }

        try {
//            shift.transferSummary(summary, summaryDate, summaryHour, this.room, true);
            fail("Expected DomainException: summary already exists!");
        } catch (DomainException e) {
            checkIfSummaryAttributesAreCorrect(this.summary, "title", "summaryText", 20, true, summaryDate,
                    summaryHour, this.executionCourse, this.professorship, null, null, this.shift,
                    this.room);
        }

        // Edit Summary using Professorship
        Date dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        summaryDate = SummaryUtils.createSummaryDate(2005, 5, 6);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
//        shift.transferSummary(summary, summaryDate, summaryHour, this.room, false);
//        summary.edit("newTitle", "newSummaryText", 40, false, this.professorship);
        sleep(1000);
        Date dateAfterEdition = Calendar.getInstance().getTime();
        checkIfSummaryAttributesAreCorrect(this.summary, "newTitle", "newSummaryText", 40, false, summaryDate,
                summaryHour, this.executionCourse, this.professorship, null, null, this.shift, this.room);
        checkSummaryModificationDate(this.summary, dateBeforeEdition, dateAfterEdition);
        assertEquals("Unexpected Shift Summary size!", 1, shift.getAssociatedSummariesCount());

        // Edit Summary using Teacher
        dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        summaryDate = SummaryUtils.createSummaryDate(2005, 5, 7);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
//        shift.transferSummary(summary, summaryDate, summaryHour, this.room, false);
//        summary.edit("newTitle2", "newSummaryText2", 30, false, this.teacher);
        sleep(1000);
        dateAfterEdition = Calendar.getInstance().getTime();
        checkIfSummaryAttributesAreCorrect(this.summary, "newTitle2", "newSummaryText2", 30, false, summaryDate,
                summaryHour, this.executionCourse, null, this.teacher, null, this.shift, this.room);
        checkSummaryModificationDate(this.summary, dateBeforeEdition, dateAfterEdition);
        assertEquals("Unexpected Shift Summary size!", 1, shift.getAssociatedSummariesCount());

        // Edit Summary using TeacherNumber
        dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        summaryDate = SummaryUtils.createSummaryDate(2005, 5, 8);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
//        shift.transferSummary(summary, summaryDate, summaryHour, this.room, false);
//        summary.edit("newTitle3", "newSummaryText3", 40, true, "JPNF");
        sleep(1000);
        dateAfterEdition = Calendar.getInstance().getTime();
        checkIfSummaryAttributesAreCorrect(this.summary, "newTitle3", "newSummaryText3", 40, true, summaryDate,
                summaryHour, this.executionCourse, null, null, "JPNF", this.shift, this.room);
        checkSummaryModificationDate(this.summary, dateBeforeEdition, dateAfterEdition);
        assertEquals("Unexpected Shift Summary size!", 1, shift.getAssociatedSummariesCount());

        // Change Summary Shift
//        Shift alternativeShift = new Shift();
//        alternativeShift.setIdInternal(2);

        dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        summaryDate = SummaryUtils.createSummaryDate(2005, 5, 10);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
//        alternativeShift.transferSummary(summary, summaryDate, summaryHour, this.room, false);
//        summary.edit("newTitle3", "newSummaryText3", 40, true, "JPNF");
        sleep(1000);
        dateAfterEdition = Calendar.getInstance().getTime();
//        checkIfSummaryAttributesAreCorrect(this.summary, "newTitle3", "newSummaryText3", 40, true, summaryDate,
//                summaryHour, this.executionCourse, null, null, "JPNF", alternativeShift, this.room);
//        checkSummaryModificationDate(this.summary, dateBeforeEdition, dateAfterEdition);
//        assertEquals("Unexpected Shift Summary size!", 0, shift.getAssociatedSummariesCount());
//        assertEquals("Unexpected Alternative Shift Summary size!", 1, alternativeShift.getAssociatedSummariesCount());

    }

    public void testDelete() {
        summary.setTeacher(this.teacher);

        assertNotNull("Expected Not Null ExecutionCourse!", summary.getExecutionCourse());
        assertEquals("Unexpected size!", 1, executionCourse.getAssociatedSummariesCount());
        assertNotNull("Expected Not Null Professorship!", summary.getProfessorship());
        assertEquals("Unexpected size!", 1, professorship.getAssociatedSummariesCount());
        assertNotNull("Expected Not Null Teacher!", summary.getTeacher());
        assertEquals("Unexpected size!", 1, teacher.getAssociatedSummariesCount());
        assertNotNull("Expected Not Null OldRoom!", summary.getRoom());
        assertEquals("Unexpected size!", 1, room.getAssociatedSummariesCount());
        assertNotNull("Expected Not Null Shift!", summary.getShift());
        assertEquals("Unexpected size!", 1, shift.getAssociatedSummariesCount());

        summary.delete();

        assertNull("Expected Null ExecutionCourse!", summary.getExecutionCourse());
        assertEquals("Unexpected size!", 0, executionCourse.getAssociatedSummariesCount());
        assertNull("Expected Null Professorship!", summary.getProfessorship());
        assertEquals("Unexpected size!", 0, professorship.getAssociatedSummariesCount());
        assertNull("Expected Null Teacher!", summary.getTeacher());
        assertEquals("Unexpected size!", 0, teacher.getAssociatedSummariesCount());
        assertNull("Expected Null OldRoom!", summary.getRoom());
        assertEquals("Unexpected size!", 0, room.getAssociatedSummariesCount());
        assertNull("Expected Null Shift!", summary.getShift());
        assertEquals("Unexpected size!", 0, shift.getAssociatedSummariesCount());
    }

    private void checkIfSummaryAttributesAreCorrect(final Summary summary, final String title, final String summaryText,
            final Integer studentsNumber, final Boolean isExtraLesson, final Date summaryDate,
            final Date summaryHour, final ExecutionCourse executionCourse,
            final Professorship professorship, final Teacher teacher, final String teacherName,
            final Shift shift, final OldRoom room) {

        assertEquals("Different Summary Title!", title, summary.getTitle());
        assertEquals("Different Summary Text!", summaryText, summary.getSummaryText());
        assertEquals("Different Summary StudentsNumber!", studentsNumber, summary.getStudentsNumber());
        assertEquals("Different Summary Extra Lesson!", isExtraLesson, summary.getIsExtraLesson());
        assertEquals("Different Summary ExecutionCourse!", executionCourse, summary.getExecutionCourse());
        assertEquals("Different Summary Professorship!", professorship, summary.getProfessorship());
        assertEquals("Different Summary Teacher!", teacher, summary.getTeacher());
        assertEquals("Different Summary TeacherName!", teacherName, summary.getTeacherName());
        assertEquals("Different Summary Shift!", shift, summary.getShift());
        assertEquals("Different Summary OldRoom!", room, summary.getRoom());
        assertTrue("Different Summary Date!", summary.getSummaryDate().equals(summaryDate));
        assertTrue("Different Summary Hour!", summary.getSummaryHour().equals(summaryHour));
    }

    private void checkSummaryModificationDate(final Summary summary, final Date dateBeforeEdition, final Date dateAfterEdition) {
        assertTrue("Expected ModificationDate After an initial timestamp", summary.getLastModifiedDate()
                .after(dateBeforeEdition));
        assertTrue("Expected ModificationDate Before an initial timestamp", summary
                .getLastModifiedDate().before(dateAfterEdition));
    }
}