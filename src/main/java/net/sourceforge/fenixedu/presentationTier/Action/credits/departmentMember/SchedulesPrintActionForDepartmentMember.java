package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.credits.SchedulesPrintAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/schedulesPrint", functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards(value = { @Forward(name = "show-schedules-resume-print", path = "/credits/schedulesPrint/schedules.jsp") })
public class SchedulesPrintActionForDepartmentMember extends SchedulesPrintAction {
}