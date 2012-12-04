package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ExportClosedExtraWorkMonth;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ExportClosedMonth;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ExportToGIAFAndSaveFile;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedDay;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecordMonthIndex;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthDocument;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(module = "personnelSection", path = "/monthClosure", attribute = "parametrizationForm", formBean = "parametrizationForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "close-month", path = "/managementAssiduousness/closeMonth.jsp", tileProperties = @Tile(title = "private.staffarea.closureofthemonth.closemonthandexport")),
	@Forward(name = "export-movements-warning", path = "/managementAssiduousness/exportMovementsWarning.jsp"),
	@Forward(name = "close-extra-work-month", path = "/managementAssiduousness/closeExtraWorkMonth.jsp", tileProperties = @Tile(title = "private.staffarea.outstandingjob.closeworkingovertime")),
	@Forward(name = "export-work-absences-warning", path = "/managementAssiduousness/exportWorkAbsencesWarning.jsp") })
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
	YearMonth yearMonth = getRenderedObject("yearMonth");
	RenderUtils.invalidateViewState();
	if (yearMonth != null && yearMonth.getCanCloseMonth()) {
	    final LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    ClosedMonth closedMonth = ClosedMonth.getOrCreateOpenClosedMonth(beginDate);
	    closedMonth.setIntensionToCloseForBalance();
	}
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward exportClosedMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = getRenderedObject("yearMonthToExport");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    final LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new LocalDate().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new LocalDate().getMonthOfYear()) {
		endDay = new LocalDate().getDayOfMonth();
	    }
	    final LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);
	    final ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	    final String result = ExportClosedMonth.run(closedMonth, beginDate, endDate);
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
	YearMonth yearMonth = getRenderedObject("yearMonthToExportList");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    final ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	    StyledExcelSpreadsheet spreadsheet = getSpreadSheet(closedMonth);
	    spreadsheet.getWorkbook().setRepeatingRowsAndColumns(0, 0, 0, 0, 1);
	    spreadsheet.getSheet().getPrintSetup().setLandscape(true);
	    spreadsheet.getSheet().getPrintSetup().setLeftToRight(true);
	    response.setContentType("text/plain");
	    ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	    String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	    response.addHeader("Content-Disposition", new StringBuilder("attachment; filename=listagem_mes_").append(month)
		    .append("-").append(yearMonth.getYear()).toString()
		    + ".xls");

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
	spreadsheet.addHeader("Nome", 10000);
	int firstColumnNumber = 2, columnNumber = 2;
	Map<JustificationMotive, Integer> justifications = new HashMap<JustificationMotive, Integer>();
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	spreadsheet.newHeaderRow();
	Set<AssiduousnessRecord> assiduousnessRecordBetweenDates = AssiduousnessRecordMonthIndex
		.getAssiduousnessRecordBetweenDates(closedMonth.getClosedMonthFirstDay().toDateTimeAtStartOfDay(), closedMonth
			.getClosedMonthLastDay().plusDays(1).toDateTimeAtStartOfDay());

	List<JustificationType> justificationTypes = JustificationType.getDayJustificationTypes();
	justificationTypes.addAll(JustificationType.getHalfDayJustificationTypes());
	for (JustificationGroup justificationGroup : JustificationGroup.values()) {
	    List<JustificationMotive> justificationMotivesByGroup = JustificationMotive
		    .getJustificationMotivesByGroup(justificationGroup);
	    if (justificationMotivesByGroup.size() != 0) {

		for (JustificationMotive justificationMotive : justificationMotivesByGroup) {
		    if (justificationTypes.contains(justificationMotive.getJustificationType())
			    && getIsJustificationUsed(assiduousnessRecordBetweenDates, justificationMotive)) {
			justifications.put(justificationMotive, columnNumber);
			spreadsheet.addHeader(1, columnNumber++, justificationMotive.getAcronym());
		    }
		}
		if (firstColumnNumber < columnNumber) {
		    spreadsheet.addHeader(0, firstColumnNumber, bundleEnumeration.getString(justificationGroup.name()));
		    spreadsheet.mergeCells(0, 0, firstColumnNumber, columnNumber - 1);
		    firstColumnNumber = columnNumber;

		}
	    }
	}
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getJustificationGroup() == null
		    && justificationTypes.contains(justificationMotive.getJustificationType())
		    && getIsJustificationUsed(assiduousnessRecordBetweenDates, justificationMotive)) {
		justifications.put(justificationMotive, columnNumber);
		spreadsheet.addHeader(1, columnNumber++, justificationMotive.getAcronym());
	    }
	}
	spreadsheet.addHeader("Saldo");
	spreadsheet.addHeader("Saldo Injustificado");
	spreadsheet.addHeader("Faltas Injustificadas em dias completos");
	spreadsheet.addHeader("Faltas Injustificadas por tempo em falta");
	spreadsheet.addHeader("Faltas por conta do periodo de férias");

	spreadsheet.mergeCells(0, 1, 0, 0);
	spreadsheet.mergeCells(0, 1, 1, 1);

	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
	    spreadsheet.newRow();
	    spreadsheet.addCell(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getEmployeeNumber());
	    spreadsheet.addCell(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getPerson().getName());
	    for (JustificationMotive justificationMotive : justifications.keySet()) {
		double leavesDaysByJustificationMotive = getLeavesDaysByJustificationMotive(justificationMotive,
			assiduousnessClosedMonth);
		spreadsheet.addCell(leavesDaysByJustificationMotive, justifications.get(justificationMotive));
	    }
	    spreadsheet.addDuration(assiduousnessClosedMonth.getBalance());
	    spreadsheet.addDuration(assiduousnessClosedMonth.getTotalUnjustifiedBalance());
	    spreadsheet.addCell(assiduousnessClosedMonth.getUnjustifiedDays());
	    spreadsheet.addCell(assiduousnessClosedMonth.getAccumulatedUnjustifiedDays());
	    spreadsheet.addCell(assiduousnessClosedMonth.getAccumulatedArticle66Days());

	}
	spreadsheet.setRegionBorder(0, spreadsheet.getRow().getRowNum() + 1, 0, spreadsheet.getMaxiumColumnNumber() - 1);

	spreadsheet.getSheet("Listagem Fecho Mês Agrupada");
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Nº Mec");
	spreadsheet.addHeader("Nome", 10000);
	columnNumber = 2;
	Map<String, Integer> justificationsByGroups = new HashMap<String, Integer>();
	for (JustificationGroup justificationGroup : JustificationGroup.values()) {
	    List<JustificationMotive> justificationMotivesByGroup = JustificationMotive
		    .getJustificationMotivesByGroup(justificationGroup);
	    if (justificationMotivesByGroup.size() != 0) {
		justificationsByGroups.put(justificationGroup.name(), columnNumber++);
		spreadsheet.addHeader(bundleEnumeration.getString(justificationGroup.name()), spreadsheet.getExcelStyle()
			.getVerticalHeaderStyle(), 1250);
	    }
	}
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getJustificationGroup() == null
		    && justificationTypes.contains(justificationMotive.getJustificationType())
		    && getIsJustificationUsed(assiduousnessRecordBetweenDates, justificationMotive)) {
		justificationsByGroups.put(justificationMotive.getAcronym(), columnNumber++);
		spreadsheet.addHeader(justificationMotive.getAcronym(), spreadsheet.getExcelStyle().getVerticalHeaderStyle(),
			1250);
	    }
	}

	spreadsheet.addHeader("Saldo");
	spreadsheet.addHeader("Saldo Injustificado");
	spreadsheet.addHeader("Faltas Injustificadas em dias completos");
	spreadsheet.addHeader("Faltas Injustificadas por tempo em falta");
	spreadsheet.addHeader("Faltas por conta do periodo de férias");

	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
	    Map<JustificationGroup, Double> justificationsValues = new HashMap<JustificationGroup, Double>();
	    spreadsheet.newRow();
	    spreadsheet.addCell(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getEmployeeNumber());
	    spreadsheet.addCell(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getPerson().getName());
	    for (JustificationMotive justificationMotive : justifications.keySet()) {
		double leavesDaysByJustificationMotive = getLeavesDaysByJustificationMotive(justificationMotive,
			assiduousnessClosedMonth);
		if (justificationMotive.getJustificationGroup() == null) {
		    spreadsheet.addCell(leavesDaysByJustificationMotive,
			    justificationsByGroups.get(justificationMotive.getAcronym()));
		} else {
		    Double value = justificationsValues.get(justificationMotive.getJustificationGroup());
		    if (value == null) {
			value = 0.0;
		    }
		    justificationsValues
			    .put(justificationMotive.getJustificationGroup(), value + leavesDaysByJustificationMotive);
		}
	    }
	    for (JustificationGroup justificationGroup : justificationsValues.keySet()) {
		spreadsheet.addCell(justificationsByGroups.get(justificationGroup.name()));
	    }

	    spreadsheet.addDuration(assiduousnessClosedMonth.getBalance());
	    spreadsheet.addDuration(assiduousnessClosedMonth.getTotalUnjustifiedBalance());
	    spreadsheet.addCell(assiduousnessClosedMonth.getUnjustifiedDays());
	    spreadsheet.addCell(assiduousnessClosedMonth.getAccumulatedUnjustifiedDays());
	    spreadsheet.addCell(assiduousnessClosedMonth.getAccumulatedArticle66Days());
	}
	spreadsheet.setRegionBorder(0, spreadsheet.getRow().getRowNum() + 1, 0, spreadsheet.getMaxiumColumnNumber() - 1);

	return spreadsheet;
    }

    public boolean getIsJustificationUsed(Set<AssiduousnessRecord> assiduousnessRecordBetweenDates,
	    JustificationMotive justificationMotive) {
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecordBetweenDates) {
	    if (assiduousnessRecord instanceof Leave
		    && ((Leave) assiduousnessRecord).getJustificationMotive().equals(justificationMotive)) {
		return true;
	    }
	}
	return false;
    }

    private double getLeavesDaysByJustificationMotive(JustificationMotive justificationMotive,
	    AssiduousnessClosedMonth assiduousnessClosedMonth) {
	double countWorkDays = 0;
	LocalDate beginDate = assiduousnessClosedMonth.getBeginDate();
	LocalDate endDate = assiduousnessClosedMonth.getEndDate();
	for (Leave leave : assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		.getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().equals(justificationMotive)) {
		if (leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) {
		    countWorkDays += leave.getWorkDaysBetween(new Interval(beginDate.toDateTimeAtStartOfDay(), endDate
			    .toDateTimeAtStartOfDay()));
		} else if (leave.getJustificationMotive().getJustificationType() == JustificationType.HALF_OCCURRENCE
			|| leave.getJustificationMotive().getJustificationType() == JustificationType.HALF_OCCURRENCE_TIME) {
		    countWorkDays += 0.5;
		}
	    }
	}
	return countWorkDays;
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
	final YearMonth yearMonth = getRenderedObject("yearMonth");
	final ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	if (yearMonth != null && !ClosedMonth.isMonthClosedForExtraWork(yearMonth.getPartial())) {
	    ServiceUtils.executeService("CloseExtraWorkMonth", new Object[] { closedMonth });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }

    public ActionForward openMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = getRenderedObject("yearMonthToOpen");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    ServiceUtils.executeService("OpenClosedMonth", new Object[] { ClosedMonth.getClosedMonthForBalance(yearMonth) });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward openExtraWorkMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = getRenderedObject("yearMonthToOpen");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosedForExtraWork()) {
	    ServiceUtils.executeService("OpenExtraWorkClosedMonth",
		    new Object[] { ClosedMonth.getClosedMonthForBalance(yearMonth) });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseExtraWorkMonth(mapping, actionForm, request, response);
    }

    public ActionForward updateExtraWorkAmounts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = getRenderedObject("yearMonthToUpdate");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosedForExtraWork()) {
	    ActionMessage result = (ActionMessage) ServiceUtils.executeService("UpdateExtraWorkClosedMonth",
		    new Object[] { ClosedMonth.getClosedMonthForBalance(yearMonth) });
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

	YearMonth yearMonth = getRenderedObject("yearMonthToExport");
	RenderUtils.invalidateViewState();
	if (yearMonth == null) {
	    return actionForward;
	}
	request.setAttribute("yearMonthToExport", yearMonth);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	String result = null;
	try {
	    result = ExportClosedExtraWorkMonth.run(closedMonth,
		    closedMonthDocumentType == ClosedMonthDocumentType.WORK_ABSENCES,
		    closedMonthDocumentType == ClosedMonthDocumentType.MOVEMENTS);
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

	ActionMessage error = ExportToGIAFAndSaveFile.run(closedMonth, fileName, closedMonthDocumentType, result);
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

	YearMonth yearMonth = getRenderedObject("yearMonthToOpen2");
	RenderUtils.invalidateViewState();
	if (yearMonth == null) {
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	request.setAttribute("yearMonthToExport", yearMonth);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	String result = null;
	try {
	    result = ExportClosedExtraWorkMonth.run(closedMonth, true, true);
	} catch (InvalidGiafCodeException e) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", new ActionMessage(e.getMessage(), e.getArgs()));
	    saveMessages(request, actionMessages);
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	response.setContentType("text/plain");
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	response.addHeader("Content-Disposition",
		new StringBuilder("attachment; filename=").append(month).append("-").append(yearMonth.getYear()).toString()
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

	YearMonth yearMonth = getRenderedObject("yearMonthToExport2");
	RenderUtils.invalidateViewState();
	if (yearMonth == null) {
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	request.setAttribute("yearMonthToExport", yearMonth);
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	String result = null;
	try {
	    result = ExportClosedExtraWorkMonth.run(closedMonth, false, true);
	} catch (InvalidGiafCodeException e) {
	    ActionMessages actionMessages = getMessages(request);
	    actionMessages.add("message", new ActionMessage(e.getMessage(), e.getArgs()));
	    saveMessages(request, actionMessages);
	    return prepareToCloseMonth(mapping, actionForm, request, response);
	}
	response.setContentType("text/plain");
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	response.addHeader("Content-Disposition",
		new StringBuilder("attachment; filename=").append(month).append("-").append(yearMonth.getYear()).toString()
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

    public ActionForward showMonthCorrections(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	YearMonth yearMonth = getRenderedObject("yearMonth");
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Correções");
	ResourceBundle bundleAssiduousness = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundleAssiduousness.getString("label.number"));
	spreadsheet.addHeader(bundleAssiduousness.getString("label.employee.name"), 10000);
	spreadsheet.addHeader(bundleAssiduousness.getString("title.showStatus"), 10000);
	spreadsheet.addHeader(bundleAssiduousness.getString("label.year"));
	spreadsheet.addHeader(bundleAssiduousness.getString("label.month"));
	spreadsheet.addHeader(bundleAssiduousness.getString("label.correction"), 10000);
	spreadsheet.addHeader(bundleAssiduousness.getString("label.quantity"));
	if (closedMonth != null) {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonthsCorrections()) {
		AssiduousnessClosedMonth oldAssiduousnessClosedMonth = assiduousnessClosedMonth.getOldAssiduousnessClosedMonth();
		AssiduousnessStatusHistory assiduousnessStatusHistory = assiduousnessClosedMonth.getAssiduousnessStatusHistory();
		if (oldAssiduousnessClosedMonth.getAccumulatedArticle66Days() != assiduousnessClosedMonth
			.getAccumulatedArticle66Days()) {
		    fillRow(spreadsheet, assiduousnessStatusHistory, bundleAssiduousness.getString("label.accumulatedArticle66"),
			    oldAssiduousnessClosedMonth.getAccumulatedArticle66Days(),
			    assiduousnessClosedMonth.getAccumulatedArticle66Days(), assiduousnessClosedMonth.getClosedMonth()
				    .getClosedYearMonth());
		}
		if (oldAssiduousnessClosedMonth.getAccumulatedUnjustifiedDays() != assiduousnessClosedMonth
			.getAccumulatedUnjustifiedDays()) {
		    fillRow(spreadsheet, assiduousnessStatusHistory,
			    bundleAssiduousness.getString("label.accumulatedUnjustifiedDays"),
			    oldAssiduousnessClosedMonth.getAccumulatedUnjustifiedDays(),
			    assiduousnessClosedMonth.getAccumulatedUnjustifiedDays(), assiduousnessClosedMonth.getClosedMonth()
				    .getClosedYearMonth());
		}
		if (oldAssiduousnessClosedMonth.getUnjustifiedDays() != assiduousnessClosedMonth.getUnjustifiedDays()) {
		    fillRow(spreadsheet, assiduousnessStatusHistory, bundleAssiduousness.getString("label.unjustifiedDays"),
			    oldAssiduousnessClosedMonth.getUnjustifiedDays(), assiduousnessClosedMonth.getUnjustifiedDays(),
			    assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth());
		}

	    }
	    for (ClosedMonthJustification closedMonthJustification : closedMonth.getClosedMonthJustificationCorrections()) {
		ClosedMonthJustification oldclosedMonthJustification = closedMonthJustification.getOldClosedMonthJustification();
		HashMap<String, Duration> pastJustificationsDurations = closedMonthJustification.getAssiduousnessClosedMonth()
			.getPastJustificationsDurations();
		Integer oldValue = oldclosedMonthJustification == null ? 0 : oldclosedMonthJustification
			.getJustificationDays(pastJustificationsDurations);
		fillRow(spreadsheet, closedMonthJustification.getAssiduousnessClosedMonth().getAssiduousnessStatusHistory(),
			closedMonthJustification.getJustificationMotive().getAcronym(), oldValue,
			closedMonthJustification.getJustificationDays(pastJustificationsDurations), closedMonthJustification
				.getAssiduousnessClosedMonth().getClosedMonth().getClosedYearMonth());
	    }

	    spreadsheet.newRow();
	    spreadsheet.newRow();

	    spreadsheet.newHeaderRow();
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.number"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.employee.name"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("title.showStatus"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.date"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.current"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.previous"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.referenceDate")
		    + bundleAssiduousness.getString("label.current"));
	    spreadsheet.addHeader(bundleAssiduousness.getString("label.referenceDate")
		    + bundleAssiduousness.getString("label.previous"));

	    for (AssiduousnessClosedDay assiduousnessClosedDay : closedMonth.getAssiduousnessClosedDaysCorrections()) {
		fillLeaves(spreadsheet, assiduousnessClosedDay);
	    }

	}
	response.setContentType("text/plain");
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
	response.addHeader("Content-Disposition", new StringBuilder("attachment; filename=correccoes-").append(month).append("-")
		.append(yearMonth.getYear()).toString()
		+ ".xls");

	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return mapping.findForward("");
    }

    public void fillLeaves(StyledExcelSpreadsheet spreadsheet, AssiduousnessClosedDay assiduousnessClosedDay) {
	AssiduousnessStatusHistory assiduousnessStatusHistory = assiduousnessClosedDay.getAssiduousnessClosedMonth()
		.getAssiduousnessStatusHistory();
	final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex = assiduousnessStatusHistory.getAssiduousness()
		.getAssiduousnessRecordMonthIndex(assiduousnessClosedDay.getDay());
	if (assiduousnessRecordMonthIndex != null) {
	    Leave currentLeave = null;
	    Leave lastAnulatedLeave = null;
	    for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecordMonthIndex.getAssiduousnessRecordsSet()) {
		if (assiduousnessRecord.isLeave()
			&& ((Leave) assiduousnessRecord).occuredInDate(assiduousnessClosedDay.getDay())
			&& (!(((Leave) assiduousnessRecord).getJustificationMotive().getJustificationType()
				.equals(JustificationType.TIME) && ((Leave) assiduousnessRecord).getJustificationMotive()
				.getAccumulate()))
			&& (!(((Leave) assiduousnessRecord).getJustificationMotive().getJustificationType()
				.equals(JustificationType.BALANCE)))
			&& (!(((Leave) assiduousnessRecord).getJustificationMotive().getJustificationType()
				.equals(JustificationType.MULTIPLE_MONTH_BALANCE)))
			&& (!(((Leave) assiduousnessRecord).getJustificationMotive().getJustificationType()
				.equals(JustificationType.ANULATION)))
			&& (!(((Leave) assiduousnessRecord).getJustificationMotive().getJustificationType()
				.equals(JustificationType.HALF_MULTIPLE_MONTH_BALANCE)))) {

		    Leave leave = (Leave) assiduousnessRecord;
		    if (!assiduousnessRecord.isAnulated()) {
			currentLeave = leave;
		    } else {
			if (leave.getIsCorrection()
				&& (lastAnulatedLeave == null || (leave.getLastModifiedDate().isAfter(lastAnulatedLeave
					.getLastModifiedDate())))) {
			    lastAnulatedLeave = leave;
			}
		    }
		}
	    }
	    if (lastAnulatedLeave != null) {
		if (currentLeave != null) {
		    if (currentLeave.getJustificationMotive() != lastAnulatedLeave.getJustificationMotive()
			    || currentLeave.getReferenceDate() != lastAnulatedLeave.getReferenceDate()) {
			fillRow(spreadsheet, assiduousnessStatusHistory, assiduousnessClosedDay.getDay(), currentLeave
				.getJustificationMotive().getAcronym(), lastAnulatedLeave.getJustificationMotive().getAcronym(),
				currentLeave.getReferenceDate(), lastAnulatedLeave.getReferenceDate());
		    }
		} else {
		    fillRow(spreadsheet, assiduousnessStatusHistory, assiduousnessClosedDay.getDay(), null, lastAnulatedLeave
			    .getJustificationMotive().getAcronym(), null, lastAnulatedLeave.getReferenceDate());
		}
	    } else if (currentLeave != null) {
		fillRow(spreadsheet, assiduousnessStatusHistory, assiduousnessClosedDay.getDay(), currentLeave
			.getJustificationMotive().getAcronym(), null, currentLeave.getReferenceDate(), null);
	    }
	}
    }

    private void fillRow(StyledExcelSpreadsheet spreadsheet, AssiduousnessStatusHistory assiduousnessStatusHistory,
	    LocalDate localDate, String newAcronym, String oldAcronym, LocalDate newReferenceDate, LocalDate oldReferenceDate) {
	spreadsheet.newRow();
	spreadsheet.addCell(assiduousnessStatusHistory.getAssiduousness().getEmployee().getEmployeeNumber());
	spreadsheet.addCell(assiduousnessStatusHistory.getAssiduousness().getEmployee().getPerson().getName());
	spreadsheet.addCell(assiduousnessStatusHistory.getAssiduousnessStatus().getDescription());
	spreadsheet.addDateCell(localDate);
	spreadsheet.addCell(newAcronym);
	spreadsheet.addCell(oldAcronym);
	spreadsheet.addCell(newReferenceDate);
	spreadsheet.addCell(oldReferenceDate);
    }

    private void fillRow(StyledExcelSpreadsheet spreadsheet, AssiduousnessStatusHistory assiduousnessStatusHistory,
	    String justification, Integer oldValue, Integer newValue, Partial partial) {
	if (oldValue == null) {
	    oldValue = 0;
	}
	if (newValue == null) {
	    newValue = 0;
	}
	int correctionValue = newValue - oldValue;
	if (correctionValue != 0) {
	    spreadsheet.newRow();
	    spreadsheet.addCell(assiduousnessStatusHistory.getAssiduousness().getEmployee().getEmployeeNumber());
	    spreadsheet.addCell(assiduousnessStatusHistory.getAssiduousness().getEmployee().getPerson().getName());
	    spreadsheet.addCell(assiduousnessStatusHistory.getAssiduousnessStatus().getDescription());
	    spreadsheet.addCell(partial.get(DateTimeFieldType.year()));
	    spreadsheet.addCell(partial.get(DateTimeFieldType.monthOfYear()));
	    spreadsheet.addCell(justification);
	    spreadsheet.addCell(correctionValue);
	}
    }
}