package net.sourceforge.fenixedu.domain;

public class ContentManagementLog extends ContentManagementLog_Base {

    public ContentManagementLog() {
	super();
    }

    public ContentManagementLog(ExecutionCourse ec, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(ec);
	}
	setDescription(description);
    }

    public static ContentManagementLog createContentManagementLog(ExecutionCourse ec, String description) {
	return new ContentManagementLog(ec, description);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.CONTENT;
    }
}
