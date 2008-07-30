package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ValidateThesisDA extends FenixDispatchAction {
    protected Thesis getThesis(HttpServletRequest request) {
	Integer id = getIdInternal(request, "thesisID");
	return id != null ? (Thesis) RootDomainObject.getInstance().readThesisByOID(id) : null;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
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
}
