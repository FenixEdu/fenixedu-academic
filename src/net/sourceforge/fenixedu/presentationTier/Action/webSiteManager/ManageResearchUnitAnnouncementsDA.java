package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageResearchUnitAnnouncements", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "viewAnnouncement", path = "research-announcements-view-announcement"),
		@Forward(name = "uploadFile", path = "research-announcements-uploadFile"),
		@Forward(name = "edit", path = "research-announcements-edit-announcement"),
		@Forward(name = "listAnnouncements", path = "research-announcements-list-announcements"),
		@Forward(name = "add", path = "research-announcements-add-announcement"),
		@Forward(name = "noBoards", path = "research-site-no-boards"),
		@Forward(name = "editFile", path = "research-announcements-editFile"),
		@Forward(name = "listAnnouncementBoards", path = "research-announcements-list-boards") })
public class ManageResearchUnitAnnouncementsDA extends UnitSiteAnnouncementManagement {

	public ActionForward editAnnouncementBoards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<PartyAnnouncementBoard> boards = getUnit(request).getBoards();
		request.setAttribute("announcementBoards", boards);
		return mapping.findForward(boards.isEmpty() ? "noBoards" : "listAnnouncementBoards");
	}

	@Override
	protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
		return "/manageResearchUnitAnnouncements.do";
	}

}
