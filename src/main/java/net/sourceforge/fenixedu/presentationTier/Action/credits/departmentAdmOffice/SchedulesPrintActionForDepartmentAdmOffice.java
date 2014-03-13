package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.credits.SchedulesPrintAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/schedulesPrint", functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
@Forwards({ @Forward(name = "show-schedules-resume-print", path = "/credits/schedulesPrint/schedules.jsp"),
        @Forward(name = "show-empty-schedules-resume-print", path = "/credits/schedulesPrint/emptySchedules.jsp") })
public class SchedulesPrintActionForDepartmentAdmOffice extends SchedulesPrintAction {
}