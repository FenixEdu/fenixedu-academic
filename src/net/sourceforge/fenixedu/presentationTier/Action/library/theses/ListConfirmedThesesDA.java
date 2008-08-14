package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ListConfirmedThesesDA extends LibraryThesisDA {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("theses", getOnlyConfirmedTheses());
	return mapping.findForward("list");
    }

    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	List<Thesis> theses = getTheses(request);

	if (request.getParameter("export") != null) {
	    if (theses.isEmpty()) {
		addActionMessage("warning", request, "warning.libray.theses.export.selectOne");
	    } else {
		return forward(mapping, request, "export", "thesesIDs");
	    }
	}

	if (request.getParameter("unconfirm") != null) {
	    executeService("ChangeThesesLibraryConfirmation", theses, false);
	    request.setAttribute("unconfirmed", true);
	}

	return prepare(mapping, actionForm, request, response);
    }
}
