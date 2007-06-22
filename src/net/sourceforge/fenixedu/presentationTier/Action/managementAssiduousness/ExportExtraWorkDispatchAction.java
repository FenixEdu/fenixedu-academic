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
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeFieldType;

public class ExportExtraWorkDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	String action = request.getParameter("action");
	request.setAttribute("action", action);
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
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    return chooseYearMonth(mapping, actionForm, request, response);
	}

	List<ExtraWorkRequest> extraWorkRequests = getExtraWorkRequests(yearMonth);

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

    private List<ExtraWorkRequest> getExtraWorkRequests(YearMonth yearMonth) {
	List<ExtraWorkRequest> extraWorkRequests = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest extraWorkRequest : rootDomainObject.getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().equals(yearMonth.getPartial())) {
		extraWorkRequests.add(extraWorkRequest);
	    }
	}
	return extraWorkRequests;
    }

}
