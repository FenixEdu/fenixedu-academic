package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ExecutionCourseAuditFile extends ExecutionCourseAuditFile_Base {

    public ExecutionCourseAuditFile(ExecutionCourseAudit executionCourseAudit, String filename, byte[] file) {
        super();
        setExecutionCourseAudit(executionCourseAudit);
        super.init(filename, filename, file, getPermissionGroup());
    }

    private Group getPermissionGroup() {
        PersonGroup teacherGroup = new PersonGroup(getExecutionCourseAudit().getTeacherAuditor().getPerson());
        PersonGroup studentGroup = new PersonGroup(getExecutionCourseAudit().getStudentAuditor().getPerson());
        RoleGroup pedagogicalCouncil = new RoleGroup(RoleType.PEDAGOGICAL_COUNCIL);
        return new GroupUnion(teacherGroup, studentGroup, pedagogicalCouncil);
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
