package net.sourceforge.fenixedu.presentationTier.Action.research.project;

import net.sourceforge.fenixedu.applicationTier.Servico.research.project.DeleteResearchProject;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ProjectsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final IUserView userView = getUserView(request);

	List<Project> projects = new ArrayList<Project>();

	for (ProjectParticipation participation : userView.getPerson().getProjectParticipations()) {
	    if (!projects.contains(participation.getProject())) {
		projects.add(participation.getProject());
	    }
	}
	request.setAttribute("projects", projects);
	return mapping.findForward("Success");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final IUserView userView = getUserView(request);
	final Integer oid = Integer.parseInt(request.getParameter("projectId"));

	DeleteResearchProject.run(oid);

	return listProjects(mapping, form, request, response);
    }

}