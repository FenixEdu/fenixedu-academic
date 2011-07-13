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

@Mapping(module = "webSiteManager", path = "/manageDepartmentSiteAnnouncements", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewAnnouncement", path = "department-announcements-view-announcement"),
		@Forward(name = "viewAnnouncementsRedirect", path = "/manageDepartmentSiteAnnouncements.do?method=viewAnnouncements&tabularVersion=true"),
		@Forward(name = "uploadFile", path = "department-announcements-uploadFile"),
		@Forward(name = "edit", path = "department-announcements-edit-announcement"),
		@Forward(name = "listAnnouncements", path = "department-announcements-list-announcements"),
		@Forward(name = "add", path = "department-announcements-add-announcement"),
		@Forward(name = "noBoards", path = "department-announcements-no-boards"),
		@Forward(name = "editFile", path = "department-announcements-editFile"),
		@Forward(name = "listAnnouncementBoards", path = "department-announcements-list-boards") })
public class AnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	return "/manageDepartmentSiteAnnouncements.do";
    }

}
