/*
 * Created on Sep 6, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

public class ExportGroupingTest extends DomainTestBase {

    private ExportGrouping exportGrouping;
    private ExecutionCourse executionCourse;
    private Grouping grouping;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse("name", "acronym", ExecutionPeriod.readActualExecutionPeriod());
        executionCourse.setIdInternal(1);

        grouping = new Grouping();
        grouping.setIdInternal(1);

        exportGrouping = new ExportGrouping();
        exportGrouping.setIdInternal(1);
        exportGrouping.setExecutionCourse(executionCourse);
        exportGrouping.setGrouping(grouping);
    }

    public void testDelete() {

        assertNotNull("Expected Not Null ExecutionCourse!", exportGrouping.getExecutionCourse());
        assertNotNull("Expected Not Null Grouping!", exportGrouping.getGrouping());

        exportGrouping.delete();

        assertNull("Expected Null ExecutionCourse!", exportGrouping.getExecutionCourse());
        assertNull("Expected Null Grouping!", exportGrouping.getGrouping());

        assertEquals("Expected none ExportGroupings in ExecutionCourse!", executionCourse
                .getExportGroupingsCount(), 0);
        assertEquals("Expected none ExportGroupings in Grouping!", grouping.getExportGroupingsCount(), 0);
    }

}
