package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowDegreeThesesDA extends FenixDispatchAction {

    protected Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        return (request.getParameter(parameter) != null) ? Boolean.valueOf(request.getParameter(parameter)) : (Boolean) request.getAttribute(parameter);
    }
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("degree", getDegree(request));
		
        // inEnglish
        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);
		
		return super.execute(mapping, actionForm, request, response);
	}
	
	public Degree getDegree(HttpServletRequest request) throws FenixActionException {
        Integer degreeId = getIntegerFromRequest(request, "degreeID");
        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        
        if (degree == null) {
            throw new FenixActionException();
        }
        
        request.setAttribute("degreeID", degreeId);
        request.setAttribute("degree", degree);

        return degree;
	}

	public ActionForward showTheses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Degree degree = getDegree(request);

		SortedSet<ExecutionYear> years = new TreeSet<ExecutionYear>();
		Map<String, Collection<Thesis>> theses =
			new HashMap<String, Collection<Thesis>>();
		
        for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
            for (CurricularCourse curricularCourse : dcp.getDissertationCurricularCourses(null)) {
                List<IEnrolment> enrolments = new ArrayList<IEnrolment>();

                for (CurriculumModule module : curricularCourse.getCurriculumModules()) {
                    if (module.isEnrolment()) {
                        enrolments.add((IEnrolment) module);
                    } 
                    else if (module.isDismissal()) {
                        Dismissal dismissal = (Dismissal) module;

                        enrolments.addAll(dismissal.getSourceIEnrolments());
                    }
                }

                for (IEnrolment enrolment : enrolments) {
                    net.sourceforge.fenixedu.domain.thesis.Thesis thesis = enrolment.getThesis();

                    if (thesis == null) {
                        continue;
                    }

                    // the thesis may not be final but usually only final and
                    // approved thesis contain a publication
                    Thesis publication = thesis.getPublication();

                    if (publication == null) {
                        continue;
                    }

                    // assert(thesis.isFinalAndApprovedThesis())

                    prepareMap(thesis, years, theses).add(publication);
                }
            }
        }
        
        if (degree.hasPendingThesis()) {
            request.setAttribute("hasPendingThesis", true);
        }
        
		request.setAttribute("years", years);
		request.setAttribute("theses", theses);
		
		return mapping.findForward("showTheses");
	}

	private Collection<Thesis> prepareMap(net.sourceforge.fenixedu.domain.thesis.Thesis thesis, SortedSet<ExecutionYear> years, Map<String, Collection<Thesis>> theses) {
		ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
		years.add(executionYear);
		
		Collection<Thesis> collection = theses.get(executionYear.getYear());
		if (collection == null) {
			collection = new TreeSet<Thesis>(new ResultByNameComparator());
			theses.put(executionYear.getYear(), collection);
		}
		
		return collection;
	}
	
	private static class ResultByNameComparator implements Comparator<Thesis> {

		public int compare(Thesis o1, Thesis o2) {
			String title1 = o1.getTitle();
			String title2 = o2.getTitle();
			
			int c = title1.compareTo(title2);
			if (c != 0) {
				return c;
			}
			else {
				return o1.getIdInternal().compareTo(o2.getIdInternal());
			}
		}
		
	}

	public ActionForward showResult(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer thesisId = getIntegerFromRequest(request, "thesisID");
		
		ResearchResult result = RootDomainObject.getInstance().readResearchResultByOID(thesisId);
		request.setAttribute("result", result);
		
		return mapping.findForward("showResult");
	}
    
    public ActionForward showThesesState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Degree degree = getDegree(request);
        
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear();
        
        SortedSet<net.sourceforge.fenixedu.domain.thesis.Thesis> theses = new TreeSet<net.sourceforge.fenixedu.domain.thesis.Thesis>(
                net.sourceforge.fenixedu.domain.thesis.Thesis.COMPARATOR_BY_STUDENT);
        
        for (net.sourceforge.fenixedu.domain.thesis.Thesis thesis : degree.getThesis()) {
            if (thesis.isEvaluated()) {
                continue;
            }
         
            if (thesis.getEnrolment().getExecutionYear().compareTo(executionYear) < 0) {
                continue;
            }
            
            theses.add(thesis);
        }
        
        request.setAttribute("theses", theses);
        
        return mapping.findForward("showThesesState");
    }
    
}
