package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "operator", path = "/manageRoles", input = "/manageRoles.do?method=prepare&page=0", attribute = "rolesForm",
        formBean = "rolesForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "SelectUserPage", path = "df.page.manage-roles"),
        @Forward(name = "ShowRoleOperationLogs", path = "/manager/showRoleOperationLogs.jsp"),
        @Forward(name = "Manage", path = "df.page.manage-roles") })
public class ManageRolesDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageRolesDA {
}