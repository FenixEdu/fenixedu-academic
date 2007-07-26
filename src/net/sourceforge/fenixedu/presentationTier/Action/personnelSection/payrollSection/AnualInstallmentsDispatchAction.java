package net.sourceforge.fenixedu.presentationTier.Action.personnelSection.payrollSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.AnualBonusInstallmentFactory;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.BonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AnualInstallmentsDispatchAction extends FenixDispatchAction {

    public ActionForward showAnualInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	AnualBonusInstallmentFactory anualBonusInstallmentFactory = (AnualBonusInstallmentFactory) getRenderedObject("anualBonusInstallmentFactory");
	if (anualBonusInstallmentFactory == null) {
	    anualBonusInstallmentFactory = new AnualBonusInstallmentFactory();
	} else {
	    anualBonusInstallmentFactory.updateAnualBonusInstallment();
	}
	request.setAttribute("anualBonusInstallmentFactory", anualBonusInstallmentFactory);
	//request.setAttribute("anualBonusInstallmentsList", getOrderedAnualBonusInstallmentsList());
	return mapping.findForward("show-anual-installment");
    }

    public ActionForward editAnualInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	AnualBonusInstallmentFactory anualBonusInstallmentFactory = (AnualBonusInstallmentFactory) getRenderedObject("anualBonusInstallmentBean");
	//	if(anualBonusInstallmentFactory == null) {
	//	    anualBonusInstallmentFactory = (AnualBonusInstallmentFactory) getRenderedObject("anualBonusInstallmentList");
	//	}
	RenderUtils.invalidateViewState();
	if (anualBonusInstallmentFactory.getAnualBonusInstallmentBeanList() != null) {
	    ActionMessage actionMessage = (ActionMessage) executeService(request,
		    "ExecuteFactoryMethod", new Object[] { anualBonusInstallmentFactory });
	    ActionMessages actionMessages = getMessages(request);
	    if (actionMessage != null) {
		actionMessages.add("message", actionMessage);
	    } else {
		actionMessages.add("successMessage", new ActionMessage(
			"message.successUpdatingInstallmentsNumber"));
	    }
	    saveMessages(request, actionMessages);
	    // request.setAttribute("anualBonusInstallmentsList", getOrderedAnualBonusInstallmentsList());

	} else {
	    anualBonusInstallmentFactory.updateAnualBonusInstallment();
	}

	request.setAttribute("anualBonusInstallmentFactory", anualBonusInstallmentFactory);
	return mapping.findForward("show-anual-installment");

    }

    public List<AnualBonusInstallment> getOrderedAnualBonusInstallmentsList() {
	List<AnualBonusInstallment> result = new ArrayList<AnualBonusInstallment>(rootDomainObject
		.getAnualBonusInstallments());
	Collections.sort(result, new BeanComparator("year"));
	return result;
    }

    public ActionForward showEmptyBonusInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	request.setAttribute("bonusInstallment", new BonusInstallment());
	return mapping.findForward("show-bonus-installment");
    }

    public ActionForward showBonusInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	BonusInstallment bonusInstallment = (BonusInstallment) getRenderedObject();
	if (bonusInstallment == null) {
	    bonusInstallment = new BonusInstallment();
	}
	bonusInstallment.updateList();
	request.setAttribute("bonusInstallment", bonusInstallment);
	return mapping.findForward("show-bonus-installment");
    }

    public ActionForward exportBonusInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException, IOException {
	BonusInstallment bonusInstallment = (BonusInstallment) getRenderedObject();
	if (bonusInstallment == null) {
	    bonusInstallment = new BonusInstallment();
	}
	bonusInstallment.updateList();

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	final ResourceBundle enumBundle = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(bundle.getString("label.bonus"));

	bonusInstallment.getExcelHeader(spreadsheet, bundle, enumBundle);
	bonusInstallment.getRows(spreadsheet, enumBundle);

	spreadsheet.setRegionBorder(0, spreadsheet.getSheet().getLastRowNum() + 1, 0, spreadsheet
		.getMaxiumColumnNumber() + 1);

	response.setContentType("text/plain");
	response.addHeader("Content-Disposition", "attachment; filename=bonus.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

}