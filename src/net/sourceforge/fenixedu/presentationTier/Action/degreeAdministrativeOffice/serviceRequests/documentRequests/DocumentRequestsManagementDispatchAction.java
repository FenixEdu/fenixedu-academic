package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.serviceRequests.documentRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DocumentRequestsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward showOperations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("showOperations");
    }

    public ActionForward viewNewRequests(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("viewNewRequests");
    }

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("prepareSearch");
    }

}
