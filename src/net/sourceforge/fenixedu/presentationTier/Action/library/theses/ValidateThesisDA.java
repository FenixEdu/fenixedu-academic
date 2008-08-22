package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.thesis.ThesisSearchBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisLibraryOperation;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.propertyeditors.URLEditor;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ValidateThesisDA extends ThesisLibraryDA {
    protected Thesis getThesis(HttpServletRequest request) {
	Integer id = getIdInternal(request, "thesisID");
	return id != null ? (Thesis) RootDomainObject.getInstance().readThesisByOID(id) : null;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String text = null;
	String state = null;
	String year = null;
	if (request.getParameter("text") != null) {
	    text = (String) URLEncoder.encode(request.getParameter("text"));
	}
	if (request.getParameter("state") != null) {
	    state = (String) request.getParameter("state");
	}
	if (request.getParameter("year") != null) {
	    year = (String) request.getParameter("year");
	}
	request.setAttribute("searchArgs", buildSearchArgs(new ThesisSearchBean(text, state, year)));
	request.setAttribute("thesis", getThesis(request));
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("view", true);
	return mapping.findForward("view");
    }

    public ActionForward prepareValidate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("validate", true);
	return mapping.findForward("view");
    }

    public ActionForward preparePending(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("pending", true);
	return mapping.findForward("view");
    }

    public ActionForward prepareEditPending(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	RenderUtils.invalidateViewState();
	request.setAttribute("editPending", true);
	return mapping.findForward("view");
    }

    public ActionForward history(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	List<ThesisLibraryOperation> operations = new ArrayList<ThesisLibraryOperation>();
	Thesis thesis = getThesis(request);
	if (thesis.hasLastLibraryOperation()) {
	    ThesisLibraryOperation last = thesis.getLastLibraryOperation();
	    do {
		operations.add(last);
	    } while ((last = last.getPrevious()) != null);
	}
	request.setAttribute("history", operations);
	return view(mapping, actionForm, request, response);
    }
}
