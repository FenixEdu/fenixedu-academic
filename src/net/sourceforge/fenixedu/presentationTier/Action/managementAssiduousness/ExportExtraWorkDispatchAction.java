package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.EmployeeExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;

public class ExportExtraWorkDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("action", getFromRequest(request, "action"));
	request.setAttribute("chooseMonth", getFromRequest(request, "chooseMonth"));
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = getYearMonthToExport();
	}
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("choose-year-month");
    }

    private YearMonth getYearMonthToExport() {
	ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed(true);
	if (lastClosedMonth == null) {
	    return null;
	}
	return new YearMonth(lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.year()),
		lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()));
    }

    public ActionForward exportByEmployees(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("action", (String) getRenderedObject("action"));
	request.setAttribute("chooseMonth", (String) getRenderedObject("chooseMonth"));
	final YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    return chooseYearMonth(mapping, actionForm, request, response);
	}
	if (!ClosedMonth.isMonthClosedForExtraWork(yearMonth.getPartial())) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage(
		    "error.extraWorkRequest.extraWorkMonthNotClosed"));
	    saveMessages(request, actionMessages);
	    return chooseYearMonth(mapping, actionForm, request, response);
	}
	List<ExtraWorkRequest> extraWorkRequests = getExtraWorkRequests(yearMonth);
	if (extraWorkRequests == null || extraWorkRequests.size() == 0) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("message.noPaymentRequests"));
	    saveMessages(request, actionMessages);
	    return chooseYearMonth(mapping, actionForm, request, response);
	}
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("unit.costCenterCode"));
	comparatorChain.addComparator(new BeanComparator("assiduousness.employee.employeeNumber"));
	Collections.sort(extraWorkRequests, comparatorChain);
	Map<String, String> parameters = new HashMap<String, String>();
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	StringBuilder stringBuilder = new StringBuilder(month).append(" ").append(yearMonth.getYear());
	parameters.put("yearMonth", stringBuilder.toString());

	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename=trabalhoExtraFunc.pdf");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	byte[] data = ReportsUtils.exportToPdf("assiduousness.employeeExtraWork", parameters, bundle,
		extraWorkRequests);
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();
	return null;
    }

    public ActionForward exportByUnits(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("action", (String) getRenderedObject("action"));
	request.setAttribute("chooseMonth", (String) getRenderedObject("chooseMonth"));
	final YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    return chooseYearMonth(mapping, actionForm, request, response);
	}

	List<UnitExtraWorkAmount> unitExtraWorkAmountList = getUnitExtraWorkAmountsByYear(yearMonth
		.getYear());
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	final ResourceBundle enumBundle = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());

	String sheetName = bundle.getString("title.extraWork");
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(sheetName);
	spreadsheet.newHeaderRow();
	spreadsheet.addCell(bundle.getString("message.unitExtraWorkPlafon")
		+ yearMonth.getYear().toString(), spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 3));
	spreadsheet.newHeaderRow();

	UnitExtraWorkAmount.getExcelHeader(spreadsheet, bundle, enumBundle);
	for (UnitExtraWorkAmount unitExtraWorkAmount : unitExtraWorkAmountList) {
	    unitExtraWorkAmount.getExcelRow(spreadsheet);
	    unitExtraWorkAmount.getExtraWorkAuthorizationsExcelRows(spreadsheet, bundle, enumBundle);
	    spreadsheet.getSheet(sheetName);
	}
	for (int i = 1; i < spreadsheet.getWorkbook().getNumberOfSheets(); i++) {
	    String thisSheetName = spreadsheet.getWorkbook().getSheetName(i);
	    spreadsheet.getSheet(thisSheetName);
	    EmployeeExtraWorkAuthorization.getExcelFooter(spreadsheet, bundle);
	}
	spreadsheet.getSheet(sheetName);
	UnitExtraWorkAmount.getExcelFooter(spreadsheet, bundle);

	response.setContentType("text/plain");
	response.addHeader("Content-Disposition", "attachment; filename=trabalhoExtraCC.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private List<UnitExtraWorkAmount> getUnitExtraWorkAmountsByYear(Integer year) {
	List<UnitExtraWorkAmount> result = new ArrayList<UnitExtraWorkAmount>();
	for (UnitExtraWorkAmount unitExtraWorkAmount : rootDomainObject.getUnitsExtraWorkAmounts()) {
	    if (unitExtraWorkAmount.getYear().equals(year)) {
		result.add(unitExtraWorkAmount);
	    }
	}
	Collections.sort(result, new BeanComparator("unit.costCenterCode"));
	return result;
    }

    private List<ExtraWorkRequest> getExtraWorkRequests(YearMonth yearMonth) {
	List<ExtraWorkRequest> extraWorkRequests = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest extraWorkRequest : rootDomainObject.getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().equals(yearMonth.getPartial())) {
		extraWorkRequests.add(extraWorkRequest);
	    }
	}
	return extraWorkRequests;
    }

    protected String getFromRequest(HttpServletRequest request, String value) {
	String result = request.getParameter(value);
	if (result == null || result.length() == 0) {
	    result = (String) request.getAttribute(value);
	}
	return result;
    }

}
