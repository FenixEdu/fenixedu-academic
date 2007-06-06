package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CheckThesisDA extends LibraryThesisDA {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("thesis", getThesis(request));
		return super.execute(mapping, actionForm, request, response);
	}
	
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("view");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("edit", true);
		
		return prepare(mapping, actionForm, request, response);
	}
	
	public ActionForward confirm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Thesis thesis = getThesis(request);
		executeService("ChangeThesesLibraryConfirmation", Collections.singletonList(thesis), true);
		
		addActionMessage("success", request, "message.thesis.confirmation.success");
		return forward(mapping, request, "listPending", null);
	}

	public ActionForward unconfirm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Thesis thesis = getThesis(request);
		executeService("ChangeThesesLibraryConfirmation", Collections.singletonList(thesis), false);
		
		addActionMessage("success", request, "message.thesis.confirmation.undo.success");
		request.setAttribute("unconfirmed", true);
		return forward(mapping, request, "listConfirmed", null);
	}
	
}
 