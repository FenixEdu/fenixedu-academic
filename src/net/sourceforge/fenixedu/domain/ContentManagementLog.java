package net.sourceforge.fenixedu.domain;

public class ContentManagementLog extends ContentManagementLog_Base {

    public ContentManagementLog() {
	super();
    }

    public ContentManagementLog(ExecutionCourse executionCourse, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(executionCourse);
	}
	setDescription(description);
    }

    public static ContentManagementLog createContentManagementLog(ExecutionCourse executionCourse, String description) {
	return new ContentManagementLog(executionCourse, description);
    }

    public static ContentManagementLog createLog(ExecutionCourse executionCourse, String bundle, String key, String... args) {
	final String label = generateLabelDescription(bundle, key, args);
	return createContentManagementLog(executionCourse, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.CONTENT;
    }
}
