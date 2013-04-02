package net.sourceforge.fenixedu.presentationTier.Action.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/academicCalendarsManagement", input = "/index.do",
        attribute = "academicCalendarsManagementForm", formBean = "academicCalendarsManagementForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "viewAcademicCalendar", path = "/manager/academicCalendarsManagement/viewAcademicCalendar.jsp"),
        @Forward(name = "prepareCreateCalendarEntry", path = "/manager/academicCalendarsManagement/createCalendarEntry.jsp"),
        @Forward(name = "prepareChooseCalendar", path = "/manager/academicCalendarsManagement/chooseCalendar.jsp") })
public class AcademicCalendarsManagementDAForManager extends AcademicCalendarsManagementDA {
}
