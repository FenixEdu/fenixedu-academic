package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.PeopleManagementApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PeopleManagementApp.class, descriptionKey = "label.manage.holidays", path = "manage-holodays",
        titleKey = "label.manage.holidays")
@Mapping(module = "manager", path = "/manageHolidays", input = "/manageHolidays.do?method=prepare&page=0", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "showHolidays", path = "/manager/showHolidays.jsp") })
public class ManageHolidaysDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageHolidaysDA {
}