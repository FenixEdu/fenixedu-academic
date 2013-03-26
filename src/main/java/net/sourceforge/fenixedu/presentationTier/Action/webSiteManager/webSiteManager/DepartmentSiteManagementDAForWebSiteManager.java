package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.webSiteManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageDepartmentSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "confirmDeleteFunction", path = "departmentSite-confirmDeleteFunction"),
        @Forward(name = "changePersonFunctions", path = "departmentSite-changePersonFunctions"),
        @Forward(name = "createPersonFunction", path = "departmentSite-createPersonFunction"),
        @Forward(name = "editSideBanner", path = "departmentSite-edit-side-banner"),
        @Forward(name = "confirmSectionDelete", path = "departmentSite-confirmSectionDelete"),
        @Forward(name = "editFooterNavigation", path = "departmentSite-edit-footer-navigation"),
        @Forward(name = "editSection", path = "departmentSite-editSection"),
        @Forward(name = "chooseManagers", path = "departmentSite-chooseManagers"),
        @Forward(name = "uploadFile", path = "departmentSite-uploadFile"),
        @Forward(name = "editConfiguration", path = "departmentSite-edit-configuration"),
        @Forward(name = "organizeTopLinks", path = "departmentSite-edit-organizeTopLinks"),
        @Forward(name = "editFunctionalitySection", path = "departmentSite-editFunctionalitySection"),
        @Forward(name = "organizeFooterLinks", path = "departmentSite-edit-organizeFooterLinks"),
        @Forward(name = "editItem", path = "departmentSite-editItem"),
        @Forward(name = "editFile", path = "departmentSite-editFile"),
        @Forward(name = "functionalitySection", path = "departmentSite-functionalitySection"),
        @Forward(name = "addInstitutionSection", path = "departmentSite-addInstitutionSection"),
        @Forward(name = "organizeFunctions", path = "departmentSite-organizeFunctions"),
        @Forward(name = "editBanners", path = "departmentSite-edit-banners"),
        @Forward(name = "organizeItems", path = "departmentSite-organizeItems"),
        @Forward(name = "manageExistingFunctions", path = "departmentSite-manageExistingFunctions"),
        @Forward(name = "organizeFiles", path = "departmentSite-organizeFiles"),
        @Forward(name = "edit-fileItem-name", path = "departmentSite-editFileItemName"),
        @Forward(name = "addFunction", path = "departmentSite-addFunction"),
        @Forward(name = "editSectionPermissions", path = "departmentSite-editSectionPermissions"),
        @Forward(name = "analytics", path = "departmentSite-analytics"),
        @Forward(name = "editFunction", path = "departmentSite-editFunction"),
        @Forward(name = "chooseIntroductionSections", path = "departmentSite-chooseIntroductionSections"),
        @Forward(name = "section", path = "departmentSite-section"),
        @Forward(name = "createSection", path = "departmentSite-createSection"),
        @Forward(name = "editItemPermissions", path = "departmentSite-editItemPermissions"),
        @Forward(name = "editIntroduction", path = "departmentSite-edit-introduction"),
        @Forward(name = "editLogo", path = "departmentSite-edit-logo"),
        @Forward(name = "editTopNavigation", path = "departmentSite-edit-top-navigation"),
        @Forward(name = "sectionsManagement", path = "departmentSite-sectionsManagement"),
        @Forward(name = "start", path = "departmentSite-start"),
        @Forward(name = "createItem", path = "departmentSite-createItem"),
        @Forward(name = "manageFunctions", path = "departmentSite-manageFunctions"),
        @Forward(name = "editPersonFunction", path = "departmentSite-editPersonFunction") })
public class DepartmentSiteManagementDAForWebSiteManager extends
        net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.DepartmentSiteManagementDA {
}