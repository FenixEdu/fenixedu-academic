package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class MonthClosureDispatchAction extends FenixDispatchAction {

    public ActionForward prepareToCloseMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	YearMonth yearMonthToExport = getYearMonthToClose(false);
	YearMonth yearMonth = new YearMonth(2007, 01);
	if (yearMonthToExport != null) {
	    request.setAttribute("yearMonthToExport", yearMonthToExport);
	    yearMonth = new YearMonth(yearMonthToExport.getYear(), yearMonthToExport.getNumberOfMonth());
	    yearMonth.addMonth();
	}
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("close-month");
    }

    private YearMonth getYearMonthToClose(boolean extraWork) {
	ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed(extraWork);
	if (lastClosedMonth == null) {
	    return null;
	}
	return new YearMonth(lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.year()),
		lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()));
    }

    public ActionForward closeMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth != null && yearMonth.getCanCloseMonth()) {
	    final YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new YearMonthDay().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
		endDay = new YearMonthDay().getDayOfMonth();
	    }
	    final YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, endDay);
	    final IUserView userView = SessionUtils.getUserView(request);
	    ServiceUtils.executeService(userView, "CloseAssiduousnessMonth", new Object[] { beginDate,
		    endDate });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward exportClosedMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExport");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    final YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new YearMonthDay().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
		endDay = new YearMonthDay().getDayOfMonth();
	    }
	    final YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, endDay);
	    final IUserView userView = SessionUtils.getUserView(request);
	    final ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	    final String result = (String) ServiceUtils.executeService(userView, "ExportClosedMonth",
		    new Object[] { closedMonth, beginDate, endDate });
	    response.setContentType("text/plain");
	    response.addHeader("Content-Disposition", "attachment; filename=Telep.dat");
	    byte[] data = result.getBytes();
	    response.setContentLength(data.length);
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	    return mapping.findForward("");
	}
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward exportClosedMonthList(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExportList");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    final ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	    StyledExcelSpreadsheet spreadsheet = getSpreadSheet(closedMonth);

	    response.setContentType("text/plain");
	    response.addHeader("Content-Disposition", "attachment; filename=listagem_mes.xls");
	    final ServletOutputStream writer = response.getOutputStream();
	    spreadsheet.getWorkbook().write(writer);
	    writer.flush();
	    response.flushBuffer();
	    return mapping.findForward("");
	}
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    private StyledExcelSpreadsheet getSpreadSheet(ClosedMonth closedMonth) {
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Listagem Fecho Mês");
        spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Nº Mec");
	spreadsheet.addHeader("Saldo");
	spreadsheet.addHeader("Inj/V");
	spreadsheet.addHeader("A66/V");
	spreadsheet.addHeader("A66 Prox");
	spreadsheet.addHeader("FER.");
	spreadsheet.addHeader("ATES");
	spreadsheet.addHeader("NOJO");
	spreadsheet.addHeader("CASA");
	spreadsheet.addHeader("PART");
	spreadsheet.addHeader("LS/V");
	spreadsheet.getSheet().setMargin(HSSFSheet.TopMargin, 0.5);
	spreadsheet.getSheet().setMargin(HSSFSheet.BottomMargin, 0.3);
	spreadsheet.getSheet().setGridsPrinted(true);

	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth
		.getAssiduousnessClosedMonths()) {
	    AssiduousnessClosedMonth previousAssiduousnessClosedMonth = assiduousnessClosedMonth
		    .getPreviousAssiduousnessClosedMonth();
	    double previousA66 = 0;
	    double previousUnjustified = 0;
	    double previousNotCompleteA66 = 0;
	    double previousNotCompleteUnjustified = 0;
	    if (previousAssiduousnessClosedMonth != null) {
		previousA66 = previousAssiduousnessClosedMonth.getAccumulatedArticle66();
		previousNotCompleteA66 = previousA66 - (int) previousA66;
		previousUnjustified = previousAssiduousnessClosedMonth.getAccumulatedUnjustified();
		previousNotCompleteUnjustified = previousUnjustified - (int) previousUnjustified;
	    }
	    double thisMonthA66 = assiduousnessClosedMonth.getAccumulatedArticle66() - previousA66
		    + assiduousnessClosedMonth.getArticle66();
	    double thisMonthUnjustified = assiduousnessClosedMonth.getAccumulatedUnjustified()
		    - previousUnjustified + assiduousnessClosedMonth.getUnjustifiedDays();

	    YearMonthDay beginDate = new YearMonthDay(closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year()), closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.monthOfYear()), 01);
	    YearMonthDay endDate = new YearMonthDay(closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year()), closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.monthOfYear()), beginDate.dayOfMonth().getMaximumValue());
	    double a66NextYearDays = getA66NextYearDays(assiduousnessClosedMonth.getAssiduousness(),
		    beginDate, endDate);
	    int vacationsDays = getVacationsDays(assiduousnessClosedMonth.getAssiduousness(), beginDate,
		    endDate);
	    int medicalIssuesDays = getMedicalIssuesDays(assiduousnessClosedMonth.getAssiduousness(),
		    beginDate, endDate);
	    int familyDeathDays = assiduousnessClosedMonth.getAssiduousness().getLeavesNumberOfWorkDays(
		    beginDate, endDate, "NOJO");
	    int weddingDays = assiduousnessClosedMonth.getAssiduousness().getLeavesNumberOfWorkDays(
		    beginDate, endDate, "LPC");
	    int paternityDays = getPaternityDays(assiduousnessClosedMonth.getAssiduousness(), beginDate,
		    endDate);
	    int leaveWithoutPayDays = assiduousnessClosedMonth.getAssiduousness()
		    .getLeavesNumberOfWorkDays(beginDate, endDate, "LS/V");
	    fillInRow(spreadsheet, assiduousnessClosedMonth, thisMonthA66, thisMonthA66
		    + previousNotCompleteA66, thisMonthUnjustified, thisMonthUnjustified
		    + previousNotCompleteUnjustified, a66NextYearDays, vacationsDays, medicalIssuesDays,
		    familyDeathDays, weddingDays, paternityDays, leaveWithoutPayDays);
	}
	return spreadsheet;
    }

    private double getA66NextYearDays(Assiduousness assiduousness, YearMonthDay beginDate,
	    YearMonthDay endDate) {
	double countWorkDays = assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "A 66 P.A.");
	for (Leave leave : assiduousness.getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("1/2 A 66 P.A.")) {
		countWorkDays += 0.5;
	    }
	}
	return countWorkDays;
    }

    private int getPaternityDays(Assiduousness assiduousness, YearMonthDay beginDate,
	    YearMonthDay endDate) {
	int paternityDays = assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "L.Pat");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "LP");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "LMAR");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "LP25%");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "PATERNID");
	return paternityDays;
    }

    private int getMedicalIssuesDays(Assiduousness assiduousness, YearMonthDay beginDate,
	    YearMonthDay endDate) {
	int countWorkDays = 0;
	for (Leave leave : assiduousness.getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getJustificationGroup() == JustificationGroup.SICKNESS
		    && leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) {
		countWorkDays += leave.getWorkDaysBetween(new Interval(beginDate.toDateTimeAtMidnight(),
			endDate.toDateTimeAtMidnight()));
	    }
	}
	return countWorkDays;
    }

    private int getVacationsDays(Assiduousness assiduousness, YearMonthDay beginDate,
	    YearMonthDay endDate) {
	int countWorkDays = 0;
	for (Leave leave : assiduousness.getLeaves(beginDate, endDate)) {
	    if ((leave.getJustificationMotive().getJustificationGroup() == JustificationGroup.CURRENT_YEAR_HOLIDAYS
		    || leave.getJustificationMotive().getJustificationGroup() == JustificationGroup.LAST_YEAR_HOLIDAYS || leave
		    .getJustificationMotive().getJustificationGroup() == JustificationGroup.NEXT_YEAR_HOLIDAYS)
		    && leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) {
		countWorkDays += leave.getWorkDaysBetween(new Interval(beginDate.toDateTimeAtMidnight(),
			endDate.toDateTimeAtMidnight()));
	    }
	}
	return countWorkDays;
    }

    private void fillInRow(StyledExcelSpreadsheet spreadsheet,
	    AssiduousnessClosedMonth assiduousnessClosedMonth, double a66, double totalA66,
	    double unjustified, double totalUnjustified, double a66NextYearDays, int vacationDays,
	    int medicalIssuesDays, int familyDeathDays, int weddingDays, int paternityDays,
	    int leaveWithoutPayDays) {
	spreadsheet.newRow();
	DecimalFormat decimalFormat = new DecimalFormat(".0");
	String a66String = a66 == 0 ? "" : decimalFormat.format(a66);
	String totalA66String = (int) totalA66 == 0 ? "" : new Integer((int) totalA66).toString();
	String unjustifiedString = unjustified == 0 ? "" : decimalFormat.format(unjustified);
	String totalUnjustifiedString = (int) totalUnjustified == 0 ? "" : new Integer(
		(int) totalUnjustified).toString();

	spreadsheet.addCell(assiduousnessClosedMonth.getAssiduousness().getEmployee()
		.getEmployeeNumber().toString());
	Duration balanceToShow = assiduousnessClosedMonth.getBalance();
	if (assiduousnessClosedMonth.getBalance().isShorterThan(Duration.ZERO)) {
	    balanceToShow = balanceToShow.plus(assiduousnessClosedMonth.getBalanceToDiscount());
	}
	spreadsheet.addCell(((Integer) balanceToShow.toPeriod(PeriodType.minutes()).get(
		DurationFieldType.minutes())).toString());
	spreadsheet.addCell(unjustifiedString + "  " + totalUnjustifiedString);
	spreadsheet.addCell(a66String + "  " + totalA66String);
	spreadsheet.addCell(a66NextYearDays == 0 ? "" : decimalFormat.format(a66NextYearDays));
	spreadsheet.addCell(((Integer) vacationDays).toString());
	spreadsheet.addCell(((Integer) medicalIssuesDays).toString());
	spreadsheet.addCell(((Integer) familyDeathDays).toString());
	spreadsheet.addCell(((Integer) weddingDays).toString());
	spreadsheet.addCell(((Integer) paternityDays).toString());
	spreadsheet.addCell(((Integer) leaveWithoutPayDays).toString());
    }

    public ActionForward prepareToCloseExtraWorkMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	YearMonth yearMonthToExport = getYearMonthToClose(true);
	YearMonth yearMonth = new YearMonth(2007, 01);
	if (yearMonthToExport != null) {
	    request.setAttribute("yearMonthToExport", yearMonthToExport);
	    yearMonth = new YearMonth(yearMonthToExport.getYear(), yearMonthToExport.getNumberOfMonth());
	    yearMonth.addMonth();
	}
	if (ClosedMonth.isMonthClosed(yearMonth.getPartial())) {
	    request.setAttribute("yearMonth", yearMonth);
	}
	return mapping.findForward("close-extra-work-month");
    }

    public ActionForward closeExtraWorkMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	final ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	final IUserView userView = SessionUtils.getUserView(request);
	if (yearMonth != null && !ClosedMonth.isMonthClosedForExtraWork(yearMonth.getPartial())) {
	    ServiceUtils.executeService(userView, "CloseExtraWorkMonth", new Object[] { closedMonth });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }

    public ActionForward exportExtraWorkMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExport");
	if (yearMonth == null) {
	    return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
	}

	final IUserView userView = SessionUtils.getUserView(request);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	String result = null;
	try {
	    result = (String) ServiceUtils.executeService(userView, "ExportClosedExtraWorkMonth",
		    new Object[] { closedMonth });
	} catch (InvalidGiafCodeException e) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", new ActionMessage(e.getMessage(), e.getArgs()));
	    saveMessages(request, actionMessages);
	    return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
	}
	response.setContentType("text/plain");
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	response.addHeader("Content-Disposition", new StringBuilder("attachment; filename=").append(
		month).append("-").append(yearMonth.getYear()).toString()
		+ ".txt");

	byte[] data = result.getBytes();
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();

	return mapping.findForward("");
    }

    public ActionForward openMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToOpen");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "OpenClosedMonth",
		    new Object[] { ClosedMonth.getClosedMonth(yearMonth) });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward openExtraWorkMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToOpen");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosedForExtraWork()) {
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "OpenExtraWorkClosedMonth",
		    new Object[] { ClosedMonth.getClosedMonth(yearMonth) });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }

    public ActionForward updateExtraWorkAmounts(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToUpdate");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosedForExtraWork()) {
	    ActionMessage result = (ActionMessage) ServiceUtils.executeService(SessionUtils
		    .getUserView(request), "UpdateExtraWorkClosedMonth", new Object[] { ClosedMonth
		    .getClosedMonth(yearMonth) });
	    if (result == null) {
		result = new ActionMessage("message.successUpdatingExtraWork");
	    }
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("sucess", result);
	    saveMessages(request, actionMessages);
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }
}
