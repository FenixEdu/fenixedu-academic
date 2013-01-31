package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "operator",
		path = "/viewPersonsWithRole",
		input = "/viewPersonsWithRole.do?method=prepare",
		scope = "request",
		validate = false,
		parameter = "method")
@Forwards(value = {
		@Forward(name = "ShowRoleOperationLogs", path = "/manager/showRoleOperationLogs.jsp", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.peoplemanagement.managementroles")),
		@Forward(name = "ShowPersons", path = "/manager/viewPersonsWithRole_bd.jsp", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.peoplemanagement.managementroles")),
		@Forward(name = "SelectRole", path = "/manager/viewPersonsWithRole_bd.jsp", tileProperties = @Tile(
				title = "private.operator.personnelmanagement.peoplemanagement.managementroles")) })
public class ViewPersonsWithRoleDAForOperator extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.ViewPersonsWithRoleDA {
}