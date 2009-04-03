package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessVacations;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class VacationsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	String action = request.getParameter("action");
	AssiduousnessExportChoices assiduousnessExportChoices = new AssiduousnessExportChoices(action);
	assiduousnessExportChoices.setChooseYear(true);
	request.setAttribute("assiduousnessExportChoices", assiduousnessExportChoices);
	request.setAttribute("action", action);
	request.setAttribute("vacations", true);
	return mapping.findForward("choose-year-month");
    }

    public ActionForward calculateA17AndA18(ActionMapping mapping, ActionForm actionFoautrm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AssiduousnessExportChoices assiduousnessExportChoices = (AssiduousnessExportChoices) getRenderedObject("assiduousnessExportChoices");

	response.setContentType("text/plain");
	StringBuilder fileName = new StringBuilder();
	fileName.append("a17a18_");
	fileName.append(assiduousnessExportChoices.getYearMonth().getYear());
	fileName.append(".xls");
	response.setHeader("Content-disposition", "attachment; filename=" + fileName.toString());
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("A17A18");
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.employeeNumber"));
	spreadsheet.addHeader(bundle.getString("label.employee.name"));
	spreadsheet.addHeader(bundle.getString("label.lastYearEfectiveWorkDays"));
	spreadsheet.addHeader(bundle.getString("label.art17And18MaximumLimitDays"));
	spreadsheet.addHeader(bundle.getString("label.art17And18LimitDays"));
	spreadsheet.addHeader(bundle.getString("label.art17Days"));
	spreadsheet.addHeader(bundle.getString("label.art18Days"));

	final ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(bundleEnumeration.getString(month.getName()));
	}

	spreadsheet.addHeader("Média de horas");
	spreadsheet.addHeader("Ano das horas");

	ServiceUtils.executeService("CalculateArticles17And18", new Object[] { assiduousnessExportChoices.getYearMonth()
		.getYear() });

	for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {
	    AssiduousnessVacations assiduousnessVacations = assiduousness
		    .getAssiduousnessVacationsByYear(assiduousnessExportChoices.getYearMonth().getYear());
	    if (assiduousnessVacations != null
		    && assiduousnessVacations.getYear().equals(assiduousnessExportChoices.getYearMonth().getYear())) {

		spreadsheet.newRow();
		spreadsheet.addCell(assiduousnessVacations.getAssiduousness().getEmployee().getEmployeeNumber().toString());
		spreadsheet.addCell(assiduousnessVacations.getAssiduousness().getEmployee().getPerson().getName());

		int efectiveWorkDays = assiduousnessVacations.getEfectiveWorkDays().intValue();
		spreadsheet.addCell(efectiveWorkDays);
		spreadsheet.addCell(assiduousnessVacations.getArt17And18MaximumLimitDays());
		spreadsheet.addCell(assiduousnessVacations.getArt17And18LimitDays());
		spreadsheet.addCell(assiduousnessVacations.getNumberOfArt17());
		spreadsheet.addCell(assiduousnessVacations.getNumberOfArt18());

		LocalDate beginDate = new LocalDate(assiduousnessVacations.getEfectiveWorkYear(), 1, 1);
		LocalDate endDate = new LocalDate(assiduousnessVacations.getEfectiveWorkYear(), 12, 31);
		List<AssiduousnessStatusHistory> assiduousnessStatusHistories = assiduousnessVacations.getAssiduousness()
			.getStatusBetween(beginDate, endDate);

		if (assiduousnessStatusHistories != null) {
		    for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {

			List<AssiduousnessClosedMonth> assiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>(
				assiduousnessStatusHistory.getAssiduousnessClosedMonths());
			Collections.sort(assiduousnessClosedMonths, new BeanComparator("beginDate"));

			for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessClosedMonths) {
			    if (assiduousnessClosedMonth.getBeginDate().get(DateTimeFieldType.year()) == assiduousnessVacations
				    .getEfectiveWorkYear()) {
				spreadsheet.addDuration(assiduousnessClosedMonth.getTotalWorkedTime(), assiduousnessClosedMonth
					.getBeginDate().get(DateTimeFieldType.monthOfYear()) + 6);
			    }
			}
		    }
		}

		spreadsheet.addDuration(assiduousness.getAverageWorkTimeDuration(beginDate, endDate), 19);
		spreadsheet.addCell(assiduousnessVacations.getEfectiveWorkYear(), 20);
	    }

	}
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }
}
