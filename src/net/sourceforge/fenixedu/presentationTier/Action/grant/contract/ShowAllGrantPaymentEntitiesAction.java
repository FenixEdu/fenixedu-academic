/*
 * Created on May 18, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Pica
 * @author Barbosa
 */
public class ShowAllGrantPaymentEntitiesAction extends FenixDispatchAction {

    public ActionForward showForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            IUserView userView = SessionUtils.getUserView(request);
            String grantPaymentEntity = null;

            if (verifyParameterInRequest(request, "project")) {
                grantPaymentEntity = GrantProject.class.getName();
                request.setAttribute("project", "yes");
            } else if (verifyParameterInRequest(request, "costcenter")) {
                grantPaymentEntity = GrantCostCenter.class.getName();
                request.setAttribute("costcenter", "yes");
            } else {
                throw new Exception();
            }

            Object[] args = { grantPaymentEntity };
            List grantPaymentList = (List) ServiceUtils.executeService(userView,
                    "ReadAllGrantPaymentEntitiesByClassName", args);
            request.setAttribute("grantPaymentList", grantPaymentList);

            return mapping.findForward("show-payment-entities");
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "show-payment-entities",
                    null);
        }
    }
}