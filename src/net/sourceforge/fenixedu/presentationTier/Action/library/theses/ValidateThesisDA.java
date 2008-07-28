package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

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

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("view", true);
	return mapping.findForward("view");
    }

    public ActionForward prepareOperation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaValidatorForm operationBean = (DynaValidatorForm) actionForm;
	String operation = operationBean.getString("operation");
	if (operation.equals("validate")) {
	    request.setAttribute("validate", true);
	} else if (operation.equals("pending")) {
	    request.setAttribute("pending", true);
	}
	return mapping.findForward("view");
    }

    public ActionForward prepareValidate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("validate", true);
	return mapping.findForward("view");
    }

    public ActionForward validate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	addActionMessage("success", request, "message.thesis.confirmation.success");
	return prepare(mapping, actionForm, request, response);
    }

    public ActionForward preparePending(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("pending", true);
	return mapping.findForward("view");
    }

    public ActionForward pending(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	addActionMessage("success", request, "message.thesis.confirmation.undo.success");
	return prepare(mapping, actionForm, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	addActionMessage("success", request, "message.thesis.cancel.confirmation");
	return prepare(mapping, actionForm, request, response);
    }
}
