package net.sourceforge.fenixedu.presentationTier.Action.research.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EventsManagementAction extends FenixDispatchAction {

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
                
        Person person = userView.getPerson();
        request.setAttribute("eventParticipations", person.getEventParticipations());
        
        return mapping.findForward("Success");
    }

    public ActionForward newEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	final IUserView userView = getUserView(request);
    	request.setAttribute("person", userView.getPerson());
    	return mapping.findForward("new");	
    }
    
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	
    	IUserView userView = getUserView(request);
    	Integer oid = Integer.parseInt(request.getParameter("oid"));
    	EventParticipation participation = (EventParticipation) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { EventParticipation.class, oid });
    	
    	request.setAttribute("person", userView.getPerson());
    	request.setAttribute("eventParticipation", participation);
        return mapping.findForward("edit");
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       	IUserView userView = getUserView(request);
    	Integer oid = Integer.parseInt(request.getParameter("oid"));
    	ServiceUtils.executeService(userView, "DeleteResearchEvent", new Object[] { oid });
    	
    	return showEvents(mapping,form,request,response);
    }
    
}