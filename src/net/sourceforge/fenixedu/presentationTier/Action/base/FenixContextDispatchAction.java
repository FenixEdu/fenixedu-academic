package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixContextDispatchAction extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ContextUtils.setExecutionPeriodContext(request);

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }
    
    protected Integer getFromRequest(String parameter, HttpServletRequest request) {
        if (request.getParameter(parameter) != null) {
            return Integer.valueOf(request.getParameter(parameter));
        } else if (request.getAttribute(parameter) != null) {
            if (request.getAttribute(parameter) instanceof String) {
                return Integer.valueOf((String) request.getAttribute(parameter));
            } else if (request.getAttribute(parameter) instanceof Integer) {
                return (Integer) request.getAttribute(parameter);
            }
        }
        return null;
    }

    protected Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        return (request.getParameter(parameter) != null) ? Boolean.valueOf(request.getParameter(parameter)) : (Boolean) request.getAttribute(parameter);
    }

}
