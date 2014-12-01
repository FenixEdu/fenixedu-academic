package org.fenixedu.academic.ui.struts.action.accounts;

import org.fenixedu.academic.ui.struts.action.commons.FacesEntryPoint;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsApplication(bundle = "ManagerResources", path = "account-management", titleKey = "link.accountmanagement",
        hint = "Account Management", accessGroup = "#managers")
@Mapping(path = "/accountManagementApp", parameter = "/accounts/welcomeScreen.jsp")
public class AccountManagementApp {

    @StrutsFunctionality(app = AccountManagementApp.class, path = "management-functions", titleKey = "link.functions.management")
    @Mapping(path = "/functionsManagement/personSearchForFunctionsManagement", module = "manager")
    public static class ManagementFunctionsPage extends FacesEntryPoint {
    }
}