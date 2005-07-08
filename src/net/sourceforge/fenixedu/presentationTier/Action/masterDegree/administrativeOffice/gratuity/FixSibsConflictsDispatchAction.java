package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class FixSibsConflictsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Object args[] = {};
        List infoSibsPaymentFileEntries = null;

        try {
            infoSibsPaymentFileEntries = (List) ServiceUtils.executeService(userView,
                    "ReadNonProcessedSibsEntries", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(SessionConstants.SIBS_PAYMENT_FILE_ENTRIES, infoSibsPaymentFileEntries);

        if (infoSibsPaymentFileEntries.isEmpty()) {
            ActionErrors errors = new ActionErrors();
            errors.add("nothingChosen", new ActionError(
                    "error.masterDegree.gratuity.nonExistingConflicts"));
            saveErrors(request, errors);

        }

        return mapping.findForward("show");

    }

    public ActionForward fix(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm fixSibsPaymentFileEntriesForm = (DynaActionForm) form;

        Integer sibsPaymentFileEntryId = (Integer) fixSibsPaymentFileEntriesForm
                .get("sibsPaymentFileEntryId");

        if (sibsPaymentFileEntryId == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("nothingChosen", new ActionError(
                    "error.masterDegree.gratuity.chooseConflictToFix"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        Object args[] = { sibsPaymentFileEntryId };
        try {
            ServiceUtils.executeService(userView, "FixSibsEntryByID", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.getInputForward();

    }

}