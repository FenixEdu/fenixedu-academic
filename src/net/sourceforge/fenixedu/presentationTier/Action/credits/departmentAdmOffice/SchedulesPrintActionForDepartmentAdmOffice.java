package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/schedulesPrint", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-schedules-resume-print", path = "show-schedules-resume-print"),
		@Forward(name = "show-empty-schedules-resume-print", path = "show-empty-schedules-resume-print") })
public class SchedulesPrintActionForDepartmentAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.credits.SchedulesPrintAction {
}