package net.sourceforge.fenixedu.domain;

public class GroupsAndShiftsManagementLog extends GroupsAndShiftsManagementLog_Base {

    public GroupsAndShiftsManagementLog() {
	super();
    }

    public GroupsAndShiftsManagementLog(ExecutionCourse ec, String description) {
	super();
	if (getExecutionCourse() == null) {
	    setExecutionCourse(ec);
	}
	setDescription(description);
    }

    private static GroupsAndShiftsManagementLog createGroupsAndShiftsManagementLog(ExecutionCourse ec, String description) {
	return new GroupsAndShiftsManagementLog(ec, description);
    }

    public static GroupsAndShiftsManagementLog createLog(ExecutionCourse ec, String bundle, String key, String... args) {
	final String label = generateLabelDescription(bundle, key, args);
	return createGroupsAndShiftsManagementLog(ec, label);
    }

    @Override
    public ExecutionCourseLogTypes getExecutionCourseLogType() {
	return ExecutionCourseLogTypes.GROUPS_AND_SHIFTS;
    }

}
