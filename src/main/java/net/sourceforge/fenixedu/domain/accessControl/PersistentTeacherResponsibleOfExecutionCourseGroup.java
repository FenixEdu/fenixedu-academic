package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentTeacherResponsibleOfExecutionCourseGroup instance =
                select(PersistentTeacherResponsibleOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentTeacherResponsibleOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentTeacherResponsibleOfExecutionCourseGroup instance =
                select(PersistentTeacherResponsibleOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentTeacherResponsibleOfExecutionCourseGroup(executionCourse);
    }
}
