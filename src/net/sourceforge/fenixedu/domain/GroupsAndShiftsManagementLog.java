package net.sourceforge.fenixedu.domain;

public class GroupsAndShiftsManagementLog extends GroupsAndShiftsManagementLog_Base {

	public GroupsAndShiftsManagementLog() {
		super();
	}

	public GroupsAndShiftsManagementLog(ExecutionCourse executionCourse, String description) {
		super();
		if (getExecutionCourse() == null) {
			setExecutionCourse(executionCourse);
		}
		setDescription(description);
	}

	private static GroupsAndShiftsManagementLog createGroupsAndShiftsManagementLog(ExecutionCourse executionCourse,
			String description) {
		return new GroupsAndShiftsManagementLog(executionCourse, description);
	}

	public static GroupsAndShiftsManagementLog createLog(ExecutionCourse executionCourse, String bundle, String key,
			String... args) {
		final String label = generateLabelDescription(bundle, key, args);
		return createGroupsAndShiftsManagementLog(executionCourse, label);
	}

	@Override
	public ExecutionCourseLogTypes getExecutionCourseLogType() {
		return ExecutionCourseLogTypes.GROUPS_AND_SHIFTS;
	}

}
