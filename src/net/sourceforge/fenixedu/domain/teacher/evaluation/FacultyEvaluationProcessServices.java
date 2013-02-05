package net.sourceforge.fenixedu.domain.teacher.evaluation;

import pt.ist.fenixWebFramework.services.Service;

public class FacultyEvaluationProcessServices {

    @Service
    public void delete(final FacultyEvaluationProcess facultyEvaluationProcess) {
        facultyEvaluationProcess.delete();
    }

    @Service
    public static void publish(final FacultyEvaluationProcess facultyEvaluationProcess) {
        facultyEvaluationProcess.setAreApprovedMarksPublished(true);
    }

    @Service
    public static void unPublish(final FacultyEvaluationProcess facultyEvaluationProcess) {
        facultyEvaluationProcess.setAreApprovedMarksPublished(false);
    }

}
