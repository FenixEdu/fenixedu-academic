package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.AnnouncementRSS;

public class ResearchUnitEventsRSS extends AnnouncementRSS {

    @Override
    protected String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
	UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
	return unitBoard.getUnit().getNameWithAcronym() + ": " + board.getName();
    }

    @Override
    protected String getDirectAnnouncementBaseUrl(HttpServletRequest request, Announcement announcement) {
	String selectedSite = request.getParameter("siteID");
	return "/publico/researchSite/manageResearchUnitAnnouncements.do?method=viewEvents&siteID=" + selectedSite;
    }

}
