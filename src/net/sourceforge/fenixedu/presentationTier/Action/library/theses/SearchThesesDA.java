package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.thesis.ThesisSearchBean;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Pedro Santos (pmrsa)
 */
public class SearchThesesDA extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisSearchBean search = new ThesisSearchBean();
	performSearch(request, search);
	return mapping.findForward("search");
    }

    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String text = (String) request.getParameter("text");
	String state = (String) request.getParameter("state");
	String year = (String) request.getParameter("year");
	ThesisSearchBean search = new ThesisSearchBean(text, state, year);
	performSearch(request, search);
	return mapping.findForward("search");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ThesisSearchBean search = (ThesisSearchBean) getRenderedObject("search");
	performSearch(request, search);
	return mapping.findForward("search");
    }

    private void performSearch(HttpServletRequest request, ThesisSearchBean search) {
	if (request.getAttribute("sortBy") != null)
	    request.setAttribute("sortBy", request.getAttribute("sortBy"));
	request.setAttribute("searchFilter", search);
	request.setAttribute("searchArgs", buildSearchArgs(search));
	List<Thesis> theses = new ArrayList<Thesis>();
	for (Thesis thesis : Thesis.getEvaluatedThesis()) {
	    if (!thesis.isFinalAndApprovedThesis())
		continue;
	    if (search.isMatch(thesis))
		theses.add(thesis);
	}
	List<Thesis> result = theses;
	request.setAttribute("thesesFound", result.size());
	request.setAttribute("theses", result);
    }

    private String buildSearchArgs(ThesisSearchBean search) {
	StringBuffer buffer = new StringBuffer();
	if (search.getText() != null && !search.getText().isEmpty())
	    appendSearchArg(buffer, "text=" + search.getText());
	if (search.getState() != null)
	    appendSearchArg(buffer, "state=" + search.getState().getName());
	if (search.getYear() != null)
	    appendSearchArg(buffer, "year=" + search.getYear().getName());
	return buffer.toString();
    }

    private void appendSearchArg(StringBuffer buffer, String arg) {
	if (buffer.length() == 0)
	    buffer.append('&');
	else if (buffer.charAt(buffer.length() - 1) != '&') {
	    buffer.append('&');
	}
	buffer.append(arg);
    }
}
