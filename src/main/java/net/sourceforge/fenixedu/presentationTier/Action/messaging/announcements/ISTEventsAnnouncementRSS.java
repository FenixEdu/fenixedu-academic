package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/eventsAnnouncementsRSS", scope = "request", validate = false, parameter = "method")
public class ISTEventsAnnouncementRSS extends AnnouncementRSS {

    @Override
    protected String getSiteLocation(HttpServletRequest request) throws Exception {
        return "http://www.ist.utl.pt/information.php?boardId=7556&language=pt&archive=true#achor";
    }

}
