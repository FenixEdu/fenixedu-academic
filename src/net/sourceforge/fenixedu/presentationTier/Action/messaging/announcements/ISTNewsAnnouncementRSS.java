package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import javax.servlet.http.HttpServletRequest;
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

@Mapping(module = "external", path = "/newsAnnouncementsRSS", scope = "request", validate = false, parameter = "method")
public class ISTNewsAnnouncementRSS extends AnnouncementRSS {

    @Override
    protected String getSiteLocation(HttpServletRequest request) throws Exception {
	return "http://www.ist.utl.pt/information.php?boardId=7557&language=pt&archive=true#achor";
    }

}
