package net.sourceforge.fenixedu.presentationTier.Action.personnelSection.payrollSection;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.BonusInstallmentFileBean;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class UploadAnualInstallments extends FenixDispatchAction {

    public ActionForward prepareUploadAnualInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	RenderUtils.invalidateViewState();
	request.setAttribute("bonusInstallmentFileBean", new BonusInstallmentFileBean());
	return mapping.findForward("upload-bonus-file");
    }

    public ActionForward uploadAnualInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	if (isCancelled(request)) {
	    return prepareUploadAnualInstallment(mapping, form, request, response);
	}
	BonusInstallmentFileBean bonusInstallmentFileBean = (BonusInstallmentFileBean) getRenderedObject();
	RenderUtils.invalidateViewState();
	if (bonusInstallmentFileBean.isComplete()) {
	    List<ActionMessage> actionMessageList = (List<ActionMessage>) executeService(request, "ExecuteFactoryMethod",
		    new Object[] { bonusInstallmentFileBean });

	    ActionMessages actionMessages = getMessages(request);
	    if (actionMessageList != null && actionMessageList.size() != 0) {
		for (ActionMessage actionMessage : actionMessageList) {
		    actionMessages.add("error", actionMessage);
		}
	    } else {
		actionMessages.add("success", new ActionMessage("message.successUpdatingEmployeeBonusInstallments"));
	    }
	    saveMessages(request, actionMessages);
	}
	setBonusInstallmentList(request, bonusInstallmentFileBean);
	request.setAttribute("bonusInstallmentFileBean", bonusInstallmentFileBean);
	return mapping.findForward("upload-bonus-file");
    }

    public ActionForward chooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	BonusInstallmentFileBean bonusInstallmentFileBean = (BonusInstallmentFileBean) getRenderedObject("bonusInstallmentFileBeanYear");
	request.setAttribute("bonusInstallmentFileBean", bonusInstallmentFileBean);
	setBonusInstallmentList(request, bonusInstallmentFileBean);
	return mapping.findForward("upload-bonus-file");
    }

    public ActionForward chooseInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	BonusInstallmentFileBean bonusInstallmentFileBean = (BonusInstallmentFileBean) getRenderedObject("bonusInstallmentFileBean");
	RenderUtils.invalidateViewState();

	setBonusInstallmentList(request, bonusInstallmentFileBean);

	request.setAttribute("bonusInstallmentFileBean", bonusInstallmentFileBean);

	return mapping.findForward("upload-bonus-file");
    }

    private void setBonusInstallmentList(HttpServletRequest request, BonusInstallmentFileBean bonusInstallmentFileBean) {
	if (bonusInstallmentFileBean.getYear() != null && bonusInstallmentFileBean.getInstallment() != null) {
	    AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment.readByYearAndInstallment(bonusInstallmentFileBean
		    .getYear(), bonusInstallmentFileBean.getInstallment());
	    request.setAttribute("bonusInstallmentList", anualBonusInstallment.getEmployeeBonusInstallments());
	}
    }

}