package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/managePedagogicalCouncilSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "confirmDeleteFunction", path = "pedagogicalCouncil-confirmDeleteFunction"),
        @Forward(name = "changePersonFunctions", path = "pedagogicalCouncil-changePersonFunctions"),
        @Forward(name = "createPersonFunction", path = "pedagogicalCouncil-createPersonFunction"),
        @Forward(name = "editSideBanner", path = "pedagogicalCouncil-edit-side-banner"),
        @Forward(name = "confirmSectionDelete", path = "pedagogicalCouncil-confirmSectionDelete"),
        @Forward(name = "editFooterNavigation", path = "pedagogicalCouncil-edit-footer-navigation"),
        @Forward(name = "editSection", path = "pedagogicalCouncil-editSection"),
        @Forward(name = "chooseManagers", path = "pedagogicalCouncil-chooseManagers"),
        @Forward(name = "uploadFile", path = "pedagogicalCouncil-uploadFile"),
        @Forward(name = "editConfiguration", path = "pedagogicalCouncil-edit-configuration"),
        @Forward(name = "organizeTopLinks", path = "pedagogicalCouncil-edit-organizeTopLinks"),
        @Forward(name = "editFunctionalitySection", path = "pedagogicalCouncil-editFunctionalitySection"),
        @Forward(name = "organizeFooterLinks", path = "pedagogicalCouncil-edit-organizeFooterLinks"),
        @Forward(name = "editItem", path = "pedagogicalCouncil-editItem"),
        @Forward(name = "editFile", path = "pedagogicalCouncil-editFile"),
        @Forward(name = "functionalitySection", path = "pedagogicalCouncil-functionalitySection"),
        @Forward(name = "addInstitutionSection", path = "pedagogicalCouncil-addInstitutionSection"),
        @Forward(name = "organizeFunctions", path = "pedagogicalCouncil-organizeFunctions"),
        @Forward(name = "editBanners", path = "pedagogicalCouncil-edit-banners"),
        @Forward(name = "organizeItems", path = "pedagogicalCouncil-organizeItems"),
        @Forward(name = "manageExistingFunctions", path = "pedagogicalCouncil-manageExistingFunctions"),
        @Forward(name = "organizeFiles", path = "pedagogicalCouncil-organizeFiles"),
        @Forward(name = "edit-fileItem-name", path = "pedagogicalCouncil-editFileItemName"),
        @Forward(name = "addFunction", path = "pedagogicalCouncil-addFunction"),
        @Forward(name = "editSectionPermissions", path = "pedagogicalCouncil-editSectionPermissions"),
        @Forward(name = "analytics", path = "pedagogicalCouncil-analytics"),
        @Forward(name = "editFunction", path = "pedagogicalCouncil-editFunction"),
        @Forward(name = "chooseIntroductionSections", path = "pedagogicalCouncil-chooseIntroductionSections"),
        @Forward(name = "section", path = "pedagogicalCouncil-section"),
        @Forward(name = "createSection", path = "pedagogicalCouncil-createSection"),
        @Forward(name = "editItemPermissions", path = "pedagogicalCouncil-editItemPermissions"),
        @Forward(name = "editIntroduction", path = "pedagogicalCouncil-edit-introduction"),
        @Forward(name = "editLogo", path = "pedagogicalCouncil-edit-logo"),
        @Forward(name = "editTopNavigation", path = "pedagogicalCouncil-edit-top-navigation"),
        @Forward(name = "sectionsManagement", path = "pedagogicalCouncil-sectionsManagement"),
        @Forward(name = "start", path = "pedagogicalCouncil-start"),
        @Forward(name = "createItem", path = "pedagogicalCouncil-createItem"),
        @Forward(name = "manageFunctions", path = "pedagogicalCouncil-manageFunctions"),
        @Forward(name = "editPersonFunction", path = "pedagogicalCouncil-editPersonFunction") })
public class PedagogicalCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getUserView(request).getPerson().getName();
    }

}
