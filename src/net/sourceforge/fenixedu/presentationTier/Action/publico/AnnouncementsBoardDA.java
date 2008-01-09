package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.util.MultiLanguageString;

public class AnnouncementsBoardDA extends UnitSiteBoardsDA {

	@Override
	protected MultiLanguageString getBoardName(HttpServletRequest request) {
		return UnitSiteBoardsDA.ANNOUNCEMENTS;
	}

}
