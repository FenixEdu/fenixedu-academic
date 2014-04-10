package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentStudentSharingDegreeOfExecutionCourseGroup extends
        PersistentStudentSharingDegreeOfExecutionCourseGroup_Base {
    protected PersistentStudentSharingDegreeOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public Group toGroup() {
        return StudentSharingDegreeOfExecutionCourseGroup.get(getExecutionCourse());
    }

    public static PersistentStudentSharingDegreeOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentSharingDegreeOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentStudentSharingDegreeOfExecutionCourseGroup(executionCourse);
    }

}
