package net.sourceforge.fenixedu.domain;

public class CurricularManagementLog extends CurricularManagementLog_Base {

    public CurricularManagementLog() {
	super();
    }

    public CurricularManagementLog(ExecutionCourse ec, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(ec);
	}
	setDescription(description);
    }

    public static CurricularManagementLog createCurricularManagementLog(ExecutionCourse ec, String description) {
	return new CurricularManagementLog(ec, description);
    }

    public static CurricularManagementLog createLog(ExecutionCourse ec, String bundle, String key, String... args) {
	final String label = generateLabelDescription(bundle, key, args);
	return createCurricularManagementLog(ec, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.CURRICULAR;
    }

}
