package net.sourceforge.fenixedu.presentationTier.Action.research.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantionFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantionSimpleCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateEventDispatchAction extends FenixDispatchAction {

    
    
    public ActionForward prepareCreateEventParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("CreateEvent");  
    }

    public ActionForward prepareCreateSimpleEventParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        EventParticipantionSimpleCreationBean simpleBean = new EventParticipantionSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        return prepareCreateEventParticipation(mapping, form, request, response);
    }

    public ActionForward createSimpleEventParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final IUserView userView = getUserView(request);
        
        if(RenderUtils.getViewState().getMetaObject().getObject() instanceof EventParticipantionSimpleCreationBean){
            EventParticipantionSimpleCreationBean simpleBean = (EventParticipantionSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            if(simpleBean.getEvent() != null) {
                Integer oid = userView.getPerson().getIdInternal();
                ServiceUtils.executeService(userView, "CreateEventParticipant", new Object[] {simpleBean, oid });
                return mapping.findForward("Success");
            }
            else {
                //Permitir a cria��o de um novo evento on-the-fly
                EventParticipantionFullCreationBean fullBean = new EventParticipantionFullCreationBean();
                fullBean.setEventName(new MultiLanguageString(simpleBean.getEventName()));
                fullBean.setRole(simpleBean.getRole());
                request.setAttribute("fullBean", fullBean);
                return prepareCreateEventParticipation(mapping, form, request, response);
            }
        }
        else {
            request.setAttribute("fullBean", (EventParticipantionFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject());
            return prepareCreateEventParticipation(mapping, form, request, response);
        }        
    }

    public ActionForward createFullEventParticipation (ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        EventParticipantionFullCreationBean fullBean = (EventParticipantionFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        Integer oid = userView.getPerson().getIdInternal();
        
        ServiceUtils.executeService(userView, "CreateEventParticipant", new Object[] { fullBean, oid });
        
        return mapping.findForward("Success");   
    }
   
}