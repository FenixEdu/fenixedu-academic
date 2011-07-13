package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
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

@Mapping(module = "webSiteManager", path = "/managePedagogicalCouncilAnnouncements", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewAnnouncement", path = "pedagogicalCouncil-announcements-view-announcement"),
		@Forward(name = "viewAnnouncementsRedirect", path = "/managePedagogicalCouncilAnnouncements.do?method=viewAnnouncements&tabularVersion=true"),
		@Forward(name = "uploadFile", path = "pedagogicalCouncil-announcements-uploadFile"),
		@Forward(name = "edit", path = "pedagogicalCouncil-announcements-edit-announcement"),
		@Forward(name = "listAnnouncements", path = "pedagogicalCouncil-announcements-list-announcements"),
		@Forward(name = "add", path = "pedagogicalCouncil-announcements-add-announcement"),
		@Forward(name = "noBoards", path = "pedagogicalCouncil-announcements-no-boards"),
		@Forward(name = "editFile", path = "pedagogicalCouncil-announcements-editFile"),
		@Forward(name = "listAnnouncementBoards", path = "pedagogicalCouncil-announcements-list-boards") })
public class PedagogicalCouncilAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	return "/managePedagogicalCouncilAnnouncements.do";
    }

}
