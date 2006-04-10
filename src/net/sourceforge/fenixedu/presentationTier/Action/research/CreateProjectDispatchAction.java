package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class CreateProjectDispatchAction extends FenixDispatchAction {

    
    public ActionForward prepareSearchProjectTitle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("SearchProject");  
    }
    
    public ActionForward searchProjectTitle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final String searchedProjectTitle = (String)((DynaActionForm)form).get("searchedProjectTitle");
        //TODO: the searchedProjectTitle has to have more than 3(?) characters
        
        final List<Project> projects = Project.getProjectsByTitle(searchedProjectTitle);
        
        final List<Project> orderedProjects = getOrderedProjects(projects);
        
        request.setAttribute("orderedProjectsList", orderedProjects);

        return mapping.findForward("SelectProject");  
    }

    public ActionForward selectProject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
    
    public ActionForward prepareCreateProject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("CreateProject");  
    }
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
       
        return mapping.findForward("Success");  
    }
    
    private List<Project> getOrderedProjects(List<Project> projects) throws FenixFilterException, FenixServiceException {
        List<Project> orderedProjects = new ArrayList<Project>(projects);
        Collections.sort(orderedProjects, new Comparator<Project>() {
            public int compare(Project project1, Project project2) {
                //REMEMBER: this should be the language that the user is viewing and not
                //the application language
                return project1.getTitle().getContent().compareToIgnoreCase(project2.getTitle().getContent());
            }
        });
        return orderedProjects;
    }

    
}