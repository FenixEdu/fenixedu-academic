package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestContextUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixDateAndTimeContextAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	RequestContextUtil.setExamDateAndTimeContext(request);

	ActionForward actionForward = super.execute(mapping, actionForm, request, response);

	return actionForward;
    }

}
