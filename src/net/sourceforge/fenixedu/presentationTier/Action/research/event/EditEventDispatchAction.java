package net.sourceforge.fenixedu.presentationTier.Action.research.event;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantUnitCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventProjectAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventProjectAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditEventDispatchAction extends FenixDispatchAction {

    //***************************************
    //                   DATA
    //***************************************
    
    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setAttributeSelectedEvent(request);
        
        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("EditEventData");  
    }
    
    //***************************************
    //               PARTICIPANTS
    //***************************************
    
    public ActionForward prepareEditParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("eventId"));

        for( Event event : rootDomainObject.getEvents()) {
            if (event.getIdInternal().equals(oid)) {
                request.setAttribute("selectedEvent", event);
                List<EventParticipation> participations = new ArrayList<EventParticipation>();
                for(EventParticipation participation : event.getEventParticipations()) {
                    if( participation.getParty() instanceof Person) {
                        participations.add(participation);
                    }
                }
                request.setAttribute("participations", participations);
            }
        }  
        return mapping.findForward("EditEventParticipants");  
    }
    
    public ActionForward prepareEditParticipantsWithSimpleBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ActionForward forward = prepareEditParticipants(mapping, form, request, response);
        
        EventParticipantSimpleCreationBean simpleBean = new EventParticipantSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        mantainExternalStatus(request);
        
        return forward;  
    }
    
    public ActionForward prepareEditParticipantsWithFullBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward forward = prepareEditParticipants(mapping, form, request, response);
        
        EventParticipantFullCreationBean fullBean = new EventParticipantFullCreationBean();
        request.setAttribute("fullBean", fullBean);
        
        mantainExternalStatus(request);
        
        return forward;  
    }

    public ActionForward createParticipantInternalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        
        EventParticipantSimpleCreationBean simpleBean = (EventParticipantSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        if(simpleBean.getPerson() != null) {
            // Criar a participação efectivamente quando já existe a pessoa escolhida
            Integer oid = Integer.parseInt(request.getParameter("eventId"));
            ServiceUtils.executeService(userView, "CreateEventParticipant", new Object[] {simpleBean, oid });
            
            mantainExternalStatus(request);
            return prepareEditParticipants(mapping, form, request, response);
        }
        else {
            //The application should never reach this point: the user may be
            //creating an external person not on pourpose
            throw new RuntimeException ();
        }
    }
    
    public ActionForward createParticipantExternalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        final Integer oid = Integer.parseInt(request.getParameter("eventId"));
        
        if (RenderUtils.getViewState().getMetaObject().getObject() instanceof EventParticipantSimpleCreationBean) {
            EventParticipantSimpleCreationBean simpleBean = (EventParticipantSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            
            if (simpleBean.getPerson() != null){
                //Criação de uma participação com uma pessoa externa já existente
                ServiceUtils.executeService(userView, "CreateEventParticipant", new Object[] { simpleBean, oid });
            }
            else {
                //Caso em que foi inserido o nome de uma pessoa externa não existente
                //Passa-se ao modo de criação completa onde é também pedida a organização da pessoa
                EventParticipantFullCreationBean fullBean = new EventParticipantFullCreationBean();
                fullBean.setPersonName(simpleBean.getPersonName());
                fullBean.setRole(simpleBean.getRole());
                request.setAttribute("fullBean", fullBean);
                
                mantainExternalStatus(request);
                
                return prepareEditParticipants(mapping, form, request, response);                
            }
        }
        else if (RenderUtils.getViewState().getMetaObject().getObject() instanceof EventParticipantFullCreationBean) {
            //Criação de uma participação com o nome de uma pessoa não existente ainda no sistema e a sua organização
            EventParticipantFullCreationBean fullBean = (EventParticipantFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            ServiceUtils.executeService(userView, "CreateEventParticipant", new Object[] { fullBean, oid });
        }
        return prepareEditParticipants(mapping, form, request, response);
    }    
    
    public ActionForward removeParticipant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final IUserView userView = getUserView(request);
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        
        ServiceUtils.executeService(userView, "DeleteEventParticipant", new Object[] { participantId });
        
        return prepareEditParticipants(mapping, form, request, response);
    }    
    
    //***************************************
    //                   PROJECTS
    //***************************************

    public ActionForward prepareEditProjectAssociationsSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("eventId"));

        for(Event event : rootDomainObject.getEvents()) {
            if (event.getIdInternal().equals(oid)) {
                request.setAttribute("selectedEvent", event);
                List<ProjectEventAssociation> associations = event.getAssociatedProjects();
                request.setAttribute("projectAssociations", associations);
            }
        }  
        
        return mapping.findForward("EditEventProjectAssociations");  
    }
    
    public ActionForward prepareEditProjectAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward forward = prepareEditProjectAssociationsSimple(mapping, form, request, response);
        
        EventProjectAssociationSimpleCreationBean simpleBean = new EventProjectAssociationSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        return forward;  
    }    
    
    public ActionForward createProjectAssociationSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        
        if(RenderUtils.getViewState().getMetaObject().getObject() instanceof EventProjectAssociationSimpleCreationBean){
            EventProjectAssociationSimpleCreationBean simpleBean = (EventProjectAssociationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            if(simpleBean.getProject() != null) {
                // Criar a associaï¿½ï¿½o efectivamente quando jï¿½ existe o evento escolhido
                Integer oid = Integer.parseInt(request.getParameter("eventId"));
                ServiceUtils.executeService(userView, "CreateEventProjectAssociation", new Object[] {simpleBean, oid });
                return prepareEditProjectAssociations(mapping, form, request, response);
            }
            else {
                //Permitir a criaï¿½ï¿½o de um novo evento on-the-fly
                EventProjectAssociationFullCreationBean fullBean = new EventProjectAssociationFullCreationBean();
                fullBean.setProjectTitle(new MultiLanguageString(simpleBean.getProjectTitle()));
                fullBean.setRole(simpleBean.getRole());
                request.setAttribute("fullBean", fullBean);
                return prepareEditProjectAssociationsSimple(mapping, form, request, response);
            }
        }
        else {
            request.setAttribute("fullBean", (EventProjectAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject());
            return prepareEditProjectAssociationsSimple(mapping, form, request, response);
        }        
    }
    
    public ActionForward createProjectAssociationFull(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        EventProjectAssociationFullCreationBean fullBean = (EventProjectAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        Integer oid = Integer.parseInt(request.getParameter("eventId"));
        
        ServiceUtils.executeService(userView, "CreateProjectEventAssociation", new Object[] { fullBean, oid });
        
        return prepareEditProjectAssociations(mapping, form, request, response);
    }       
    
    public ActionForward removeProjectAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final IUserView userView = getUserView(request);
        Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        
        ServiceUtils.executeService(userView, "DeleteProjectEventAssociation", new Object[] { associationId });
        
        return prepareEditProjectAssociations(mapping, form, request, response);
    }       


	// ***************************************
	//                   UNITS
	// ***************************************
    
	public ActionForward prepareEditParticipantUnits(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final Integer oid = Integer.parseInt(request.getParameter("eventId"));

		for (Event event : rootDomainObject.getEvents()) {
			if (event.getIdInternal().equals(oid)) {
				request.setAttribute("selectedEvent", event);
				List<EventParticipation> unitParticipations = new ArrayList<EventParticipation>();
				for (EventParticipation unitParticipation : event
						.getEventParticipations()) {
					if (unitParticipation.getParty() instanceof Unit) {
						unitParticipations.add(unitParticipation);
					}
				}
				request.setAttribute("unitParticipations", unitParticipations);
			}
		}

		EventParticipantUnitCreationBean bean = new EventParticipantUnitCreationBean();
		request.setAttribute("bean", bean);

		return mapping.findForward("EditEventParticipantUnits");
	}

	public ActionForward createParticipantUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final IUserView userView = getUserView(request);

		EventParticipantUnitCreationBean bean = (EventParticipantUnitCreationBean) RenderUtils
				.getViewState().getMetaObject().getObject();
		
		Integer oid = Integer.parseInt(request.getParameter("eventId"));
		ServiceUtils.executeService(userView, "CreateEventParticipant", new Object[] { bean, oid });
		return prepareEditParticipantUnits(mapping, form, request, response);
	}

	public ActionForward removeParticipantUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final IUserView userView = getUserView(request);
		Integer participantId = Integer.parseInt(request
				.getParameter("participantUnitId"));

		ServiceUtils.executeService(userView, "DeleteEventParticipant",
				new Object[] { participantId });

		return prepareEditParticipants(mapping, form, request, response);
	}

	// ***************************************
	//                  PRIVATE
	// ***************************************

	private void setAttributeSelectedEvent(HttpServletRequest request) {
		final Integer oid = Integer.parseInt(request.getParameter("eventId"));

		for (Event event : rootDomainObject.getEvents()) {
			if (event.getIdInternal().equals(oid)) {
				request.setAttribute("selectedEvent", event);
			}
		}
	}
    
    private void mantainExternalStatus(HttpServletRequest request) {
        final String external = request.getParameter("external");
        if (external != null) {
            request.setAttribute("external", external);
        }
    }
}