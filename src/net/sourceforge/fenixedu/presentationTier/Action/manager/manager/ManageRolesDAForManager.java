package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/manageRoles", input = "/manageRoles.do?method=prepare&page=0", attribute = "rolesForm",
        formBean = "rolesForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "SelectUserPage", path = "/manager/manageRoles_bd.jsp"),
        @Forward(name = "ShowRoleOperationLogs", path = "/manager/showRoleOperationLogs.jsp"),
        @Forward(name = "Manage", path = "/manager/manageRoles_bd.jsp") })
public class ManageRolesDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageRolesDA {
}