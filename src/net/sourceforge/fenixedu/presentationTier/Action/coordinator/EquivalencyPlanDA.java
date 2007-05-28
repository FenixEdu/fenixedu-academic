package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("showPlan");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	final String degreeCurricularPlanIDString = request.getParameter("degreeCurricularPlanID");
	final Integer degreeCurricularPlanID = getInteger(degreeCurricularPlanIDString);
	return degreeCurricularPlanID == null ? null : RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);
    }
    
    private Integer getInteger(final String string) {
	return isValidNumber(string) ? Integer.valueOf(string) : null;
    }

    private boolean isValidNumber(final String string) {
	return string != null && string.length() > 0 && StringUtils.isNumeric(string);
    }

}
