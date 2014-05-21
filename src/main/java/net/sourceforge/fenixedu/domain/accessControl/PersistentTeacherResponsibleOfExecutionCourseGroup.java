package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentTeacherResponsibleOfExecutionCourseGroup extends PersistentTeacherResponsibleOfExecutionCourseGroup_Base {
    protected PersistentTeacherResponsibleOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public Group toGroup() {
        return TeacherResponsibleOfExecutionCourseGroup.get(getExecutionCourse());
    }

    public static PersistentTeacherResponsibleOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        return singleton(PersistentTeacherResponsibleOfExecutionCourseGroup.class, executionCourse,
                () -> new PersistentTeacherResponsibleOfExecutionCourseGroup(executionCourse));
    }
}
