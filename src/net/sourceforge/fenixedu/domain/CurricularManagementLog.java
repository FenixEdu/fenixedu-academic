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

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.CURRICULAR;
    }

}
