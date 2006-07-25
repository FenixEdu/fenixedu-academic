package net.sourceforge.fenixedu.presentationTier.Action.research.event;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EventsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listEvents (ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        
        List<Event> events = new ArrayList<Event>();

        for(EventParticipation participation : userView.getPerson().getEventParticipations()) {
            if (!events.contains(participation.getEvent())) {
                events.add(participation.getEvent());
            }
        }
        request.setAttribute("events", events);
        return mapping.findForward("Success");  
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final Integer oid = Integer.parseInt(request.getParameter("eventId"));
        
        ServiceUtils.executeService(userView, "DeleteResearchEvent", new Object[] { oid });
        
        return listEvents(mapping, form, request, response);  
    }
    
}