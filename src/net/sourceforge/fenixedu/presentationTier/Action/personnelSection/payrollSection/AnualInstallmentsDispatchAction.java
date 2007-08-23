package net.sourceforge.fenixedu.presentationTier.Action.personnelSection.payrollSection;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeMonthlyBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.util.BonusType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;

public class AnualInstallmentsDispatchAction extends FenixDispatchAction {

    private final String separator = ";";

    private final String endLine = ";\r\n";

    public ActionForward showAnualInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	AnualBonusInstallmentFactory anualBonusInstallmentFactory = (AnualBonusInstallmentFactory) getRenderedObject("anualBonusInstallmentFactory");
	if (anualBonusInstallmentFactory == null) {
	    anualBonusInstallmentFactory = new AnualBonusInstallmentFactory();
	} else {
	    ActionMessage error = anualBonusInstallmentFactory.updateAnualBonusInstallment();
	    if (error != null) {
		ActionMessages actionMessages = getMessages(request);
		actionMessages.add("message", error);
		saveMessages(request, actionMessages);
	    }
	}
	request.setAttribute("anualBonusInstallmentFactory", anualBonusInstallmentFactory);
	request.setAttribute("anualBonusInstallmentsList", getOrderedAnualBonusInstallmentsList());

	return mapping.findForward("show-anual-installment");
    }

    public ActionForward editAnualInstallment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {

	if (isCancelled(request)) {
	    return showAnualInstallment(mapping, form, request, response);
	}
	AnualBonusInstallmentFactory anualBonusInstallmentFactory = (AnualBonusInstallmentFactory) getRenderedObject("anualBonusInstallmentBean");
	RenderUtils.invalidateViewState();
	ActionMessages actionMessages = getMessages(request);
	if (anualBonusInstallmentFactory == null) {
	    Integer year = new Integer(request.getParameter("year"));
	    anualBonusInstallmentFactory = new AnualBonusInstallmentFactory();
	    anualBonusInstallmentFactory.setYear(year);
	    List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
		    .readByYear(year);
	    if (anualBonusInstallmentList != null) {
		anualBonusInstallmentFactory.setInstallmentsNumber(anualBonusInstallmentList.size());
	    }

	}
	if (anualBonusInstallmentFactory.getAnualBonusInstallmentBeanList() != null) {
	    List<ActionMessage> actionMessageList = (List<ActionMessage>) executeService(request,
		    "ExecuteFactoryMethod", new Object[] { anualBonusInstallmentFactory });
	    if (!actionMessageList.isEmpty()) {
		for (ActionMessage actionMessage : actionMessageList) {
		    actionMessages.add("message", actionMessage);
		}
	    } else {
		actionMessages.add("successMessage", new ActionMessage(
			"message.successUpdatingInstallmentsNumber"));
	    }
	} else {
	    ActionMessage error = anualBonusInstallmentFactory.updateAnualBonusInstallment();
	    if (error != null) {
		actionMessages.add("message", error);
	    }
	}
	saveMessages(request, actionMessages);
	request.setAttribute("anualBonusInstallmentFactory", anualBonusInstallmentFactory);
	request.setAttribute("anualBonusInstallmentsList", getOrderedAnualBonusInstallmentsList());
	return mapping.findForward("show-anual-installment");

    }

    public List<AnualBonusInstallmentFactory> getOrderedAnualBonusInstallmentsList() {
	List<AnualBonusInstallment> anualBonusInstallmentList = new ArrayList<AnualBonusInstallment>(
		rootDomainObject.getAnualBonusInstallments());
	HashMap<Integer, Integer> anualBonusInstallments = new HashMap<Integer, Integer>();

	for (AnualBonusInstallment anualBonusInstallment : anualBonusInstallmentList) {
	    Integer installmentsNumber = anualBonusInstallments.get(anualBonusInstallment.getYear());
	    if (installmentsNumber == null) {
		installmentsNumber = new Integer(0);
	    }
	    installmentsNumber = installmentsNumber + 1;
	    anualBonusInstallments.put(anualBonusInstallment.getYear(), installmentsNumber);

	}

	List<AnualBonusInstallmentFactory> result = new ArrayList<AnualBonusInstallmentFactory>();
	for (Integer year : anualBonusInstallments.keySet()) {
	    Integer installmentsNumber = anualBonusInstallments.get(year);
	    result.add(new AnualBonusInstallmentFactory(year, installmentsNumber));
	}

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
	if (isCancelled(request)) {
	    RenderUtils.invalidateViewState();
	    request.setAttribute("bonusInstallment", new BonusInstallment());
	    return mapping.findForward("show-bonus-installment");
	}
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

    public ActionForward exportBonusInstallmentToGIAF(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException, IOException {
	BonusInstallment bonusInstallment = (BonusInstallment) getRenderedObject();
	if (bonusInstallment == null) {
	    bonusInstallment = new BonusInstallment();
	}
	bonusInstallment.updateList();
	List<EmployeeBonusInstallment> employeeBonusInstallmentList = new ArrayList<EmployeeBonusInstallment>(
		bonusInstallment.getBonusInstallmentList());

	Collections.sort(employeeBonusInstallmentList, new BeanComparator("employee.employeeNumber"));
	StringBuilder stringBuilder = new StringBuilder();
	for (EmployeeBonusInstallment employeeBonusInstallment : employeeBonusInstallmentList) {
	    stringBuilder.append(getLine(employeeBonusInstallment));
	    String aditionalLines = getAditionalLines(employeeBonusInstallment);
	    if (!StringUtils.isEmpty(aditionalLines)) {
		stringBuilder.append(aditionalLines);
	    }
	}
	response.setContentType("text/plain");
	response.addHeader("Content-Disposition", "attachment; filename=bonus_giaf.txt");
	final ServletOutputStream writer = response.getOutputStream();
	byte[] data = stringBuilder.toString().getBytes();
	response.setContentLength(data.length);
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();
	return null;
    }

    public ActionForward exportBonusInstallmentToGIAFP1(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException, IOException {
	BonusInstallment bonusInstallment = (BonusInstallment) getRenderedObject();
	if (bonusInstallment == null) {
	    bonusInstallment = new BonusInstallment();
	}
	bonusInstallment.updateList();
	List<EmployeeBonusInstallment> employeeBonusInstallmentList = new ArrayList<EmployeeBonusInstallment>(
		bonusInstallment.getBonusInstallmentList());

	Collections.sort(employeeBonusInstallmentList, new BeanComparator("employee.employeeNumber"));
	StringBuilder stringBuilder = new StringBuilder();
	for (EmployeeBonusInstallment employeeBonusInstallment : employeeBonusInstallmentList) {
	    if (employeeBonusInstallment.getBonusType() == BonusType.DEDICATION_BONUS) {
		stringBuilder.append(getLine(employeeBonusInstallment));
		String aditionalLines = getAditionalLines(employeeBonusInstallment);
		if (!StringUtils.isEmpty(aditionalLines)) {
		    stringBuilder.append(aditionalLines);
		}
	    }
	}
	response.setContentType("text/plain");
	response.addHeader("Content-Disposition", "attachment; filename=bonus_giaf_p1.txt");
	final ServletOutputStream writer = response.getOutputStream();
	byte[] data = stringBuilder.toString().getBytes();
	response.setContentLength(data.length);
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();
	return null;
    }

    public ActionForward exportBonusInstallmentToGIAFP2(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException, IOException {
	BonusInstallment bonusInstallment = (BonusInstallment) getRenderedObject();
	if (bonusInstallment == null) {
	    bonusInstallment = new BonusInstallment();
	}
	bonusInstallment.updateList();
	List<EmployeeBonusInstallment> employeeBonusInstallmentList = new ArrayList<EmployeeBonusInstallment>(
		bonusInstallment.getBonusInstallmentList());

	Collections.sort(employeeBonusInstallmentList, new BeanComparator("employee.employeeNumber"));
	StringBuilder stringBuilder = new StringBuilder();
	for (EmployeeBonusInstallment employeeBonusInstallment : employeeBonusInstallmentList) {
	    if (employeeBonusInstallment.getBonusType() == BonusType.EXCEPTIONAL_BONUS) {
		stringBuilder.append(getLine(employeeBonusInstallment));
		String aditionalLines = getAditionalLines(employeeBonusInstallment);
		if (!StringUtils.isEmpty(aditionalLines)) {
		    stringBuilder.append(aditionalLines);
		}
	    }
	}
	response.setContentType("text/plain");
	response.addHeader("Content-Disposition", "attachment; filename=bonus_giaf_p2.txt");
	final ServletOutputStream writer = response.getOutputStream();
	byte[] data = stringBuilder.toString().getBytes();
	response.setContentLength(data.length);
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();
	return null;
    }

    private String getAditionalLines(EmployeeBonusInstallment employeeBonusInstallment) {
	StringBuilder stringBuilder = new StringBuilder();
	for (EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment : employeeBonusInstallment
		.getEmployeeMonthlyBonusInstallments()) {
	    if (employeeMonthlyBonusInstallment.getValue() < 0) {
		stringBuilder.append(
			new DecimalFormat("000000").format(employeeBonusInstallment.getEmployee()
				.getEmployeeNumber())).append(separator);
		stringBuilder.append(getMovementCode(employeeBonusInstallment.getBonusType())).append(
			separator);
		stringBuilder.append(
			new DecimalFormat("0000").format(employeeBonusInstallment.getCostCenterCode()))
			.append(separator);
		stringBuilder.append(employeeMonthlyBonusInstallment.getValue()).append(separator);
		stringBuilder.append("3").append(separator);
		stringBuilder.append(employeeBonusInstallment.getSubCostCenterCode()).append(separator);
		stringBuilder.append(employeeBonusInstallment.getExplorationUnit()).append(separator);
		stringBuilder.append(
			employeeMonthlyBonusInstallment.getPartialYearMonth().get(
				DateTimeFieldType.year())).append(separator);
		stringBuilder.append(
			getMonthString(employeeMonthlyBonusInstallment.getPartialYearMonth().get(
				DateTimeFieldType.monthOfYear()))).append(endLine);
	    }
	}
	return stringBuilder.toString();
    }

    private String getMonthString(int month) {
	if (month < 10) {
	    return "0" + month;
	}
	return "" + month;
    }

    private String getLine(EmployeeBonusInstallment employeeBonusInstallment) {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(
		new DecimalFormat("000000").format(employeeBonusInstallment.getEmployee()
			.getEmployeeNumber())).append(separator);
	stringBuilder.append(getMovementCode(employeeBonusInstallment.getBonusType())).append(separator);
	stringBuilder.append(
		new DecimalFormat("0000").format(employeeBonusInstallment.getCostCenterCode())).append(
		separator);
	stringBuilder.append(employeeBonusInstallment.getValue()).append(separator);
	stringBuilder.append("3").append(separator);
	stringBuilder.append(employeeBonusInstallment.getSubCostCenterCode()).append(separator);
	stringBuilder.append(employeeBonusInstallment.getExplorationUnit()).append(separator);
	stringBuilder.append(
		employeeBonusInstallment.getAnualBonusInstallment().getPaymentPartialDate().get(
			DateTimeFieldType.year())).append(separator);
	stringBuilder.append(
		getMonthString(employeeBonusInstallment.getAnualBonusInstallment()
			.getPaymentPartialDate().get(DateTimeFieldType.monthOfYear()))).append(endLine);
	return stringBuilder.toString();
    }

    private String getMovementCode(BonusType bonusType) {
	if (bonusType.equals(BonusType.DEDICATION_BONUS)) {
	    return "6001";
	} else {
	    return "6002";
	}
    }

}