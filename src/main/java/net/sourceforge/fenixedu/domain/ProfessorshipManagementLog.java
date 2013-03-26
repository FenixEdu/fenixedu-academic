package net.sourceforge.fenixedu.domain;

public class ProfessorshipManagementLog extends ProfessorshipManagementLog_Base {

    public ProfessorshipManagementLog() {
        super();
    }

    public ProfessorshipManagementLog(ExecutionCourse executionCourse, String description) {
        super();
        if (getExecutionCourse() == null) {
            setExecutionCourse(executionCourse);
        }
        setDescription(description);
    }

    public static ProfessorshipManagementLog createProfessorshipManagementLog(ExecutionCourse executionCourse, String description) {
        return new ProfessorshipManagementLog(executionCourse, description);
    }

    public static ProfessorshipManagementLog createLog(ExecutionCourse executionCourse, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createProfessorshipManagementLog(executionCourse, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
        return ExecutionCourseLogTypes.PROFESSORSHIP;
    }
}
