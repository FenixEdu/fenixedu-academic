package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class PublicShowThesesDA extends FenixDispatchAction {

    protected Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
	return (request.getParameter(parameter) != null) ? Boolean.valueOf(request.getParameter(parameter)) : (Boolean) request
		.getAttribute(parameter);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// inEnglish
	Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
	if (inEnglish == null) {
	    inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
	}
	request.setAttribute("inEnglish", inEnglish);

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showTheses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	SortedSet<ExecutionYear> years = new TreeSet<ExecutionYear>();
	Map<String, Collection<Thesis>> theses = new HashMap<String, Collection<Thesis>>();

	ThesisFilterBean bean = getFilterBean(request);

	ExecutionYear year = bean.getYear();
	ThesisState state = bean.getState();

	Collection<Degree> degrees = bean.getDegreeOptions();

	Degree degree = bean.getDegree();
	if (degree != null) {
	    degrees = Collections.singleton(degree);
	}

	collectTheses(request, years, theses, year, state, degrees);

	request.setAttribute("filter", bean);
	request.setAttribute("years", years);
	request.setAttribute("theses", theses);

	return mapping.findForward("showTheses");
    }

    protected ThesisFilterBean getFilterBean(HttpServletRequest request) throws Exception {
	ThesisFilterBean bean = (ThesisFilterBean) getRenderedObject("filter");

	if (bean == null) {
	    bean = new ThesisFilterBean();

	    ThesisState state = getCustomState(request);

	    bean.setState(state == null ? ThesisState.EVALUATED : state);
	    bean.setDegree(getCustomDegree(request));
	    bean.setYear(getCustomExecutionYear(request));
	}

	return bean;
    }

    private ThesisState getCustomState(HttpServletRequest request) {
	String state = request.getParameter("thesisState");

	if (state == null) {
	    return null;
	}

	return ThesisState.valueOf(state);
    }

    private Degree getCustomDegree(HttpServletRequest request) {
	Integer id = getIdInternal(request, "degreeID");

	if (id == null) {
	    return null;
	}

	return RootDomainObject.getInstance().readDegreeByOID(id);
    }

    private ExecutionYear getCustomExecutionYear(HttpServletRequest request) {
	Integer id = getIdInternal(request, "executionYearID");

	if (id == null) {
	    return null;
	}

	return RootDomainObject.getInstance().readExecutionYearByOID(id);
    }

    protected void collectTheses(HttpServletRequest request, SortedSet<ExecutionYear> years,
	    Map<String, Collection<Thesis>> theses, ExecutionYear year, ThesisState state, Collection<Degree> degrees)
	    throws Exception {
	for (Degree degree : degrees) {
	    for (final Thesis thesis : degree.getThesisSet()) {
		if (state != null && thesis.getState() != state) {
		    continue;
		}

		final Enrolment enrolment = thesis.getEnrolment();
		if (enrolment != null) {
		    final ExecutionYear executionYear = enrolment.getExecutionYear();

		    if (year != null && executionYear != year) {
			continue;
		    }

		    if (thesis.isFinalThesis() && !thesis.isFinalAndApprovedThesis()) {
			continue;
		    }

		    prepareMap(thesis, years, theses).add(thesis);
		}
	    }
	}
    }

    protected Collection<Thesis> prepareMap(Thesis thesis, SortedSet<ExecutionYear> years, Map<String, Collection<Thesis>> theses) {
	ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
	years.add(executionYear);

	Collection<Thesis> collection = theses.get(executionYear.getYear());
	if (collection == null) {
	    collection = new TreeSet<Thesis>(new ThesisByNameComparator());
	    theses.put(executionYear.getYear(), collection);
	}

	return collection;
    }

    public ActionForward showThesisDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer thesisId = getIntegerFromRequest(request, "thesisID");

	Thesis thesis = RootDomainObject.getInstance().readThesisByOID(thesisId);
	request.setAttribute("thesis", thesis);

	return mapping.findForward("showThesisDetails");
    }

    public ActionForward showResult(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer thesisId = getIntegerFromRequest(request, "thesisID");

	ResearchResult result = RootDomainObject.getInstance().readResearchResultByOID(thesisId);
	request.setAttribute("result", result);

	return mapping.findForward("showResult");
    }

    private static class ThesisByNameComparator implements Comparator<Thesis> {

	public int compare(Thesis o1, Thesis o2) {
	    String title1 = o1.getFinalFullTitle().getContent().trim();
	    String title2 = o2.getFinalFullTitle().getContent().trim();

	    int c = title1.compareTo(title2);
	    if (c != 0) {
		return c;
	    } else {
		return o1.getIdInternal().compareTo(o2.getIdInternal());
	    }
	}

    }

}
