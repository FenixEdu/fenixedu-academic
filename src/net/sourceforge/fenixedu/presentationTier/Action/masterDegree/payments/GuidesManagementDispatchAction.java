/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;


public class GuidesManagementDispatchAction extends PaymentsManagementDispatchAction {
        
    public ActionForward printGuide(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        //PaymentsManagementDTO managementDTO = (PaymentsManagementDTO) RenderUtils.getViewState("payments-guides").getMetaObject().getObject();
        //request.setAttribute("paymentsManagementDTO", managementDTO);
        
        
        
        return mapping.findForward("printGuide");
    }
}
