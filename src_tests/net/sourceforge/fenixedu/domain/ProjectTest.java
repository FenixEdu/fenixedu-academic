/*
 * Created on Nov 10, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class ProjectTest extends DomainTestBase {

    private ExecutionCourse executionCourse;
    private Project project;    
    private Date begin, end;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(1);

        begin = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "11/10/2005 10:0");
        end = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "1/1/2006 12:0");

//        project = new Project("name", begin, end, "description", executionCourse);
        project.setIdInternal(1);
    }

    public void testCreateProject() {
        Project newProject = null;
        Date newBegin = null;
        Date newEnd = null;
        Date wrongEnd = null;
        try {
            newBegin = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "1/9/2005 10:00");
            newEnd = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "29/1/2006 14:0");
            wrongEnd = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "1/8/2006 14:0");
        } catch (ParseException e1) {
            fail("Unexpected error creating project dates!");
        }

//        try {
//            new Project(null, null, newEnd, null, this.executionCourse);
//            fail("Expected NullPointerException!");
//        } catch (final NullPointerException e) {
//            assertEquals("Unexpected Associated Evaluations Count!", executionCourse
//                    .getAssociatedEvaluationsCount(), 1);
//        }
//
//        try {
//            new Project("projectName", null, null, null, this.executionCourse);
//            fail("Expected NullPointerException!");
//        } catch (final NullPointerException e) {
//            assertEquals("Unexpected Associated Evaluations Count!", executionCourse
//                    .getAssociatedEvaluationsCount(), 1);
//        }
//
//        try {
//            new Project("projectName", null, newEnd, null, this.executionCourse);
//            fail("Expected NullPointerException!");
//        } catch (final NullPointerException e) {
//            assertEquals("Unexpected Associated Evaluations Count!", executionCourse
//                    .getAssociatedEvaluationsCount(), 1);
//        }
//        
//        try {
//            new Project("projectName", newBegin, wrongEnd, null, this.executionCourse);
//            fail("Expected DomainException: BeginDate is After EndDate!");
//        } catch (final DomainException e) {
//            assertEquals("Unexpected Associated Evaluations Count!", executionCourse
//                    .getAssociatedEvaluationsCount(), 1);
//        }
//
//        newProject = new Project("projectName", newBegin, newEnd, null, this.executionCourse);
//        checkIfProjectAttributesAreCorrect(newProject, "projectName", newBegin, newEnd, "",
//                this.executionCourse);
    }

    public void testEdit() {
        Date editBegin = null;
        Date editEnd = null;
        Date wrongEnd = null;
        try {
            editBegin = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "1/9/2005 10:00");
            editEnd = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "29/1/2006 14:0");
            wrongEnd = DateFormatUtil.parse("dd/MM/yyyy HH:mm", "1/8/2006 14:0");
        } catch (ParseException e1) {
            fail("Unexpected error creating project dates!");
        }

//        try {
//            project.edit(null, null, null, "descriptionEdited");
//            fail("Expected NullPointerException!");
//        } catch (final NullPointerException e) {
//            checkIfProjectAttributesAreCorrect(this.project, "name", this.begin, this.end, "description",
//                    this.executionCourse);
//        }
//
//        try {
//            project.edit("nameEdited", null, editEnd, null);
//            fail("Expected NullPointerException!");
//        } catch (final NullPointerException e) {
//            checkIfProjectAttributesAreCorrect(this.project, "name", this.begin, this.end, "description",
//                    this.executionCourse);
//        }
//
//        try {
//            project.edit(null, editBegin, editEnd, "descriptionEdited");
//            fail("Expected NullPointerException!");
//        } catch (final NullPointerException e) {
//            checkIfProjectAttributesAreCorrect(this.project, "name", this.begin, this.end, "description",
//                    this.executionCourse);
//        }
//        
//        try {
//            project.edit("nameEdited", editBegin, wrongEnd, "descriptionEdited");
//            fail("Expected DomainException: BeginDate is After EndDate!");
//        } catch (final DomainException e) {
//            checkIfProjectAttributesAreCorrect(this.project, "name", this.begin, this.end, "description",
//                    this.executionCourse);
//        }
//        
//        project.edit("nameEdited", editBegin, editEnd, "descriptionEdited");
//        checkIfProjectAttributesAreCorrect(this.project, "nameEdited", editBegin, editEnd, "descriptionEdited",
//                this.executionCourse);
    }

    public void testDelete() {
        assertEquals("Unexpected evaluations number in ExecutionCourse!", executionCourse
                .getAssociatedEvaluationsCount(), 1);
        project.delete();
        assertEquals("Unexpected evaluations number in ExecutionCourse!", executionCourse
                .getAssociatedEvaluationsCount(), 0);
    }

    private void checkIfProjectAttributesAreCorrect(final Project project, final String projectName,
            final Date newBegin, final Date newEnd, final String description,
            final ExecutionCourse executionCourse) {
        assertEquals("Different Project Name!", projectName, project.getName());
        assertEquals("Different Project Begin Date!", newBegin, project.getBegin());
        assertEquals("Different Project End Date!", newEnd, project.getEnd());
        assertEquals("Different Project Description!", description, project.getDescription());
        assertEquals("Different Project ExecutionCourse!", executionCourse, project
                .getAssociatedExecutionCourses().get(0));
    }

}
