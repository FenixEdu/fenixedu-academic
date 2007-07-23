package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

public class AnnouncementsBoardDA extends UnitSiteBoardsDA {

	@Override
	protected String getBoardName(HttpServletRequest request) {
		return UnitSiteBoardsDA.ANNOUNCEMENTS;
	}

}
