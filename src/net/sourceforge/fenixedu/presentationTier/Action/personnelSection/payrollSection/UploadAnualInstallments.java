package net.sourceforge.fenixedu.presentationTier.Action.personnelSection.payrollSection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.BonusInstallmentFileBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class UploadAnualInstallments extends FenixDispatchAction {

    public ActionForward prepareUploadAnualInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	RenderUtils.invalidateViewState();
	request.setAttribute("bonusInstallmentFileBean", new BonusInstallmentFileBean());
	return mapping.findForward("upload-bonus-file");
    }

    public ActionForward uploadAnualInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	BonusInstallmentFileBean bonusInstallmentFileBean = (BonusInstallmentFileBean) getRenderedObject("bonusInstallmentFileBean");
	RenderUtils.invalidateViewState();
	if (bonusInstallmentFileBean.isComplete()) {
	    ActionMessage actionMessage = (ActionMessage) executeService(request,
		    "ExecuteFactoryMethod", new Object[] { bonusInstallmentFileBean });
	    ActionMessages actionMessages = getMessages(request);
	    if (actionMessage != null) {
		actionMessages.add("message", actionMessage);
	    } else {
		actionMessages.add("successMessage", new ActionMessage(
			"message.successUpdatingEmployeeBonusInstallments"));
	    }
	    saveMessages(request, actionMessages);
	}

	request.setAttribute("bonusInstallmentFileBean", bonusInstallmentFileBean);
	return mapping.findForward("upload-bonus-file");
    }

    public ActionForward chooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute("bonusInstallmentFileBean",
		(BonusInstallmentFileBean) getRenderedObject("bonusInstallmentFileBeanYear"));
	return mapping.findForward("upload-bonus-file");
    }

}