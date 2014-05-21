package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(PersistentStudentSharingDegreeOfExecutionCourseGroup.class, executionCourse,
                () -> new PersistentStudentSharingDegreeOfExecutionCourseGroup(executionCourse));
    }
}
