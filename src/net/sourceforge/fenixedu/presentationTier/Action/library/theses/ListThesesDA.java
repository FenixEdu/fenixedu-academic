package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ListThesesDA extends LibraryThesisDA {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("theses", getUnconfirmedTheses());
	return mapping.findForward("list");
    }

}
