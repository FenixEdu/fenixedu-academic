package net.sourceforge.fenixedu.domain;

public class EvaluationManagementLog extends EvaluationManagementLog_Base {

    public EvaluationManagementLog() {
        super();
    }

    public EvaluationManagementLog(ExecutionCourse executionCourse, String description) {
        super();
        if (getExecutionCourse() == null) {
            setExecutionCourse(executionCourse);
        }
        setDescription(description);
    }

    public static EvaluationManagementLog createEvaluationManagementLog(ExecutionCourse executionCourse, String description) {
        return new EvaluationManagementLog(executionCourse, description);
    }

    public static EvaluationManagementLog createLog(ExecutionCourse executionCourse, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createEvaluationManagementLog(executionCourse, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
        return ExecutionCourseLogTypes.EVALUATION;
    }
}
