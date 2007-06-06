package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ListExportedThesesDA extends LibraryThesisDA {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("theses", getExportedTheses());
		return mapping.findForward("list");
	}

	public ActionForward unmark(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Thesis> theses = getTheses(request);
		
		if (theses.isEmpty()) {
			addActionMessage("warning", request, "warning.libray.theses.unexport.selectOne");
		}
		else {
			executeService("MarkExportedTheses", theses, false);
			request.setAttribute("unmarkSuccessfull", true);
		}
		
		return prepare(mapping, actionForm, request, response);
	}
}
