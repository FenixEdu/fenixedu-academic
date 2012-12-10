package net.sourceforge.fenixedu.domain;

public class EvaluationManagementLog extends EvaluationManagementLog_Base {

    public EvaluationManagementLog() {
	super();
    }

    public EvaluationManagementLog(ExecutionCourse ec, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(ec);
	}
	setDescription(description);
    }

    public static EvaluationManagementLog createEvaluationManagementLog(ExecutionCourse ec, String description) {
	return new EvaluationManagementLog(ec, description);
    }

    public static EvaluationManagementLog createLog(ExecutionCourse ec, String bundle, String key, String... args) {
	final String label = generateLabelDescription(bundle, key, args);
	return createEvaluationManagementLog(ec, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.EVALUATION;
    }
}
