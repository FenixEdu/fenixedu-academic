package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import javax.servlet.http.HttpServletRequest;

public class ISTEventsAnnouncementRSS extends AnnouncementRSS {

    @Override
    protected String getSiteLocation(HttpServletRequest request) throws Exception {
	return "http://www.ist.utl.pt/information.php?boardId=7556&language=pt&archive=true#achor";
    }

}
