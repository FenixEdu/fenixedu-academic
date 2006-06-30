package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewCurriculumDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        
        List<Project> projects = new ArrayList<Project>();

        for(ProjectParticipation participation : userView.getPerson().getProjectParticipations()) {
            projects.add(participation.getProject());
        }
        request.setAttribute("projects", projects);
        
        List<ResearchInterest> researchInterests = getUserView(request).getPerson()
        .getResearchInterests();

        request.setAttribute("researchInterests", researchInterests);
        
        List<ResultPublication> resultPublications = getUserView(request).getPerson().getResultPublications();
        request.setAttribute("resultPublications", resultPublications);
        
        List<ResultPatent> resultPatents = new ArrayList<ResultPatent>();

        for(ResultParticipation participation : userView.getPerson().getPersonParticipationsWithPatents()) {
            resultPatents.add((ResultPatent)participation.getResult());
        }
        request.setAttribute("resultPatents", resultPatents);
        
        return mapping.findForward("Success");
    }

}