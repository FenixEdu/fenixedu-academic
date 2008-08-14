package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ValidateEmailDA extends FenixDispatchAction {

    public static class ValidateEmailForm {
	private String validationString;

	public String getValidationString() {
	    return validationString;
	}

	public void setValidationString(String validationString) {
	    this.validationString = validationString;
	}
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("showForm");
    }

    public ActionForward redirect(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final ActionForward actionForward = new ActionForward();
	actionForward.setRedirect(true);

	final ValidateEmailForm validateEmailForm = (ValidateEmailForm) getRenderedObject();
	if (validateEmailForm == null) {
	    return prepare(mapping, actionForm, request, response);
	}

	final StringBuilder path = new StringBuilder();
	path.append("https://ciist.ist.utl.pt/servicos/self_service/verify.php?hash=");
	path.append(validateEmailForm.getValidationString());
	actionForward.setPath(path.toString());

	return actionForward;
    }

}
