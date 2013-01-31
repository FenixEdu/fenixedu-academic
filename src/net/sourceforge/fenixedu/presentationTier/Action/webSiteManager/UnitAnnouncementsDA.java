package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageUnitAnnouncements", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewAnnouncement", path = "basicUnit-announcements-view-announcement"),
		@Forward(
				name = "viewAnnouncementsRedirect",
				path = "/manageUnitAnnouncements.do?method=viewAnnouncements&tabularVersion=true"),
		@Forward(name = "uploadFile", path = "basicUnit-announcements-uploadFile"),
		@Forward(name = "edit", path = "basicUnit-announcements-edit-announcement"),
		@Forward(name = "listAnnouncements", path = "basicUnit-announcements-list-announcements"),
		@Forward(name = "add", path = "basicUnit-announcements-add-announcement"),
		@Forward(name = "noBoards", path = "basicUnit-announcements-no-boards"),
		@Forward(name = "editFile", path = "basicUnit-announcements-editFile"),
		@Forward(name = "listAnnouncementBoards", path = "basicUnit-announcements-list-boards") })
public class UnitAnnouncementsDA extends UnitSiteAnnouncementManagement {

	@Override
	protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
		return mapping.getPath() + ".do";
	}

	@Override
	protected String getExtraRequestParameters(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();

		addExtraParameter(request, builder, "siteId");
		addExtraParameter(request, builder, "tabularVersion");
		addExtraParameter(request, builder, "oid");

		return builder.toString();
	}

}
