/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;


public class GuidesManagementDispatchAction extends PaymentsManagementDispatchAction {
    
    public ActionForward searchEventsForCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer candidacyNumber = getCandidacyNumber(form);
        
        final PaymentsManagementDTO managementDTO = searchEventsForCandidacy(candidacyNumber);
        
        if (managementDTO == null) {
            if (candidacyNumber == null) {
                addActionMessage(request, "error.masterDegreeAdministrativeOffice.payments.invalid.candidacyNumber");
            } else {
                addActionMessage(request, "error.masterDegreeAdministrativeOffice.payments.invalid.candidacyNumber.withNumber", candidacyNumber.toString());
            }
            form.set("candidacyNumber", candidacyNumber);
            return firstPage(mapping, actionForm, request, response);
            
        } else {
            request.setAttribute("paymentsManagementDTO", managementDTO);
            return mapping.findForward("showEvents");
        }
    }
    
    public ActionForward printGuide(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        //TODO: change return actionForward
        return null;
    }
}
