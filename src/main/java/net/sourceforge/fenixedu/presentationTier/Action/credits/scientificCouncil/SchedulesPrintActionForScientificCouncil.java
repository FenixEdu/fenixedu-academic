package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.credits.SchedulesPrintAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/schedulesPrint", functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards(value = { @Forward(name = "show-schedules-resume-print", path = "/credits/schedulesPrint/schedules.jsp"),
        @Forward(name = "show-empty-schedules-resume-print", path = "/credits/schedulesPrint/emptySchedules.jsp") })
public class SchedulesPrintActionForScientificCouncil extends SchedulesPrintAction {
}