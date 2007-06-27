package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextLookupDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixExecutionDegreeAndCurricularYearContextLookupDispatchAction extends
        FenixContextLookupDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setCurricularYearContext(request);

        return actionForward;
    }

}
