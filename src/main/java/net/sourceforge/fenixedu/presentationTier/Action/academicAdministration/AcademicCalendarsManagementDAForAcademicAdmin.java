package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import net.sourceforge.fenixedu.presentationTier.Action.manager.AcademicCalendarsManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/academicCalendarsManagement", input = "/index.do",
        attribute = "academicCalendarsManagementForm", formBean = "academicCalendarsManagementForm", scope = "request",
        parameter = "method")
@Forwards(
        value = {
                @Forward(name = "viewAcademicCalendar",
                        path = "/academicAdministration/academicCalendarsManagement/viewAcademicCalendar.jsp"),
                @Forward(name = "prepareCreateCalendarEntry",
                        path = "/academicAdministration/academicCalendarsManagement/createCalendarEntry.jsp"),
                @Forward(name = "prepareChooseCalendar",
                        path = "/academicAdministration/academicCalendarsManagement/chooseCalendar.jsp") })
public class AcademicCalendarsManagementDAForAcademicAdmin extends AcademicCalendarsManagementDA {
}
