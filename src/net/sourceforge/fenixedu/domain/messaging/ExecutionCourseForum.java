package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;

public class ExecutionCourseForum extends ExecutionCourseForum_Base {

    public ExecutionCourseForum() {
        super();
    }

    public ExecutionCourseForum(String name, String description) {
        this();
        init(name, description);
    }

    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        if (this.getName() != null) {
            executionCourse.checkIfCanAddForum(this.getName());
        }

        super.setExecutionCourse(executionCourse);
    }

    @Override
    public void setName(String name) {
        if (this.getExecutionCourse() != null) {
            getExecutionCourse().checkIfCanAddForum(name);
        }

        super.setName(name);
    }

    @Override
    public void delete() {
        removeExecutionCourse();
        super.delete();
    }

    @Override
    public void removeExecutionCourse() {
        super.setExecutionCourse(null);
    }

    @Override
    public Group getReadersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getWritersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getAdminGroup() {
        return new ExecutionCourseTeachersGroup(getExecutionCourse());
    }

    private Group getExecutionCourseMembersGroup() {
        return new GroupUnion(new ExecutionCourseTeachersGroup(getExecutionCourse()),
                new ExecutionCourseStudentsGroup(getExecutionCourse()));
    }
}
