package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessMonthlyResume;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeAnualInfo;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class ExportAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	String action = request.getParameter("action");
	request.setAttribute("action", action);
	String chooseBetweenDates = request.getParameter("chooseBetweenDates");
	AssiduousnessExportChoices assiduousnessExportChoices = new AssiduousnessExportChoices(action);
	if (chooseBetweenDates != null && chooseBetweenDates.length() != 0 && new Boolean(chooseBetweenDates) == true) {
	    assiduousnessExportChoices.setCanChooseDateType(true);
	}
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	return mapping.findForward("choose-year-month");
    }

    public ActionForward chooseAssiduousnessExportChoicesPostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	RenderUtils.invalidateViewState();
	request.setAttribute("action", assiduousnessExportChoices.getAction());
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	return mapping.findForward("choose-year-month");
    }

    public ActionForward exportWorkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
	final IUserView userView = SessionUtils.getUserView(request);
	List<EmployeeWorkSheet> employeeWorkSheetList = (List<EmployeeWorkSheet>) ServiceUtils.executeService(userView,
		"ReadAllAssiduousnessWorkSheets", new Object[] { assiduousnessExportChoices });
	if (employeeWorkSheetList.size() != 0) {
	    Map<String, String> parameters = new HashMap<String, String>();
	    if (assiduousnessExportChoices.getYearMonth() != null) {
		ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils
			.getLocale());
		String month = bundleEnumeration.getString(assiduousnessExportChoices.getYearMonth().getMonth().toString());
		StringBuilder stringBuilder = new StringBuilder(month).append(" ").append(
			assiduousnessExportChoices.getYearMonth().getYear());
		parameters.put("yearMonth", stringBuilder.toString());
	    } else {
		parameters.put("yearMonth", " ");
	    }
	    String path = getServlet().getServletContext().getRealPath("/");
	    parameters.put("path", path);
	    ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(new BeanComparator("unitCode"));
	    comparatorChain.addComparator(new BeanComparator("employee.employeeNumber"));
	    Collections.sort(employeeWorkSheetList, comparatorChain);
	    response.setContentType("application/pdf");
	    response.addHeader("Content-Disposition", "attachment; filename=verbetes.pdf");
	    byte[] data = ReportsUtils.exportToPdf("assiduousness.workDaySheet", parameters, bundle, employeeWorkSheetList);
	    response.setContentLength(data.length);
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	} else {
	    setError(request, assiduousnessExportChoices, "error.noWorkScheduleToExport");
	    request.setAttribute("action", "exportWorkSheets");
	    return mapping.findForward("choose-year-month");
	}
	return mapping.findForward("");
    }

    public ActionForward exportMonthResume(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	assiduousnessExportChoices.setYearMonth();
	final IUserView userView = SessionUtils.getUserView(request);
	List<AssiduousnessMonthlyResume> assiduousnessMonthlyResumeList = (List<AssiduousnessMonthlyResume>) ServiceUtils
		.executeService(userView, "ReadMonthResume", new Object[] { assiduousnessExportChoices });
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=resumoMes.xls");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(bundle.getString("label.monthlyResume"));
	AssiduousnessMonthlyResume.getExcelHeader(spreadsheet, bundle);
	for (AssiduousnessMonthlyResume assiduousnessMonthlyResume : assiduousnessMonthlyResumeList) {
	    assiduousnessMonthlyResume.getExcelRow(spreadsheet);
	}
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    public ActionForward exportJustifications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	assiduousnessExportChoices.setYearMonth();
	final IUserView userView = SessionUtils.getUserView(request);
	StyledExcelSpreadsheet spreadsheet = (StyledExcelSpreadsheet) ServiceUtils.executeService(userView,
		"ExportJustifications", new Object[] { assiduousnessExportChoices });
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=justificacoes.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    public ActionForward prepareExportAssignedEmployees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("yearMonth", new YearMonth(new YearMonthDay()));
	request.setAttribute("employeesAnualInfo", "employeesAnualInfo");
	request.setAttribute("action", request.getParameter("action"));
	return mapping.findForward("choose-year-month");
    }

    public ActionForward prepareExportADISTEmployees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("yearMonth", new YearMonth(new YearMonthDay()));
	request.setAttribute("employeesAnualInfo", "employeesAnualInfo");
	request.setAttribute("action", request.getParameter("action"));
	return mapping.findForward("choose-year-month");
    }

    public ActionForward exportADISTEmployees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AssiduousnessStatus assiduousnessStatus = AssiduousnessStatus
		.getAssiduousnessStatusByDescription("Contratado pela ADIST");
	String fileName = new String("listagemADIST.pdf");
	String action = new String("exportADISTEmployees");
	return exportEmployeesAnualInfo(mapping, actionForm, request, response, assiduousnessStatus, fileName, action);
    }

    public ActionForward exportAssignedEmployees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AssiduousnessStatus assiduousnessStatus = AssiduousnessStatus.getAssiduousnessStatusByDescription("Destacado no IST");
	String fileName = new String("listagemDestacados.pdf");
	String action = new String("exportAssignedEmployees");
	return exportEmployeesAnualInfo(mapping, actionForm, request, response, assiduousnessStatus, fileName, action);
    }

    public ActionForward exportEmployeesAnualInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, AssiduousnessStatus assiduousnessStatus, String fileName, String action)
	    throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	final IUserView userView = SessionUtils.getUserView(request);

	if (!isMonthClosed(yearMonth)) {
	    addError(request, "error.monthNotClosed");
	    request.setAttribute("yearMonth", yearMonth);
	    request.setAttribute("employeesAnualInfo", "employeesAnualInfo");
	    request.setAttribute("action", action);
	    return mapping.findForward("choose-year-month");
	}

	List<EmployeeAnualInfo> assignedEmployeeInfoList = (List<EmployeeAnualInfo>) ServiceUtils.executeService(userView,
		"ExportEmployeesAnualInfo", new Object[] { yearMonth, assiduousnessStatus });

	Collections.sort(assignedEmployeeInfoList, new BeanComparator("employee.employeeNumber"));
	byte[] data = ReportsUtils.exportToPdf("personnelSection.employeesAnualInfo", null, null, assignedEmployeeInfoList);
	response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.setHeader("Content-disposition", "attachment; filename=" + fileName);

	final ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();

	response.flushBuffer();
	return null;
    }

    private boolean isMonthClosed(YearMonth yearMonth) {
	for (ClosedMonth closedMonth : rootDomainObject.getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()) == yearMonth.getYear()
		    && closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) == yearMonth.getNumberOfMonth()) {
		return true;
	    }
	}
	return false;
    }

    private void addError(HttpServletRequest request, String errorMsg) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("message", new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, AssiduousnessExportChoices assiduousnessExportChoices, String errorMsg) {
	RenderUtils.invalidateViewState();
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("message", new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }

}
