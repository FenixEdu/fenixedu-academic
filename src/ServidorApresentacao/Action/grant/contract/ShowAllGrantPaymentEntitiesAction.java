/*
 * Created on May 18, 2004
 *
 */
package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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
                grantPaymentEntity = "Dominio.grant.contract.GrantProject";
                request.setAttribute("project", "yes");
            } else if (verifyParameterInRequest(request, "costcenter")) {
                grantPaymentEntity = "Dominio.grant.contract.GrantCostCenter";
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