package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup extends
PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup_Base {
    protected PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public Group toGroup() {
        return StudentSharingDegreeOfCompetenceOfExecutionCourseGroup.get(getExecutionCourse());
    }

    public static PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        return singleton(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse,
                () -> new PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(executionCourse));
    }
}
