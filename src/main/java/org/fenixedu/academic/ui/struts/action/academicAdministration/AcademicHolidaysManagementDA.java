package org.fenixedu.academic.ui.struts.action.academicAdministration;

import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCalendarsApp;
import org.fenixedu.academic.ui.struts.action.manager.ManageHolidaysDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AcademicAdminCalendarsApp.class, path = "manage-holidays", titleKey = "label.manage.holidays", accessGroup = "#managers")
@Mapping(module = "academicAdministration", path = "/manageHolidays")
@Forwards(@Forward(name = "showHolidays", path = "/manager/showHolidays.jsp"))
public class AcademicHolidaysManagementDA extends ManageHolidaysDA {

}
