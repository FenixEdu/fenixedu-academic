package net.sourceforge.fenixedu.domain;

public class ProfessorshipManagementLog extends ProfessorshipManagementLog_Base {

    public ProfessorshipManagementLog() {
	super();
    }

    public ProfessorshipManagementLog(ExecutionCourse ec, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(ec);
	}
	setDescription(description);
    }

    public static ProfessorshipManagementLog createProfessorshipManagementLog(ExecutionCourse ec, String description) {
	return new ProfessorshipManagementLog(ec, description);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.PROFESSORSHIP;
    }
}
