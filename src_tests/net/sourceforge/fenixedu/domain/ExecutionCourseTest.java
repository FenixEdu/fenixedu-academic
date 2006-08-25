/*
 * Created on Jun 29, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ExecutionCourseTest extends DomainTestBase {

    private ExecutionCourse executionCourse;
	private ExecutionCourse executionCourseToReadFrom = null;
	private Registration thisStudent = null;
	private Attends attendsForThisStudent = null;
    private Shift shift;
    private OldRoom room;
    private Teacher teacher;
    private Professorship professorship;
    
    /*CurricularCourse curricularCourse;
    ExecutionPeriod executionPeriod;*/

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();

        executionCourse.setNome("name");
        executionCourse.setSigla("acronym");
        executionCourse.setTheoreticalHours(4.0);
        executionCourse.setTheoPratHours(0.0);
        executionCourse.setPraticalHours(2.0);
        executionCourse.setLabHours(0.0);
        executionCourse.setComment("comment");
        
        setUpForGetAttendsByStudentCase();

//        shift = new Shift();

        room = new OldRoom();

//        teacher = new Teacher();

        professorship = new Professorship();
        professorship.setExecutionCourse(this.executionCourse);
        professorship.setTeacher(this.teacher);
        
        setUpGetActiveEnrollmentEvaluations();
    }

    private void setUpForGetAttendsByStudentCase() {
		executionCourseToReadFrom = new ExecutionCourse();
		thisStudent = new Registration();
		attendsForThisStudent = new Attends();
		
		attendsForThisStudent.setAluno(thisStudent);
		executionCourseToReadFrom.addAttends(attendsForThisStudent);
		
		Attends otherAttends1 = new Attends();
		otherAttends1.setAluno(new Registration());
		executionCourseToReadFrom.addAttends(otherAttends1);
		
		Attends otherAttends2 = new Attends();
		otherAttends2.setAluno(new Registration());
		executionCourseToReadFrom.addAttends(otherAttends2);
	}
    
    private void setUpGetActiveEnrollmentEvaluations() {
        /* Setup curricular course */
        CurricularCourse curricularCourse = new CurricularCourse();
        
        executionCourse.addAssociatedCurricularCourses(curricularCourse);
        curricularCourse.addAssociatedExecutionCourses(executionCourse);
        
        /* Setup execution period */
        ExecutionPeriod executionPeriod = new ExecutionPeriod();
        
        executionCourse.setExecutionPeriod(executionPeriod);
        
        /* Setup enrollments */
        Enrolment annuledEnrollment = new Enrolment();
        annuledEnrollment.setEnrollmentState(EnrollmentState.ANNULED);

        Enrolment numericApprovedEnrollment = new Enrolment();
        numericApprovedEnrollment.setEnrollmentState(EnrollmentState.APROVED);
        
        Enrolment apApprovedEnrollment = new Enrolment();
        apApprovedEnrollment.setEnrollmentState(EnrollmentState.APROVED);

        Enrolment reprovedEnrollment = new Enrolment();
        reprovedEnrollment.setEnrollmentState(EnrollmentState.NOT_APROVED);
        
        Enrolment notEvaluatedEnrollment = new Enrolment();
        notEvaluatedEnrollment.setEnrollmentState(EnrollmentState.NOT_EVALUATED);
        
        /* Setup enrollment evaluations */
        EnrolmentEvaluation numericFirstEnrolmentEvaluation = new EnrolmentEvaluation();
        numericFirstEnrolmentEvaluation.setGrade("10");
        
        EnrolmentEvaluation numericSecondEnrolmentEvaluation = new EnrolmentEvaluation();
        numericSecondEnrolmentEvaluation.setGrade("11");
        
        numericFirstEnrolmentEvaluation.setEnrolment(numericApprovedEnrollment);
        numericSecondEnrolmentEvaluation.setEnrolment(numericApprovedEnrollment);
        
        EnrolmentEvaluation apFirstEnrolmentEvaluation = new EnrolmentEvaluation();
        apFirstEnrolmentEvaluation.setGrade("AP");
        
        apFirstEnrolmentEvaluation.setEnrolment(apApprovedEnrollment);
        
        EnrolmentEvaluation reprovedEnrollmentEvaluation = new EnrolmentEvaluation();
        reprovedEnrollmentEvaluation.setGrade("RE");
        
        reprovedEnrollmentEvaluation.setEnrolment(reprovedEnrollment);
        
        EnrolmentEvaluation notEvaluatedEnrollmentEvaluation = new EnrolmentEvaluation();
        notEvaluatedEnrollmentEvaluation.setGrade("NA");
        
        notEvaluatedEnrollmentEvaluation.setEnrolment(notEvaluatedEnrollment);
        
        /* Setup enrollments execution periods */
        annuledEnrollment.setExecutionPeriod(executionPeriod);
        numericApprovedEnrollment.setExecutionPeriod(executionPeriod);
        apApprovedEnrollment.setExecutionPeriod(executionPeriod);
        reprovedEnrollment.setExecutionPeriod(executionPeriod);
        notEvaluatedEnrollment.setExecutionPeriod(executionPeriod);
        
        /* Setup Curricular Course */
        annuledEnrollment.setCurricularCourse(curricularCourse);
        numericApprovedEnrollment.setCurricularCourse(curricularCourse);
        apApprovedEnrollment.setCurricularCourse(curricularCourse);
        reprovedEnrollment.setCurricularCourse(curricularCourse);
        notEvaluatedEnrollment.setCurricularCourse(curricularCourse);
    }

	public void testEdit() {
//        try {
//            executionCourse.edit(null, null, 0.0, 0.0, 0.0, 0.0, null);
//            fail("Expected NullPointerException!");
//        } catch (NullPointerException e) {
//            checkIfExecutionCourseAttributesAreCorrect("name", "acronym", 4.0, 0.0, 2.0, 0.0, "comment");
//        }
//
//        try {
//            executionCourse.edit(null, null, 0.0, 2.0, 0.0, 0.0, "newComment");
//            fail("Expected NullPointerException!");
//        } catch (NullPointerException e) {
//            checkIfExecutionCourseAttributesAreCorrect("name", "acronym", 4.0, 0.0, 2.0, 0.0, "comment");
//        }
//
//        try {
//            executionCourse.edit("newName", null, 0.0, 0.0, 4.0, 0.0, null);
//            fail("Expected NullPointerException!");
//        } catch (NullPointerException e) {
//            checkIfExecutionCourseAttributesAreCorrect("name", "acronym", 4.0, 0.0, 2.0, 0.0, "comment");
//        }
//
//        executionCourse.edit("newName", "newAcronym", 2.0, 1.0, 2.0, 1.0, "newComment");
//        checkIfExecutionCourseAttributesAreCorrect("newName", "newAcronym", 2.0, 1.0, 2.0, 1.0,
//                "newComment");
    }

    public void testCreateSite() {
        executionCourse.setSite(null);

        assertNull("Expected Null Site in ExecutionCourse!", executionCourse.getSite());
        executionCourse.createSite();
        assertNotNull("Expected Not Null Site in ExecutionCourse!", executionCourse.getSite());
        assertTrue("Expected the Same Execution Course in created Site!", executionCourse.getSite()
                .getExecutionCourse().equals(executionCourse));
        assertEquals("Size unexpected in Site Announcements!", 0, executionCourse.getSite()
                .getAssociatedAnnouncementsCount());
        assertEquals("Size unexpected in Site Sections!", 0, executionCourse.getSite()
                .getAssociatedSectionsCount());
    }

    public void testCreateEvaluationMethod() {
        executionCourse.setEvaluationMethod(null);

        assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                .getEvaluationMethod());

        try {
            executionCourse.createEvaluationMethod(null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        try {
            executionCourse.createEvaluationMethod(new MultiLanguageString(Language.en, "evaluationElementsEng"));
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        try {
            executionCourse.createEvaluationMethod(new MultiLanguageString(Language.pt, "evaluationElements"));
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        final MultiLanguageString multiLanguageString = new MultiLanguageString();
        multiLanguageString.setContent(Language.pt, "evaluationElements");
        multiLanguageString.setContent(Language.en, "evaluationElementsEng");
        executionCourse.createEvaluationMethod(multiLanguageString);
        assertNotNull("Expected Not Null EvaluationMethod in ExecutionCourse!", executionCourse
                .getEvaluationMethod());
        assertEquals("Different EvaluationElements in EvaluationMethod!", "evaluationElements",
                executionCourse.getEvaluationMethod().getEvaluationElements().getContent(Language.pt));
        assertEquals("Different EvaluationElementsEng in EvaluationMethod!", "evaluationElementsEng",
                executionCourse.getEvaluationMethod().getEvaluationElements().getContent(Language.en));
        assertTrue("Different ExecutionCourse in EvaluationMethod!", executionCourse
                .getEvaluationMethod().getExecutionCourse().equals(executionCourse));

    }

    public void testCreateBibliographicReference() {
        assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                .getAssociatedBibliographicReferencesCount());
        try {
            executionCourse.createBibliographicReference(null, null, null, null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        try {
            executionCourse.createBibliographicReference("title", null, "reference", null, true);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        try {
            executionCourse.createBibliographicReference(null, "authors", null, "year", true);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        executionCourse.createBibliographicReference("title", "authors", "reference", "year", true);
        assertEquals("Size unexpected for AssociatedBibliographicReference!", 1, executionCourse
                .getAssociatedBibliographicReferencesCount());
        assertEquals("Different Title in BibliographicReference!", "title", executionCourse
                .getAssociatedBibliographicReferences().get(0).getTitle());
        assertEquals("Different Authors in BibliographicReference!", "authors", executionCourse
                .getAssociatedBibliographicReferences().get(0).getAuthors());
        assertEquals("Different Reference in BibliographicReference!", "reference", executionCourse
                .getAssociatedBibliographicReferences().get(0).getReference());
        assertEquals("Different Year in BibliographicReference!", "year", executionCourse
                .getAssociatedBibliographicReferences().get(0).getYear());
        assertTrue("Different Optional in BibliographicReference!", executionCourse
                .getAssociatedBibliographicReferences().get(0).getOptional());
        assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse,
                executionCourse.getAssociatedBibliographicReferences().get(0).getExecutionCourse());
    }

    public void testCreateCourseReport() {
        executionCourse.setCourseReport(null);
        assertNull("Expected Null CourseReport in ExecutionCourse!", executionCourse.getCourseReport());

        try {
            executionCourse.createCourseReport(null);
            fail("Expected Null Pointer Exception");
        } catch (NullPointerException e) {
            assertNull("Expected Null CourseReport in ExecutionCourse!", executionCourse
                    .getCourseReport());
        }

        final Date dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        executionCourse.createCourseReport("report");
        sleep(1000);
        final Date dateAfterCreation = Calendar.getInstance().getTime();

        assertEquals("Different Report in CourseReport!", "report", executionCourse.getCourseReport()
                .getReport());
        assertTrue("Expected LastModificationDate After an initial timestamp!", executionCourse
                .getCourseReport().getLastModificationDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an initial timestamp!", executionCourse
                .getCourseReport().getLastModificationDate().before(dateAfterCreation));
        assertEquals("Different ExecutionCourse in CourseReport!", executionCourse.getCourseReport()
                .getExecutionCourse(), executionCourse);
    }

    public void testCreateSummary() {
        Summary summary;
        Date summaryDate;
        Date summaryHour;
        Date dateBeforeCreation;
        Date dateAfterCreation;
        try {
            Professorship professorshipTest = null;
            executionCourse.createSummary("title", "summaryText", 20, true, professorshipTest);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Unexpected size in associated summaries!", 0, executionCourse
                    .getAssociatedSummariesCount());
        }

        try {
            Teacher teacherTest = null;
            executionCourse.createSummary("title", "summaryText", 20, true, teacherTest);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Unexpected size in associated summaries!", 0, executionCourse
                    .getAssociatedSummariesCount());
        }

        try {
            String teacherNameTest = null;
            executionCourse.createSummary("title", "summaryText", 20, true, teacherNameTest);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Unexpected size in associated summaries!", 0, executionCourse
                    .getAssociatedSummariesCount());
        }

        try {
            executionCourse.createSummary(null, "summaryText", null, true, this.professorship);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Unexpected size in associated summaries!", 0, executionCourse
                    .getAssociatedSummariesCount());
        }

        try {
            executionCourse.createSummary("title", null, 20, null, this.professorship);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Unexpected size in associated summaries!", 0, executionCourse
                    .getAssociatedSummariesCount());
        }

        // Create Summary using Professorship
        summaryDate = SummaryUtils.createSummaryDate(2005, 5, 5);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        summary = executionCourse.createSummary("title", "summaryText", 20, true, this.professorship);
        shift.transferSummary(summary, summaryDate, summaryHour, this.room, true);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        checkIfSummaryAttributesAreCorrect(summary, "title", "summaryText", 20, true, summaryDate,
                summaryHour, this.executionCourse, this.professorship, null, null, this.shift, this.room);
        checkSummaryModificationDate(summary, dateBeforeCreation, dateAfterCreation);
        assertEquals("Unexpected size in associated summaries!", 1, executionCourse
                .getAssociatedSummariesCount());
        assertEquals("Unexpected size in associated summaries!", 1, shift.getAssociatedSummariesCount());

        // Try to insert a summary to a shift that already exit
        try {
            shift.transferSummary(summary, summaryDate, summaryHour, this.room, true);
            fail("Expected DomainException: summary already exist!");
        } catch (DomainException e) {
            assertEquals("Unexpected size in associated summaries!", 1, executionCourse
                    .getAssociatedSummariesCount());
            assertEquals("Unexpected size in associated summaries!", 1, shift
                    .getAssociatedSummariesCount());
        }

        // Create Summary using Teacher
        summaryDate = SummaryUtils.createSummaryDate(2005, 6, 5);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        summary = executionCourse.createSummary("title", "summaryText", 20, true, this.teacher);
        shift.transferSummary(summary, summaryDate, summaryHour, this.room, true);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        checkIfSummaryAttributesAreCorrect(summary, "title", "summaryText", 20, true, summaryDate,
                summaryHour, this.executionCourse, null, this.teacher, null, this.shift, this.room);
        checkSummaryModificationDate(summary, dateBeforeCreation, dateAfterCreation);
        assertEquals("Unexpected size in associated summaries!", 2, executionCourse
                .getAssociatedSummariesCount());
        assertEquals("Unexpected size in associated summaries!", 2, shift.getAssociatedSummariesCount());

        // Create Summary using TeacherName
        summaryDate = SummaryUtils.createSummaryDate(2005, 8, 5);
        summaryHour = SummaryUtils.createSummaryHour(11, 0);
        dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        summary = executionCourse.createSummary("title", "summaryText", 20, true, "JPNF");
        shift.transferSummary(summary, summaryDate, summaryHour, this.room, true);
        sleep(1000);
        dateAfterCreation = Calendar.getInstance().getTime();
        checkIfSummaryAttributesAreCorrect(summary, "title", "summaryText", 20, true, summaryDate,
                summaryHour, this.executionCourse, null, null, "JPNF", this.shift, this.room);
        checkSummaryModificationDate(summary, dateBeforeCreation, dateAfterCreation);
        assertEquals("Unexpected size in associated summaries!", 3, executionCourse
                .getAssociatedSummariesCount());
        assertEquals("Unexpected size in associated summaries!", 3, shift.getAssociatedSummariesCount());
    }
    
    public void testGetAttendsByStudent() {
		Attends attends = executionCourseToReadFrom.getAttendsByStudent(thisStudent);
		
		assertEquals(attends,attendsForThisStudent);
	}

    private void checkIfExecutionCourseAttributesAreCorrect(final String name, final String acronym,
            final double theoreticalHours, final double theoreticalPraticalHours,
            final double praticalHours, final double laboratoryHours, final String comment) {

        assertEquals("Different ExecutionCourse Name!", name, executionCourse.getNome());
        assertEquals("Different ExecutionCourse Acronym!", acronym, executionCourse.getSigla());
        assertEquals("Different ExecutionCourse Theoretical Hours!", theoreticalHours, executionCourse
                .getTheoreticalHours());
        assertEquals("Different ExecutionCourse TheoreticalPratical Hours!", theoreticalPraticalHours,
                executionCourse.getTheoPratHours());
        assertEquals("Different ExecutionCourse Pratical Hours!", praticalHours, executionCourse
                .getPraticalHours());
        assertEquals("Different ExecutionCourse Laboratory Hours!", laboratoryHours, executionCourse
                .getLabHours());
        assertEquals("Different ExecutionCourse Comment!", comment, executionCourse.getComment());
    }

    private void checkIfSummaryAttributesAreCorrect(final Summary summary, final String title,
            final String summaryText, final Integer studentsNumber, final Boolean isExtraLesson,
            final Date summaryDate, final Date summaryHour, final ExecutionCourse executionCourse,
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

    private void checkSummaryModificationDate(final Summary summary, final Date dateBeforeEdition,
            final Date dateAfterEdition) {
        assertTrue("Expected ModificationDate After an initial timestamp", summary.getLastModifiedDate()
                .after(dateBeforeEdition));
        assertTrue("Expected ModificationDate Before an initial timestamp", summary
                .getLastModifiedDate().before(dateAfterEdition));
    }

    public void testGetActiveEnrollmentEvaluations() {
//        assertEquals("Active Enrollment Evaluations Count", 5, executionCourse.getActiveEnrollmentEvaluations().size());    
    }
}
