package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/schedulesPrint", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-schedules-resume-print", path = "show-schedules-resume-print") })
public class SchedulesPrintActionForDepartmentMember extends
		net.sourceforge.fenixedu.presentationTier.Action.credits.SchedulesPrintAction {
}