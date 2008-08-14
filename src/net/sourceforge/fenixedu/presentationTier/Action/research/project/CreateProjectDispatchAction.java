package net.sourceforge.fenixedu.presentationTier.Action.research.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateProjectDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateProject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("party", getUserView(request).getPerson());
	return mapping.findForward("CreateProject");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	return mapping.findForward("Success");
    }

}