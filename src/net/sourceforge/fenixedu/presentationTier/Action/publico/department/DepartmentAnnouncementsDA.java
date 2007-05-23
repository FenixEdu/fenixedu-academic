package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

public class DepartmentAnnouncementsDA extends DepartmentBoardsDA {

    // TODO: change literal
    public static final String NAME = "Anúncios";
    
    @Override
    protected String getBoardName() {
        return NAME;
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        String path = "/department/announcements.do";
        
        request.setAttribute("announcementActionVariable", path);
        request.setAttribute("showingAnnouncements", true);
        
        return path;
    }

}
