package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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
        //final IUserView userView = getUserView(request);
        
        request.getAttribute("delete");
        //tratar de chamar o metodo delete do projecto
        
        
        return mapping.findForward("Success");  
    }

}