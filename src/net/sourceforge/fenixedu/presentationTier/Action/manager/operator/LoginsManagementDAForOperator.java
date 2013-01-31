package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "operator", path = "/loginsManagement", input = "/index.do", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "prepareManageLoginTimeIntervals", path = "/manager/loginsManagement/manageLoginTimeIntervals.jsp"),
		@Forward(name = "prepareCreateNewLoginTimeInterval", path = "/manager/loginsManagement/createNewLoginPeriod.jsp"),
		@Forward(
				name = "prepareSearchPerson",
				path = "/manager/loginsManagement/searchPersonToManageLogins.jsp",
				tileProperties = @Tile(title = "private.operator.systemmanagement.managementlogins")),
		@Forward(name = "prepareEditLoginTimeInterval", path = "/manager/loginsManagement/editLoginPeriod.jsp"),
		@Forward(name = "prepareEditLoginAlias", path = "/manager/loginsManagement/editLoginAlias.jsp"),
		@Forward(name = "prepareManageLoginAlias", path = "/manager/loginsManagement/manageLoginAlias.jsp"),
		@Forward(name = "prepareCreateNewLoginAlias", path = "/manager/loginsManagement/createNewLoginAlias.jsp") })
public class LoginsManagementDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.LoginsManagementDA {
}