package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewCurriculumDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person loggedPerson = getLoggedPerson(request);
        
        List<Project> projects = new ArrayList<Project>();
        for(ProjectParticipation participation : loggedPerson.getProjectParticipations()) {
            if(!projects.contains(participation.getProject())) {
                projects.add(participation.getProject());
            }
        }
        request.setAttribute("projects", projects);
        
        List<Event> events = new ArrayList<Event>();
        for(EventParticipation participation : loggedPerson.getEventParticipations()) {
            if (!events.contains(participation.getEvent())) {
                events.add(participation.getEvent());                
            }
        }
        request.setAttribute("events", events);
        
        final List<ResearchInterest> researchInterests = loggedPerson.getResearchInterests();
        request.setAttribute("researchInterests", researchInterests);
        
        final List<ResultPublication> resultPublications = loggedPerson.getResultPublications();
        request.setAttribute("resultPublications", ResultPublication.sort(resultPublications));
        
        request.setAttribute("resultPatents", loggedPerson.getResultPatents());

        return mapping.findForward("Success");
    }
}