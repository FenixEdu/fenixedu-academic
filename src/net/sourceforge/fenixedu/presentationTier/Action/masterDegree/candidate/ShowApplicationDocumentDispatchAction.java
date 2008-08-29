package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowApplicationDocumentDispatchAction extends FenixDispatchAction {

    public static final String REQUEST_DOCUMENT_TYPE = "documentType";

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	request.setAttribute("candidateID", request.getParameter("candidateID"));
	return mapping.findForward("showApplicationDocumentsList");
    }
}