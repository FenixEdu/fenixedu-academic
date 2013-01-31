package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/loginsManagement", input = "/index.do", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "prepareManageLoginTimeIntervals", path = "/manager/loginsManagement/manageLoginTimeIntervals.jsp"),
		@Forward(name = "prepareCreateNewLoginTimeInterval", path = "/manager/loginsManagement/createNewLoginPeriod.jsp"),
		@Forward(name = "prepareSearchPerson", path = "/manager/loginsManagement/searchPersonToManageLogins.jsp"),
		@Forward(name = "prepareEditLoginTimeInterval", path = "/manager/loginsManagement/editLoginPeriod.jsp"),
		@Forward(name = "prepareEditLoginAlias", path = "/manager/loginsManagement/editLoginAlias.jsp"),
		@Forward(name = "prepareManageLoginAlias", path = "/manager/loginsManagement/manageLoginAlias.jsp"),
		@Forward(name = "prepareCreateNewLoginAlias", path = "/manager/loginsManagement/createNewLoginAlias.jsp") })
public class LoginsManagementDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.LoginsManagementDA {
}