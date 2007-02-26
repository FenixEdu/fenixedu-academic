package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheetToExportSearch;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

public class ExportAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("yearMonth", new YearMonth(new YearMonthDay()));
	request.setAttribute("workDaySheetToExportSearch", new WorkDaySheetToExportSearch());
	return mapping.findForward("choose-year-month");
    }

    public ActionForward exportToPDFWorkDaySheet(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject();
	if (yearMonth == null) {
	    yearMonth = new YearMonth(new YearMonthDay());
	}
	Object[] args = { new WorkDaySheetToExportSearch(yearMonth) };
	int result = exportWorkSheetListToPDF(request, response, yearMonth, args);
	if (result == 0) {
	    setError(request, new WorkDaySheetToExportSearch(), yearMonth,
		    "error.noWorkScheduleToExport");
	    return mapping.findForward("choose-year-month");
	}
	return mapping.findForward("");
    }

    public ActionForward exportToPDFChoosedWorkDaySheet(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	WorkDaySheetToExportSearch workDaySheetToExportSearch = (WorkDaySheetToExportSearch) getRenderedObject();
	if (workDaySheetToExportSearch == null) {
	    workDaySheetToExportSearch = new WorkDaySheetToExportSearch();
	}
	Object[] args = { workDaySheetToExportSearch };
	int result = exportWorkSheetListToPDF(request, response, null, args);
	if (result == 0) {
	    setError(request, workDaySheetToExportSearch, new YearMonth(new YearMonthDay()),
		    "error.noWorkScheduleToExport");
	    return mapping.findForward("choose-year-month");
	}
	return mapping.findForward("");
    }

    private void setError(HttpServletRequest request,
	    WorkDaySheetToExportSearch workDaySheetToExportSearch, YearMonth yearMonth, String errorMsg) {
	RenderUtils.invalidateViewState();
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("workDaySheetToExportSearch", workDaySheetToExportSearch);
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("message", new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }

    private int exportWorkSheetListToPDF(HttpServletRequest request, HttpServletResponse response,
	    YearMonth yearMonth, Object[] args) throws FenixServiceException, FenixFilterException,
	    JRException, IOException {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	final IUserView userView = SessionUtils.getUserView(request);
	List<EmployeeWorkSheet> employeeWorkSheetList = (List<EmployeeWorkSheet>) ServiceUtils
		.executeService(userView, "ReadAllAssiduousnessWorkSheets", args);
	if (employeeWorkSheetList.size() != 0) {
	    Map<String, String> parameters = new HashMap<String, String>();
	    if (yearMonth != null) {
		ResourceBundle bundleEnumeration = ResourceBundle.getBundle(
			"resources.EnumerationResources", LanguageUtils.getLocale());
		String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
		StringBuilder stringBuilder = new StringBuilder(month).append(" ").append(
			yearMonth.getYear());
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
	}
	return employeeWorkSheetList.size();
    }
}
