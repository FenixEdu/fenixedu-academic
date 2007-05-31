package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
        request.setAttribute("showingEvents", true);

        
        return path;
    }

    public ActionForward viewEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return viewAnnouncements(mapping, form, request, response);
    }

}
