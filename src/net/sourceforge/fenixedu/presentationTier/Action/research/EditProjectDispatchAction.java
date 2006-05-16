package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditProjectDispatchAction extends FenixDispatchAction {

    
    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setAttributeSelectedProject(request);
        
        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("EditProjectData");  
    }
    
    public ActionForward prepareEditParticipantsSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("projectId"));

        for( Project project : rootDomainObject.getProjects()) {
            if (project.getIdInternal().equals(oid)) {
                request.setAttribute("selectedProject", project);
                List<ProjectParticipation> participations = new ArrayList<ProjectParticipation>();
                for(ProjectParticipation participation : project.getProjectParticipations()) {
                    if( participation.getParty() instanceof Person) {
                        participations.add(participation);
                    }
                }
                request.setAttribute("participations", participations);
            }
        }  
        
        return mapping.findForward("EditProjectParticipants");  
    }
    
    public ActionForward prepareEditParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward forward = prepareEditParticipantsSimple(mapping, form, request, response);
        
        ProjectParticipantSimpleCreationBean simpleBean = new ProjectParticipantSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        return forward;  
    }
    
    public ActionForward manageAssociatedEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setAttributeSelectedProject(request);
        
        ProjectParticipantSimpleCreationBean simpleBean = new ProjectParticipantSimpleCreationBean();
        
        request.setAttribute("simpleBean", simpleBean);
        
        return mapping.findForward("ManageAssociatedEvents");  
    }    

    public ActionForward viewEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer eventId = Integer.parseInt(request.getParameter("eventId"));
        final Integer projectId = Integer.parseInt(request.getParameter("projectId"));

        for( Project project : rootDomainObject.getProjects()) {
            if (project.getIdInternal().equals(projectId)) {
                request.setAttribute("selectedProject", project);
                for(ProjectEventAssociation association : project.getAssociatedEvents()) {
                    if(association.getEvent().getIdInternal().equals(eventId)) {
                        request.setAttribute("selectedEvent", association.getEvent());
                    }
                }
            }
        } 
        
        return mapping.findForward("ViewEvent");  
    }    
    
    public ActionForward createParticipantSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        ProjectParticipantSimpleCreationBean simpleBean = (ProjectParticipantSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        if(simpleBean.getPerson() != null) {
            // Criar a participação efectivamente quando já existe a pessoa escolhida
            Integer oid = Integer.parseInt(request.getParameter("projectId"));
            ServiceUtils.executeService(userView, "CreateProjectParticipant", new Object[] {simpleBean, oid });
            return prepareEditParticipants(mapping, form, request, response);
        }
        else {
            //Permitir a criação de uma pessoa externa
            ProjectParticipantFullCreationBean fullBean = new ProjectParticipantFullCreationBean();
            fullBean.setPersonName(simpleBean.getPersonName());
            fullBean.setRole(simpleBean.getRole());
            request.setAttribute("fullBean", fullBean);
            return prepareEditParticipantsSimple(mapping, form, request, response);
        }
    }
    
    public ActionForward createParticipantFull(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProjectParticipantFullCreationBean fullBean = (ProjectParticipantFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        Integer oid = Integer.parseInt(request.getParameter("projectId"));
        
        ServiceUtils.executeService(userView, "CreateProjectParticipant", new Object[] { fullBean, oid });
        
        return prepareEditParticipants(mapping, form, request, response);
    }
    
    public ActionForward prepareAssociateEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setAttributeSelectedProject(request);
        
        return mapping.findForward("AssociateEvent");  
    } 
    
    private void setAttributeSelectedProject(HttpServletRequest request) {
        final Integer oid = Integer.parseInt(request.getParameter("projectId"));

        for( Project project : rootDomainObject.getProjects()) {
            if (project.getIdInternal().equals(oid)) {
                request.setAttribute("selectedProject", project);
            }
        }        
    }
}