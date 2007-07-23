package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageResearchUnitAnnouncementsDA extends UnitSiteAnnouncementManagement {

	public ActionForward editAnnouncementBoards(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<PartyAnnouncementBoard> boards = getUnit(request).getBoards();
		request.setAttribute("announcementBoards", boards);
		return mapping.findForward(boards.isEmpty() ? "noBoards"
				: "listAnnouncementBoards");
	}

	protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
		return "/manageResearchUnitAnnouncements.do";
	}

}
