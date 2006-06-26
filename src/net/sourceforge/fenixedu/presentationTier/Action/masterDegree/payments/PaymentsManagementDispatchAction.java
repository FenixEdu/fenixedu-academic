/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class PaymentsManagementDispatchAction extends FenixDispatchAction {
    
    protected void addMessage(HttpServletRequest request, ActionMessages actionMessages, String keyMessage, String ... args) {
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(keyMessage, args));
        saveMessages(request, actionMessages);
    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("success");
    }
}
