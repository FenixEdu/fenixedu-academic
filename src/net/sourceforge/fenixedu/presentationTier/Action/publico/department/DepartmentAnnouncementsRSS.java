package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.AnnouncementRSS;
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

@Mapping(module = "publico", path = "/department/announcementsRSS", scope = "session", parameter = "method")
public class DepartmentAnnouncementsRSS extends AnnouncementRSS {

    @Override
    protected String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
	UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
	return unitBoard.getUnit().getNameWithAcronym() + ": " + board.getName();
    }

    @Override
    protected String getDirectAnnouncementBaseUrl(HttpServletRequest request, Announcement announcement) {
	String selectedDepartment = request.getParameter("selectedDepartmentUnitID");
	return "/publico/department/announcements.do?method=viewAnnouncement&selectedDepartmentUnitID=" + selectedDepartment;
    }

}
