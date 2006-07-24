package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
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
            if(!projects.contains(participation.getProject())) {
                projects.add(participation.getProject());
            }
        }
        request.setAttribute("projects", projects);
        
        List<Event> events = new ArrayList<Event>();
        
        for(EventParticipation participation : userView.getPerson().getEventParticipations()) {
            if (!events.contains(participation.getEvent())) {
                events.add(participation.getEvent());                
            }
        }
        request.setAttribute("events", events);
        
        List<ResearchInterest> researchInterests = getUserView(request).getPerson()
        .getResearchInterests();

        request.setAttribute("researchInterests", researchInterests);
        
        List<ResultPublication> resultPublications = getUserView(request).getPerson().getResultPublications();
        //comparator by year in descendent order
        Comparator YearComparator = new Comparator()
        {
            public int compare(Object o1, Object o2) {
                Integer publication1Year = ((ResultPublication) o1).getYear();
                Integer publication2Year = ((ResultPublication) o2).getYear();
                if (publication1Year == null) {
                    return 1;
                } else if (publication2Year == null) {
                    return -1;
                }
                return (-1) * publication1Year.compareTo(publication2Year);
            }
        };
        //order publications
        Collections.sort(resultPublications, YearComparator);
        request.setAttribute("resultPublications", resultPublications);
        
        List<ResultPatent> resultPatents = new ArrayList<ResultPatent>();

        for(ResultParticipation participation : userView.getPerson().getPersonParticipationsWithPatents()) {
            resultPatents.add((ResultPatent)participation.getResult());
        }
        request.setAttribute("resultPatents", resultPatents);
        
        return mapping.findForward("Success");
    }

}