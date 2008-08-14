package net.sourceforge.fenixedu.presentationTier.Action.research.project;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewProjectDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final Integer oid = Integer.parseInt(request.getParameter("projectId"));

	for (Project project : rootDomainObject.getProjects()) {
	    if (project.getIdInternal().equals(oid)) {
		request.setAttribute("selectedProject", project);
		List<ProjectParticipation> participations = new ArrayList<ProjectParticipation>();
		for (ProjectParticipation participation : project.getProjectParticipations()) {
		    if (participation.getParty() instanceof Person) {
			participations.add(participation);
		    }
		}
		request.setAttribute("participations", participations);
	    }
	}

	for (Project project : rootDomainObject.getProjects()) {
	    if (project.getIdInternal().equals(oid)) {
		request.setAttribute("selectedProject", project);
		List<ProjectParticipation> unitParticipations = new ArrayList<ProjectParticipation>();
		for (ProjectParticipation participation : project.getProjectParticipations()) {
		    if (participation.getParty() instanceof Unit) {
			unitParticipations.add(participation);
		    }
		}
		request.setAttribute("unitParticipations", unitParticipations);
	    }
	}

	request.setAttribute("party", getUserView(request).getPerson());
	return mapping.findForward("ViewProject");
    }
}