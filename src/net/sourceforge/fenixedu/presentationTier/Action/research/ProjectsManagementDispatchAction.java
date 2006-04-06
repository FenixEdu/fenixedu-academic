package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ProjectsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        
        List<Project> projects = new ArrayList<Project>();

        for(ProjectParticipation participation : userView.getPerson().getProjectParticipations()) {
            projects.add(participation.getProject());
        }
        request.setAttribute("projects", projects);
        return mapping.findForward("Success");  
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final Integer oid = Integer.parseInt(request.getParameter("oid"));
        
        ServiceUtils.executeService(userView, "DeleteResearchProject", new Object[] { oid });
        
        return listProjects(mapping, form, request, response);  
    }
    
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("oid"));

        for( Project project : rootDomainObject.getProjects()) {
            if (project.getIdInternal().equals(oid)) {
                final int personIdInternal = getUserView(request).getPerson().getIdInternal();
                for (ProjectParticipation participation: project.getProjectParticipations()) {
                    if (participation.getParty().getIdInternal() == personIdInternal) {
                        request.setAttribute("selectedProjectParticipation", participation);
                    }
                }
            }
        }
        
        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("ProjectSelected");  
    }    
    
}