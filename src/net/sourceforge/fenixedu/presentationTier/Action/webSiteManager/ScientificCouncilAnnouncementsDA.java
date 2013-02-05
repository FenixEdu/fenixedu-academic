package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageScientificCouncilAnnouncements", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "viewAnnouncement", path = "scientificCouncil-announcements-view-announcement"),
        @Forward(name = "viewAnnouncementsRedirect",
                path = "/manageScientificCouncilAnnouncements.do?method=viewAnnouncements&tabularVersion=true"),
        @Forward(name = "uploadFile", path = "scientificCouncil-announcements-uploadFile"),
        @Forward(name = "edit", path = "scientificCouncil-announcements-edit-announcement"),
        @Forward(name = "listAnnouncements", path = "scientificCouncil-announcements-list-announcements"),
        @Forward(name = "add", path = "scientificCouncil-announcements-add-announcement"),
        @Forward(name = "noBoards", path = "scientificCouncil-announcements-no-boards"),
        @Forward(name = "editFile", path = "scientificCouncil-announcements-editFile"),
        @Forward(name = "listAnnouncementBoards", path = "scientificCouncil-announcements-list-boards") })
public class ScientificCouncilAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/manageScientificCouncilAnnouncements.do";
    }

}
