package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageScientificCouncilSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "confirmDeleteFunction", path = "scientificCouncil-confirmDeleteFunction"),
        @Forward(name = "changePersonFunctions", path = "scientificCouncil-changePersonFunctions"),
        @Forward(name = "createPersonFunction", path = "scientificCouncil-createPersonFunction"),
        @Forward(name = "editSideBanner", path = "scientificCouncil-edit-side-banner"),
        @Forward(name = "confirmSectionDelete", path = "scientificCouncil-confirmSectionDelete"),
        @Forward(name = "editFooterNavigation", path = "scientificCouncil-edit-footer-navigation"),
        @Forward(name = "editSection", path = "scientificCouncil-editSection"),
        @Forward(name = "chooseManagers", path = "scientificCouncil-chooseManagers"),
        @Forward(name = "uploadFile", path = "scientificCouncil-uploadFile"),
        @Forward(name = "editConfiguration", path = "scientificCouncil-edit-configuration"),
        @Forward(name = "organizeTopLinks", path = "scientificCouncil-edit-organizeTopLinks"),
        @Forward(name = "editFunctionalitySection", path = "scientificCouncil-editFunctionalitySection"),
        @Forward(name = "organizeFooterLinks", path = "scientificCouncil-edit-organizeFooterLinks"),
        @Forward(name = "editItem", path = "scientificCouncil-editItem"),
        @Forward(name = "editFile", path = "scientificCouncil-editFile"),
        @Forward(name = "functionalitySection", path = "scientificCouncil-functionalitySection"),
        @Forward(name = "addInstitutionSection", path = "scientificCouncil-addInstitutionSection"),
        @Forward(name = "organizeFunctions", path = "scientificCouncil-organizeFunctions"),
        @Forward(name = "editBanners", path = "scientificCouncil-edit-banners"),
        @Forward(name = "organizeItems", path = "scientificCouncil-organizeItems"),
        @Forward(name = "manageExistingFunctions", path = "scientificCouncil-manageExistingFunctions"),
        @Forward(name = "organizeFiles", path = "scientificCouncil-organizeFiles"),
        @Forward(name = "edit-fileItem-name", path = "scientificCouncil-editFileItemName"),
        @Forward(name = "addFunction", path = "scientificCouncil-addFunction"),
        @Forward(name = "editSectionPermissions", path = "scientificCouncil-editSectionPermissions"),
        @Forward(name = "analytics", path = "scientificCouncil-analytics"),
        @Forward(name = "editFunction", path = "scientificCouncil-editFunction"),
        @Forward(name = "chooseIntroductionSections", path = "scientificCouncil-chooseIntroductionSections"),
        @Forward(name = "section", path = "scientificCouncil-section"),
        @Forward(name = "createSection", path = "scientificCouncil-createSection"),
        @Forward(name = "editItemPermissions", path = "scientificCouncil-editItemPermissions"),
        @Forward(name = "editIntroduction", path = "scientificCouncil-edit-introduction"),
        @Forward(name = "editLogo", path = "scientificCouncil-edit-logo"),
        @Forward(name = "editTopNavigation", path = "scientificCouncil-edit-top-navigation"),
        @Forward(name = "sectionsManagement", path = "scientificCouncil-sectionsManagement"),
        @Forward(name = "start", path = "scientificCouncil-start"),
        @Forward(name = "createItem", path = "scientificCouncil-createItem"),
        @Forward(name = "manageFunctions", path = "scientificCouncil-manageFunctions"),
        @Forward(name = "editPersonFunction", path = "scientificCouncil-editPersonFunction") })
public class ScientificCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getUserView(request).getPerson().getName();
    }

}
