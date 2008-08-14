package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.SwitchAction;

public class FenixSwitchAction extends SwitchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return super.execute(mapping, actionForm, request, response);
    }

}
