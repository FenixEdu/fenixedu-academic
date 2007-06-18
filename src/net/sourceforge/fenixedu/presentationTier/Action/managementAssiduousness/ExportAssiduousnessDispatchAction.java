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
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ExportAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	String action = request.getParameter("action");
	request.setAttribute("action", action);
	String chooseBetweenDates = request.getParameter("chooseBetweenDates");
	AssiduousnessExportChoices assiduousnessExportChoices = new AssiduousnessExportChoices(action);
	if (chooseBetweenDates != null && chooseBetweenDates.length() != 0
		&& new Boolean(chooseBetweenDates) == true) {
	    assiduousnessExportChoices.setCanChooseDateType(true);
	}
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	return mapping.findForward("choose-year-month");
    }

    public ActionForward chooseAssiduousnessExportChoicesPostBack(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws FenixServiceException, FenixFilterException {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	RenderUtils.invalidateViewState();
	request.setAttribute("action", assiduousnessExportChoices.getAction());
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	return mapping.findForward("choose-year-month");
    }

    public ActionForward exportWorkSheets(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	final IUserView userView = SessionUtils.getUserView(request);
	List<EmployeeWorkSheet> employeeWorkSheetList = (List<EmployeeWorkSheet>) ServiceUtils
		.executeService(userView, "ReadAllAssiduousnessWorkSheets",
			new Object[] { assiduousnessExportChoices });
	if (employeeWorkSheetList.size() != 0) {
	    Map<String, String> parameters = new HashMap<String, String>();
	    if (assiduousnessExportChoices.getYearMonth() != null) {
		ResourceBundle bundleEnumeration = ResourceBundle.getBundle(
			"resources.EnumerationResources", LanguageUtils.getLocale());
		String month = bundleEnumeration.getString(assiduousnessExportChoices.getYearMonth()
			.getMonth().toString());
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
	    byte[] data = ReportsUtils.exportToPdf("assiduousness.workDaySheet", parameters, bundle,
		    employeeWorkSheetList);
	    response.setContentLength(data.length);
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	} else {
	    setError(request, assiduousnessExportChoices, "error.noWorkScheduleToExport");
	    return mapping.findForward("choose-year-month");
	}
	return mapping.findForward("");
    }

    public ActionForward exportMonthResume(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	assiduousnessExportChoices.setYearMonth();
	final IUserView userView = SessionUtils.getUserView(request);
	List<AssiduousnessMonthlyResume> assiduousnessMonthlyResumeList = (List<AssiduousnessMonthlyResume>) ServiceUtils
		.executeService(userView, "ReadMonthResume", new Object[] { assiduousnessExportChoices });
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=resumoMês.xls");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(bundle
		.getString("label.monthlyResume"));
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

    public ActionForward exportJustifications(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");
	assiduousnessExportChoices.setYearMonth();
	final IUserView userView = SessionUtils.getUserView(request);
	StyledExcelSpreadsheet spreadsheet = (StyledExcelSpreadsheet) ServiceUtils.executeService(
		userView, "ExportJustifications", new Object[] { assiduousnessExportChoices });
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=justificacoes.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void setError(HttpServletRequest request,
	    AssiduousnessExportChoices assiduousnessExportChoices, String errorMsg) {
	RenderUtils.invalidateViewState();
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("message", new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }

}
