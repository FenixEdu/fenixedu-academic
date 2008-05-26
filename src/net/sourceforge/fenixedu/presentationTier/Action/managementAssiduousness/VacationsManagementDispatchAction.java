package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessVacations;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class VacationsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("action", getFromRequest(request, "action"));
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = getYearMonth(request);
	}
	request.setAttribute("chooseYear", Boolean.TRUE);
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("choose-year-month");
    }

    public ActionForward calculateA17AndA18(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = getYearMonth(request);
	}
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=a17a18.xls");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("A17A18");
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.employeeNumber"));
	spreadsheet.addHeader(bundle.getString("label.employee.name"));
	spreadsheet.addHeader(bundle.getString("label.lastYearEfectiveWorkDays"));
	spreadsheet.addHeader(bundle.getString("label.art17And18MaximumLimitDays"));
	spreadsheet.addHeader(bundle.getString("label.art17And18LimitDays"));
	spreadsheet.addHeader(bundle.getString("label.art17Days"));
	spreadsheet.addHeader(bundle.getString("label.art18Days"));

	final ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils
		.getLocale());
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(bundleEnumeration.getString(month.getName()));
	}

	spreadsheet.addHeader("Média de horas");

	ServiceUtils.executeService(SessionUtils.getUserView(request), "CalculateArticles17And18", new Object[] { yearMonth
		.getYear() });

	YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear() - 1, 1, 1);
	YearMonthDay endDate = new YearMonthDay(yearMonth.getYear() - 1, 12, 31);

	for (AssiduousnessVacations assiduousnessVacations : rootDomainObject.getAssiduousnessVacations()) {
	    if (assiduousnessVacations.getYear().equals(yearMonth.getYear())) {
		AssiduousnessStatusHistory assiduousnessStatusHistory = assiduousnessVacations.getAssiduousness()
			.getLastAssiduousnessStatusHistoryBetween(beginDate, endDate);
		if (assiduousnessStatusHistory != null) {
		    spreadsheet.newRow();
		    spreadsheet.addCell(assiduousnessVacations.getAssiduousness().getEmployee().getEmployeeNumber().toString());
		    spreadsheet.addCell(assiduousnessVacations.getAssiduousness().getEmployee().getPerson().getName());

		    spreadsheet.addCell(assiduousnessVacations.getEfectiveWorkDays());
		    spreadsheet.addCell(assiduousnessVacations.getArt17And18MaximumLimitDays());
		    spreadsheet.addCell(assiduousnessVacations.getArt17And18LimitDays());
		    spreadsheet.addCell(assiduousnessVacations.getNumberOfArt17());
		    spreadsheet.addCell(assiduousnessVacations.getNumberOfArt18());

		    List<AssiduousnessClosedMonth> assiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>(
			    assiduousnessStatusHistory.getAssiduousnessClosedMonths());
		    Collections.sort(assiduousnessClosedMonths, new BeanComparator("beginDate"));

		    for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessClosedMonths) {
			if (assiduousnessClosedMonth.getBeginDate().get(DateTimeFieldType.year()) == (yearMonth.getYear() - 1)) {
			    spreadsheet.addDuration(assiduousnessClosedMonth.getTotalWorkedTime());
			}
		    }

		    List<Schedule> schedules = assiduousnessVacations.getAssiduousness().getSchedules(beginDate, endDate);
		    Duration averageWorkPeriodDuration = Duration.ZERO;
		    for (Schedule schedule : schedules) {
			averageWorkPeriodDuration = averageWorkPeriodDuration.plus(schedule.getAverageWorkPeriodDuration());
		    }
		    spreadsheet.addDuration(new Duration(averageWorkPeriodDuration.getMillis() / schedules.size()));
		}
	    }
	}
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private YearMonth getYearMonth(HttpServletRequest request) {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = (YearMonth) request.getAttribute("yearMonth");
	}
	if (yearMonth == null) {
	    yearMonth = new YearMonth();
	    String year = request.getParameter("year");
	    String month = request.getParameter("month");

	    if (StringUtils.isEmpty(year) || StringUtils.isEmpty(month)) {
		ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed();
		if (lastClosedMonth != null) {
		    yearMonth = new YearMonth(lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), lastClosedMonth
			    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear()));
		    yearMonth.addMonth();
		} else {
		    yearMonth = new YearMonth(new YearMonthDay());
		}
	    } else {
		yearMonth.setYear(new Integer(year));
		yearMonth.setMonth(Month.valueOf(month));
	    }
	}
	if (yearMonth.getYear() < 2006) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidPastDateNoData"));
	    saveMessages(request, actionMessages);
	    return null;
	}
	return yearMonth;
    }
}
