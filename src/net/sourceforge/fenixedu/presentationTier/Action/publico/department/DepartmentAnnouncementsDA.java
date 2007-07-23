package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;

public class DepartmentAnnouncementsDA extends DepartmentBoardsDA {

    @Override
    protected String getBoardName(HttpServletRequest request) {
        return UnitSiteBoardsDA.ANNOUNCEMENTS;
    }

}
