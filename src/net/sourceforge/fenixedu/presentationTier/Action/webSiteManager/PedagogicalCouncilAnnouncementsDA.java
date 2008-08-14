package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class PedagogicalCouncilAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	return "/managePedagogicalCouncilAnnouncements.do";
    }

}
