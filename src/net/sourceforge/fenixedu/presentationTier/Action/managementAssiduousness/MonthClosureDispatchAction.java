package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthDocument;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;
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

    public ActionForward prepareToCloseMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	YearMonth yearMonthToExport = getYearMonthToClose(false, request);
	YearMonth yearMonth = new YearMonth(2007, 01);
	if (yearMonthToExport != null) {
	    request.setAttribute("yearMonthToExport", yearMonthToExport);
	    yearMonth = new YearMonth(yearMonthToExport.getYear(), yearMonthToExport.getNumberOfMonth());
	    yearMonth.addMonth();
	}
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("close-month");
    }

    private YearMonth getYearMonthToClose(boolean extraWork, HttpServletRequest request) {
	ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed(extraWork);
	List<ClosedMonthDocument> closedMonthDocuments = getClosedMonthDocuments(extraWork, lastClosedMonth);
	request.setAttribute("closedMonthDocuments", closedMonthDocuments);
	if (lastClosedMonth == null) {
	    return null;
	}
	return new YearMonth(lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), lastClosedMonth
		.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()));
    }

    private List<ClosedMonthDocument> getClosedMonthDocuments(boolean extraWork, ClosedMonth lastClosedMonth) {
	List<ClosedMonthDocument> closedMonthDocuments = null;
	if (lastClosedMonth != null) {
	    if (extraWork) {
		closedMonthDocuments = lastClosedMonth.getClosedMonthDocumentsByType(ClosedMonthDocumentType.MOVEMENTS);
	    } else {
		closedMonthDocuments = lastClosedMonth.getClosedMonthDocumentsByType(ClosedMonthDocumentType.WORK_ABSENCES);
	    }
	}
	return closedMonthDocuments;
    }

    public ActionForward closeMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	RenderUtils.invalidateViewState();
	if (yearMonth != null && yearMonth.getCanCloseMonth()) {
	    final YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new YearMonthDay().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
		endDay = new YearMonthDay().getDayOfMonth();
	    }
	    final YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);
	    final IUserView userView = SessionUtils.getUserView(request);
	    List<AssiduousnessClosedMonth> negativeAssiduousnessClosedMonths = (List<AssiduousnessClosedMonth>) ServiceUtils
		    .executeService(userView, "CloseAssiduousnessMonth", new Object[] { beginDate, endDate });
	    request.setAttribute("negativeAssiduousnessClosedMonths", negativeAssiduousnessClosedMonths);
	}

	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward exportClosedMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExport");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    final YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new YearMonthDay().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
		endDay = new YearMonthDay().getDayOfMonth();
	    }
	    final YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);
	    final IUserView userView = SessionUtils.getUserView(request);
	    final ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	    final String result = (String) ServiceUtils.executeService(userView, "ExportClosedMonth", new Object[] { closedMonth,
		    beginDate, endDate });
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

    public ActionForward exportClosedMonthList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
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

	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
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
	    double thisMonthUnjustified = assiduousnessClosedMonth.getAccumulatedUnjustified() - previousUnjustified
		    + assiduousnessClosedMonth.getUnjustifiedDays();

	    YearMonthDay beginDate = new YearMonthDay(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), 01);
	    YearMonthDay endDate = new YearMonthDay(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), beginDate.dayOfMonth().getMaximumValue());
	    double a66NextYearDays = getA66NextYearDays(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
		    .getAssiduousness(), beginDate, endDate);
	    int vacationsDays = getVacationsDays(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness(),
		    beginDate, endDate);
	    int medicalIssuesDays = getMedicalIssuesDays(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
		    .getAssiduousness(), beginDate, endDate);
	    int familyDeathDays = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		    .getLeavesNumberOfWorkDays(beginDate, endDate, "NOJO");
	    int weddingDays = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		    .getLeavesNumberOfWorkDays(beginDate, endDate, "LPC");
	    int paternityDays = getPaternityDays(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness(),
		    beginDate, endDate);
	    int leaveWithoutPayDays = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		    .getLeavesNumberOfWorkDays(beginDate, endDate, "LS/V");
	    fillInRow(spreadsheet, assiduousnessClosedMonth, thisMonthA66, thisMonthA66 + previousNotCompleteA66,
		    thisMonthUnjustified, thisMonthUnjustified + previousNotCompleteUnjustified, a66NextYearDays, vacationsDays,
		    medicalIssuesDays, familyDeathDays, weddingDays, paternityDays, leaveWithoutPayDays);
	}
	return spreadsheet;
    }

    private double getA66NextYearDays(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate) {
	double countWorkDays = assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "A 66 P.A.");
	for (Leave leave : assiduousness.getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("1/2 A 66 P.A.")) {
		countWorkDays += 0.5;
	    }
	}
	return countWorkDays;
    }

    private int getPaternityDays(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate) {
	int paternityDays = assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "L.Pat");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "LP");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "LMAR");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "LP25%");
	paternityDays += assiduousness.getLeavesNumberOfWorkDays(beginDate, endDate, "PATERNID");
	return paternityDays;
    }

    private int getMedicalIssuesDays(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate) {
	int countWorkDays = 0;
	for (Leave leave : assiduousness.getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getJustificationGroup() == JustificationGroup.SICKNESS
		    && leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) {
		countWorkDays += leave.getWorkDaysBetween(new Interval(beginDate.toDateTimeAtMidnight(), endDate
			.toDateTimeAtMidnight()));
	    }
	}
	return countWorkDays;
    }

    private int getVacationsDays(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate) {
	int countWorkDays = 0;
	for (Leave leave : assiduousness.getLeaves(beginDate, endDate)) {
	    if ((leave.getJustificationMotive().getJustificationGroup() == JustificationGroup.CURRENT_YEAR_HOLIDAYS
		    || leave.getJustificationMotive().getJustificationGroup() == JustificationGroup.LAST_YEAR_HOLIDAYS || leave
		    .getJustificationMotive().getJustificationGroup() == JustificationGroup.NEXT_YEAR_HOLIDAYS)
		    && leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) {
		countWorkDays += leave.getWorkDaysBetween(new Interval(beginDate.toDateTimeAtMidnight(), endDate
			.toDateTimeAtMidnight()));
	    }
	}
	return countWorkDays;
    }

    private void fillInRow(StyledExcelSpreadsheet spreadsheet, AssiduousnessClosedMonth assiduousnessClosedMonth, double a66,
	    double totalA66, double unjustified, double totalUnjustified, double a66NextYearDays, int vacationDays,
	    int medicalIssuesDays, int familyDeathDays, int weddingDays, int paternityDays, int leaveWithoutPayDays) {
	spreadsheet.newRow();
	DecimalFormat decimalFormat = new DecimalFormat(".0");
	String a66String = a66 == 0 ? "" : decimalFormat.format(a66);
	String totalA66String = (int) totalA66 == 0 ? "" : new Integer((int) totalA66).toString();
	String unjustifiedString = unjustified == 0 ? "" : decimalFormat.format(unjustified);
	String totalUnjustifiedString = (int) totalUnjustified == 0 ? "" : new Integer((int) totalUnjustified).toString();

	spreadsheet.addCell(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		.getEmployeeNumber().toString());
	Duration balanceToShow = assiduousnessClosedMonth.getBalance();
	if (assiduousnessClosedMonth.getBalance().isShorterThan(Duration.ZERO)) {
	    balanceToShow = balanceToShow.plus(assiduousnessClosedMonth.getBalanceToDiscount());
	}
	spreadsheet.addCell(((Integer) balanceToShow.toPeriod(PeriodType.minutes()).get(DurationFieldType.minutes())).toString());
	spreadsheet.addCell(unjustifiedString + "  " + totalUnjustifiedString);
	spreadsheet.addCell(a66String + "  " + totalA66String);
	spreadsheet.addCell(a66NextYearDays == 0 ? "" : decimalFormat.format(a66NextYearDays));
	spreadsheet.addCell(getNumberToCell(vacationDays));
	spreadsheet.addCell(getNumberToCell(medicalIssuesDays));
	spreadsheet.addCell(getNumberToCell(familyDeathDays));
	spreadsheet.addCell(getNumberToCell(weddingDays));
	spreadsheet.addCell(getNumberToCell(paternityDays));
	spreadsheet.addCell(getNumberToCell(leaveWithoutPayDays));
    }

    private String getNumberToCell(int value) {
	return value == 0 ? "-" : ((Integer) value).toString();
    }

    public ActionForward prepareToCloseExtraWorkMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	YearMonth yearMonthToExport = getYearMonthToClose(true, request);
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

    public ActionForward closeExtraWorkMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	final ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	final IUserView userView = SessionUtils.getUserView(request);
	if (yearMonth != null && !ClosedMonth.isMonthClosedForExtraWork(yearMonth.getPartial())) {
	    ServiceUtils.executeService(userView, "CloseExtraWorkMonth", new Object[] { closedMonth });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }

    public ActionForward openMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToOpen");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "OpenClosedMonth", new Object[] { ClosedMonth
		    .getClosedMonth(yearMonth) });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward openExtraWorkMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToOpen");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosedForExtraWork()) {
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "OpenExtraWorkClosedMonth", new Object[] { ClosedMonth
		    .getClosedMonth(yearMonth) });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }

    public ActionForward updateExtraWorkAmounts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToUpdate");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosedForExtraWork()) {
	    ActionMessage result = (ActionMessage) ServiceUtils.executeService(SessionUtils.getUserView(request),
		    "UpdateExtraWorkClosedMonth", new Object[] { ClosedMonth.getClosedMonth(yearMonth) });
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

    public ActionForward verifyExportClosedaMonthMovements(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed(Boolean.TRUE);
	if (closedMonth.getClosedMonthDocumentsByType(ClosedMonthDocumentType.MOVEMENTS).isEmpty()) {
	    return exportClosedMonthMovementsToGIAF(mapping, actionForm, request, response);
	} else {
	    request.setAttribute("yearMonthToExport", getYearMonthToClose(Boolean.TRUE, request));
	    return mapping.findForward("export-movements-warning");
	}
    }

    public ActionForward exportClosedMonthMovementsToGIAF(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return exportClosedMonthData(mapping, actionForm, request, response, ClosedMonthDocumentType.MOVEMENTS);
    }

    public ActionForward verifyExportClosedaMonthWorkAbsences(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed(Boolean.FALSE);
	if (closedMonth.getClosedMonthDocumentsByType(ClosedMonthDocumentType.WORK_ABSENCES).isEmpty()) {
	    return exportClosedMonthWorkAbsencesToGIAF(mapping, actionForm, request, response);
	} else {
	    request.setAttribute("yearMonthToExport", getYearMonthToClose(Boolean.FALSE, request));
	    return mapping.findForward("export-work-absences-warning");
	}
    }

    public ActionForward exportClosedMonthWorkAbsencesToGIAF(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return exportClosedMonthData(mapping, actionForm, request, response, ClosedMonthDocumentType.WORK_ABSENCES);
    }

    private ActionForward exportClosedMonthData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, ClosedMonthDocumentType closedMonthDocumentType) throws FenixServiceException,
	    FenixFilterException, IOException {

	ActionForward actionForward = null;
	if (closedMonthDocumentType == ClosedMonthDocumentType.WORK_ABSENCES) {
	    actionForward = prepareToCloseMonth(mapping, actionForm, request, response);
	} else {
	    actionForward = prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
	}

	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExport");
	RenderUtils.invalidateViewState();
	if (yearMonth == null) {
	    return actionForward;
	}
	request.setAttribute("yearMonthToExport", yearMonth);

	final IUserView userView = SessionUtils.getUserView(request);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	String result = null;
	try {
	    result = (String) ServiceUtils.executeService(userView, "ExportClosedExtraWorkMonth", new Object[] { closedMonth,
		    closedMonthDocumentType == ClosedMonthDocumentType.WORK_ABSENCES,
		    closedMonthDocumentType == ClosedMonthDocumentType.MOVEMENTS });
	} catch (InvalidGiafCodeException e) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", new ActionMessage(e.getMessage(), e.getArgs()));
	    saveMessages(request, actionMessages);
	    return actionForward;
	}

	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	String fileName = new StringBuilder().append(bundleEnumeration.getString(closedMonthDocumentType.name())).append("-")
		.append(month).append("-").append(yearMonth.getYear()).append(".txt").toString();

	ActionMessage error = (ActionMessage) ServiceUtils.executeService(userView, "ExportToGIAFAndSaveFile", new Object[] {
		closedMonth, fileName, closedMonthDocumentType, result });
	if (error != null) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", error);
	    saveMessages(request, actionMessages);
	    return actionForward;
	}
	RenderUtils.invalidateViewState();
	if (closedMonthDocumentType == ClosedMonthDocumentType.WORK_ABSENCES) {
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	} else {
	    return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
	}
    }

    public ActionForward exportClosedMonthFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToOpen2");
	RenderUtils.invalidateViewState();
	if (yearMonth == null) {
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	request.setAttribute("yearMonthToExport", yearMonth);

	final IUserView userView = SessionUtils.getUserView(request);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	String result = null;
	try {
	    result = (String) ServiceUtils.executeService(userView, "ExportClosedExtraWorkMonth", new Object[] { closedMonth,
		    true, true });
	} catch (InvalidGiafCodeException e) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", new ActionMessage(e.getMessage(), e.getArgs()));
	    saveMessages(request, actionMessages);
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	response.setContentType("text/plain");
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	response.addHeader("Content-Disposition", new StringBuilder("attachment; filename=").append(month).append("-").append(
		yearMonth.getYear()).toString()
		+ ".txt");

	byte[] data = result.getBytes();
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();

	return null;

    }

    public ActionForward exportExtraWorkClosedMonthFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExport2");
	RenderUtils.invalidateViewState();
	if (yearMonth == null) {
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	request.setAttribute("yearMonthToExport", yearMonth);

	final IUserView userView = SessionUtils.getUserView(request);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	String result = null;
	try {
	    result = (String) ServiceUtils.executeService(userView, "ExportClosedExtraWorkMonth", new Object[] { closedMonth,
		    false, true });
	} catch (InvalidGiafCodeException e) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", new ActionMessage(e.getMessage(), e.getArgs()));
	    saveMessages(request, actionMessages);
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	response.setContentType("text/plain");
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	response.addHeader("Content-Disposition", new StringBuilder("attachment; filename=").append(month).append("-").append(
		yearMonth.getYear()).toString()
		+ ".txt");

	byte[] data = result.getBytes();
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();

	return null;

    }

}
