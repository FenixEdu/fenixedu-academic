package net.sourceforge.fenixedu.presentationTier.Action.publico.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.AnnouncementRSS;

public class PedagogicalCouncilAnnouncementsRSS extends AnnouncementRSS {

	@Override
	protected String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
		UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
		return unitBoard.getUnit().getNameWithAcronym() + ": " + board.getName();
	}

	@Override
	protected String getDirectAnnouncementBaseUrl(HttpServletRequest request, Announcement announcement) {
		String selectedDepartment = request.getParameter("unitID");
		return "/publico/pedagogicalCouncil/announcements.do?method=viewAnnouncement&unitID=" + selectedDepartment;
	}
	
}
