package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(executionCourse);
    }
}
