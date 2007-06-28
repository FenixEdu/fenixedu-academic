package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificCouncil;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;

public class ScientificCouncilAnnouncementsDA extends UnitSiteBoardsDA {

    @Override
    protected String getBoardName(HttpServletRequest request) {
        return UnitSiteBoardsDA.ANNOUNCEMENTS;
    }

	@Override
	protected String getActionPath(HttpServletRequest request) {
		return "/scientificCouncil/announcements.do";
	}

}
