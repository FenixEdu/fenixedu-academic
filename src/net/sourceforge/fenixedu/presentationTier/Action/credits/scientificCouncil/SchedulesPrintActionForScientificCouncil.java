package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/schedulesPrint", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-schedules-resume-print", path = "show-schedules-resume-print"),
		@Forward(name = "show-empty-schedules-resume-print", path = "show-empty-schedules-resume-print") })
public class SchedulesPrintActionForScientificCouncil extends
		net.sourceforge.fenixedu.presentationTier.Action.credits.SchedulesPrintAction {
}