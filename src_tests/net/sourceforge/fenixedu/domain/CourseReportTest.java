/*
 * Created on Jul 13, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.gesdis.CourseReport;

public class CourseReportTest extends DomainTestBase {

    private CourseReport courseReport;
    private ExecutionCourse executionCourse;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse("name", "acronym", ExecutionPeriod.readActualExecutionPeriod());
        executionCourse.setIdInternal(1);

        courseReport = new CourseReport();
        courseReport.setIdInternal(1);
        courseReport.setReport("report");
        courseReport.setLastModificationDate(Calendar.getInstance().getTime());
        courseReport.setExecutionCourse(executionCourse);
    }

    public void testEdit() {
        try {
            courseReport.edit(null);
            fail("Expected Null Pointer Exception!");
        } catch (NullPointerException e) {
            checkIfCourseReportAttributesAreCorrect("report", this.executionCourse);            
        }

        final Date dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        courseReport.edit("newReport");
        sleep(1000);
        final Date dateAfterEdition = Calendar.getInstance().getTime();

        checkIfCourseReportAttributesAreCorrect("newReport", this.executionCourse);
        assertTrue("Expected LastModificationDate After an initial timestamp!", courseReport
                .getLastModificationDate().after(dateBeforeEdition));
        assertTrue("Expected LastModificationDate Before an initial timestamp!", courseReport
                .getLastModificationDate().before(dateAfterEdition));
    }

    public void testDelete() {
        assertEquals("Different ExecutionCourse in CourseReport!", courseReport.getExecutionCourse(),
                executionCourse);
        courseReport.delete();
        assertNull("Expected Null ExecutionCourse in CourseReport!", courseReport.getExecutionCourse());
    }
    
    private void checkIfCourseReportAttributesAreCorrect(final String report, final ExecutionCourse executionCourse) {
        assertEquals("Different Report in CourseReport!", report, courseReport.getReport());
        assertEquals("Different ExecutionCourse in CourseReport!", courseReport.getExecutionCourse(), executionCourse);
        
    }

}
