package net.sourceforge.fenixedu.domain;

public class CurricularManagementLog extends CurricularManagementLog_Base {

    public CurricularManagementLog() {
	super();
    }

    public CurricularManagementLog(ExecutionCourse executionCourse, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(executionCourse);
	}
	setDescription(description);
    }

    public static CurricularManagementLog createCurricularManagementLog(ExecutionCourse executionCourse, String description) {
	return new CurricularManagementLog(executionCourse, description);
    }

    public static CurricularManagementLog createLog(ExecutionCourse executionCourse, String bundle, String key, String... args) {
	final String label = generateLabelDescription(bundle, key, args);
	return createCurricularManagementLog(executionCourse, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.CURRICULAR;
    }

}
