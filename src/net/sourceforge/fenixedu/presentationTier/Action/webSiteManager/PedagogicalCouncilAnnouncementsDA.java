package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

public class PedagogicalCouncilAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/managePedagogicalCouncilAnnouncements.do";
    }

}
