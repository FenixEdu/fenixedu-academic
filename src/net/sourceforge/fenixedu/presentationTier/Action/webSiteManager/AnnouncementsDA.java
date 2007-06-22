package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

public class AnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/manageDepartmentSiteAnnouncements.do";
    }

}
