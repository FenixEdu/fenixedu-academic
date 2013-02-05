package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/qucAudit", module = "departmentMember")
@Forwards({ @Forward(name = "viewAuditProcesses", path = "/departmentMember/quc/viewAuditProcesses.jsp"),
        @Forward(name = "viewProcessDetails", path = "/departmentMember/quc/viewProcessDetails.jsp"),
        @Forward(name = "editProcess", path = "/departmentMember/quc/editProcess.jsp"),
        @Forward(name = "manageAuditFiles", path = "/departmentMember/quc/manageAuditFiles.jsp") })
public class QUCTeacherAuditorDA extends QUCAuditorDA {

    @Override
    protected List<ExecutionCourseAudit> getExecutionCoursesAudit(ExecutionSemester executionSemester) {
        Teacher teacher = AccessControl.getPerson().getTeacher();
        return teacher.getExecutionCourseAudits(executionSemester);
    }

    @Override
    protected boolean isTeacher() {
        return true;
    }

    @Override
    protected String getApprovedSelf() {
        return "approvedByTeacher";
    }

    @Override
    protected String getApprovedOther() {
        return "approvedByStudent";
    }
}
