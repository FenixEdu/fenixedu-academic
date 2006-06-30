/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class GuidesManagementDispatchAction extends PaymentsManagementDispatchAction {
        
    public ActionForward printGuide(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        //PaymentsManagementDTO managementDTO = (PaymentsManagementDTO) RenderUtils.getViewState("payments-guides").getMetaObject().getObject();
        //request.setAttribute("paymentsManagementDTO", managementDTO);
        
        
        
        return mapping.findForward("printGuide");
    }
}
