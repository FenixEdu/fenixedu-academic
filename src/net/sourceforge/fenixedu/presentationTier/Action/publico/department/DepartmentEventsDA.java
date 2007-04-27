package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

public class DepartmentEventsDA extends DepartmentBoardsDA {

    // TODO: change literal
    public static final String NAME = "Eventos";
    
    @Override
    protected String getBoardName() {
        return NAME;
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        String path = "/department/events.do";
        
        request.setAttribute("announcementActionVariable", path);
        return path;
    }

}
