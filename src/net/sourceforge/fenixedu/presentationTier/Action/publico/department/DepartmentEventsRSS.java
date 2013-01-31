package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.AnnouncementRSS;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/department/eventsRSS", scope = "session", parameter = "method")
public class DepartmentEventsRSS extends AnnouncementRSS {

	@Override
	protected String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
		UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
		return unitBoard.getUnit().getNameWithAcronym() + ": " + board.getName();
	}

	@Override
	protected String getDirectAnnouncementBaseUrl(HttpServletRequest request, Announcement announcement) {
		String selectedDepartment = request.getParameter("selectedDepartmentUnitID");
		return "/publico/department/events.do?method=viewEvent&selectedDepartmentUnitID=" + selectedDepartment;
	}

}
