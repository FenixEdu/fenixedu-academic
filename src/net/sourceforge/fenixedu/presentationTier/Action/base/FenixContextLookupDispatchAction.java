package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixContextLookupDispatchAction extends FenixLookupDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ContextUtils.setExecutionPeriodContext(request);

	ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

	return super.execute(mapping, actionForm, request, response);
    }

}
