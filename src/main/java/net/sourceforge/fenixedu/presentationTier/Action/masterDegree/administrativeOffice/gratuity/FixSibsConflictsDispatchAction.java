package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.FixSibsEntryByID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadNonProcessedSibsEntries;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.presentationTier.formbeans.masterDegreeAdminOffice.FixSibsPaymentFileEntriesForm;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

@Mapping(path = "/fixSibsPaymentFileEntries", module = "masterDegreeAdministrativeOffice",
        formBeanClass = FixSibsPaymentFileEntriesForm.class)
@Forwards(@Forward(name = "show", path = "showSibsPaymentFileEntries", tileProperties = @Tile(title = "teste48")))
@Exceptions({ @ExceptionHandling(type = NonExistingActionException.class, handler = FenixErrorExceptionHandler.class) })
public class FixSibsConflictsDispatchAction extends FenixDispatchAction {

    @Input
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();

        List infoSibsPaymentFileEntries = null;

        try {
            infoSibsPaymentFileEntries = ReadNonProcessedSibsEntries.run();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(PresentationConstants.SIBS_PAYMENT_FILE_ENTRIES, infoSibsPaymentFileEntries);

        if (infoSibsPaymentFileEntries.isEmpty()) {
            ActionErrors errors = new ActionErrors();
            errors.add("nothingChosen", new ActionError("error.masterDegree.gratuity.nonExistingConflicts"));
            saveErrors(request, errors);

        }

        return mapping.findForward("show");

    }

    public ActionForward fix(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        FixSibsPaymentFileEntriesForm fixSibsPaymentFileEntriesForm = (FixSibsPaymentFileEntriesForm) form;

        String sibsPaymentFileEntryId = fixSibsPaymentFileEntriesForm.getSibsPaymentFileEntryId();

        if (sibsPaymentFileEntryId == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("nothingChosen", new ActionError("error.masterDegree.gratuity.chooseConflictToFix"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        try {
            FixSibsEntryByID.run(sibsPaymentFileEntryId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.getInputForward();

    }

}