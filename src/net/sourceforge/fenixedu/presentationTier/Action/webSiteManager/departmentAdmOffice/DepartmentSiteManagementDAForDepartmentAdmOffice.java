package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/departmentSite", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseManagers", path = "/webSiteManager/commons/chooseManagers.jsp", tileProperties = @Tile(bodyContext = "/departmentAdmOffice/site/siteContext.jsp")) })
public class DepartmentSiteManagementDAForDepartmentAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.DepartmentSiteManagementDA {
}