package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.PeopleManagementApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PeopleManagementApp.class, descriptionKey = "title.manage.roles", path = "role-management",
        titleKey = "title.manage.roles")
@Mapping(module = "manager", path = "/viewPersonsWithRole", input = "/viewPersonsWithRole.do?method=prepare", scope = "request",
        validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "ShowRoleOperationLogs", path = "/manager/showRoleOperationLogs.jsp"),
        @Forward(name = "ShowPersons", path = "/manager/viewPersonsWithRole_bd.jsp"),
        @Forward(name = "SelectRole", path = "/manager/viewPersonsWithRole_bd.jsp") })
public class ViewPersonsWithRoleDAForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.ViewPersonsWithRoleDA {
}