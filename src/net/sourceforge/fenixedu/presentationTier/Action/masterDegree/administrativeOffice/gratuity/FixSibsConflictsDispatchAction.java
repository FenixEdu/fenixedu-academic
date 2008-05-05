package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.formbeans.masterDegreeAdminOffice.FixSibsPaymentFileEntriesForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Exceptions;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Input;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

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

@Mapping(path = "/fixSibsPaymentFileEntries", module = "posGraduacao", formBeanClass = FixSibsPaymentFileEntriesForm.class)
@Forwards(@Forward(name = "show", path = "showSibsPaymentFileEntries"))
@Exceptions(NonExistingActionException.class)
public class FixSibsConflictsDispatchAction extends FenixDispatchAction {

    @Input
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	Object args[] = {};
	List infoSibsPaymentFileEntries = null;

	try {
	    infoSibsPaymentFileEntries = (List) ServiceUtils.executeService(userView, "ReadNonProcessedSibsEntries", args);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	request.setAttribute(SessionConstants.SIBS_PAYMENT_FILE_ENTRIES, infoSibsPaymentFileEntries);

	if (infoSibsPaymentFileEntries.isEmpty()) {
	    ActionErrors errors = new ActionErrors();
	    errors.add("nothingChosen", new ActionError("error.masterDegree.gratuity.nonExistingConflicts"));
	    saveErrors(request, errors);

	}

	return mapping.findForward("show");

    }

    public ActionForward fix(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = SessionUtils.getUserView(request);
	FixSibsPaymentFileEntriesForm fixSibsPaymentFileEntriesForm = (FixSibsPaymentFileEntriesForm) form;

	Integer sibsPaymentFileEntryId = fixSibsPaymentFileEntriesForm.getSibsPaymentFileEntryId();

	if (sibsPaymentFileEntryId == null) {
	    ActionErrors errors = new ActionErrors();
	    errors.add("nothingChosen", new ActionError("error.masterDegree.gratuity.chooseConflictToFix"));
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