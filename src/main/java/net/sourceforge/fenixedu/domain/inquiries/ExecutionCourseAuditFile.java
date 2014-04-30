package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

public class ExecutionCourseAuditFile extends ExecutionCourseAuditFile_Base {

    public ExecutionCourseAuditFile(ExecutionCourseAudit executionCourseAudit, String filename, byte[] file) {
        super();
        setExecutionCourseAudit(executionCourseAudit);
        super.init(filename, filename, file, getPermissionGroup());
    }

    private Group getPermissionGroup() {
        Group teacherGroup = UserGroup.of(getExecutionCourseAudit().getTeacherAuditor().getPerson().getUser());
        Group studentGroup = UserGroup.of(getExecutionCourseAudit().getStudentAuditor().getPerson().getUser());
        Group pedagogicalCouncil = RoleGroup.get(RoleType.PEDAGOGICAL_COUNCIL);
        return teacherGroup.or(studentGroup).or(pedagogicalCouncil);
    }

    @Override
    public void delete() {
        setExecutionCourseAudit(null);
        super.delete();
    }

    @Deprecated
    public boolean hasExecutionCourseAudit() {
        return getExecutionCourseAudit() != null;
    }

}
