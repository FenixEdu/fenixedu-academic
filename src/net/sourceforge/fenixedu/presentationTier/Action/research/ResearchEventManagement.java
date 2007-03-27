package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResearchEventManagement extends FenixDispatchAction {

    public ActionForward showEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
	
	String eventId  = request.getParameter("eventId");
	if(eventId!=null) {
	    Event event = rootDomainObject.readEventByOID(Integer.valueOf(eventId));
	    request.setAttribute("event",event);
	}
	
	return mapping.findForward("showEvent");
    }
    
}
