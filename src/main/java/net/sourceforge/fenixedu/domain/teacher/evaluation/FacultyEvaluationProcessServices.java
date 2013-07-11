package net.sourceforge.fenixedu.domain.teacher.evaluation;

import pt.ist.fenixframework.Atomic;

public class FacultyEvaluationProcessServices {

    @Atomic
    public void delete(final FacultyEvaluationProcess facultyEvaluationProcess) {
        facultyEvaluationProcess.delete();
    }

    @Atomic
    public static void publish(final FacultyEvaluationProcess facultyEvaluationProcess) {
        facultyEvaluationProcess.setAreApprovedMarksPublished(true);
    }

    @Atomic
    public static void unPublish(final FacultyEvaluationProcess facultyEvaluationProcess) {
        facultyEvaluationProcess.setAreApprovedMarksPublished(false);
    }

}
