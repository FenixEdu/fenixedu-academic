package net.sourceforge.fenixedu.presentationTier.Action.research.event;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewEventDispatchAction extends FenixDispatchAction {

    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final Integer oid = Integer.parseInt(request.getParameter("eventId"));

        for( Event event : rootDomainObject.getEvents()) {
            if (event.getIdInternal().equals(oid)) {
                request.setAttribute("selectedEvent", event);
                List<EventParticipation> participations = new ArrayList<EventParticipation>();
                for (EventParticipation participation : event.getEventParticipations()) {
                    if (participation.getParty() instanceof Person) {
                        participations.add(participation);                        
                    }
                }
                request.setAttribute("participations", participations);
            }
        }
        
        for( Event event : rootDomainObject.getEvents()) {
            if (event.getIdInternal().equals(oid)) {
                request.setAttribute("selectedEvent", event);
                List<EventParticipation> unitParticipations = new ArrayList<EventParticipation>();
                for (EventParticipation participation : event.getEventParticipations()) {
                    if (participation.getParty() instanceof Unit) {
                        unitParticipations.add(participation);                        
                    }
                }
                request.setAttribute("unitParticipations", unitParticipations);
            }
        }
        
        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("ViewEvent");  
    }
}