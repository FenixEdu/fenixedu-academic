package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.QUCAuditorDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/qucAudit", module = "student")
@Forwards({ @Forward(name = "viewAuditProcesses", path = "/student/inquiries/viewAuditProcesses.jsp"),
        @Forward(name = "viewProcessDetails", path = "/student/inquiries/viewProcessDetails.jsp"),
        @Forward(name = "editProcess", path = "/student/inquiries/editProcess.jsp"),
        @Forward(name = "manageAuditFiles", path = "/student/inquiries/manageAuditFiles.jsp") })
public class QUCStudentAuditorDA extends QUCAuditorDA {

    @Override
    protected List<ExecutionCourseAudit> getExecutionCoursesAudit(ExecutionSemester executionSemester) {
        Student student = AccessControl.getPerson().getStudent();
        return student.getExecutionCourseAudits(executionSemester);
    }

    @Override
    protected boolean isTeacher() {
        return false;
    }

    @Override
    protected String getApprovedSelf() {
        return "approvedByStudent";
    }

    @Override
    protected String getApprovedOther() {
        return "approvedByTeacher";
    }
}
