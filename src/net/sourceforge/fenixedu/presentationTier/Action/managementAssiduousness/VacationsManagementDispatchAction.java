package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeAssiduousnessExemption;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(module = "personnelSection", path = "/vacationsManagement", attribute = "extraWorkForm", formBean = "extraWorkForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "choose-year-month", path = "/managementAssiduousness/chooseYearMonth.jsp", tileProperties = @Tile(title = "private.staffarea.holidays.calculatea17anda18")) })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
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

	AssiduousnessExportChoices assiduousnessExportChoices = getRenderedObject("assiduousnessExportChoices");

	response.setContentType("text/plain");
	StringBuilder fileName = new StringBuilder();
	fileName.append("a17a18_");
	fileName.append(assiduousnessExportChoices.getYearMonth().getYear());
	fileName.append(".xls");
	response.setHeader("Content-disposition", "attachment; filename=" + fileName.toString());
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());

	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("A17A18");

	HSSFFont font = spreadsheet.getWorkbook().createFont();
	font.setColor(HSSFColor.BLACK.index);
	font.setFontHeightInPoints((short) 8);

	HSSFCellStyle redStyle = spreadsheet.getWorkbook().createCellStyle();
	redStyle.setFont(font);
	redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	redStyle.setDataFormat(spreadsheet.getWorkbook().createDataFormat().getFormat("0"));
	redStyle.setFillForegroundColor(HSSFColor.RED.index);
	redStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	HSSFCellStyle newRowStyle = spreadsheet.getWorkbook().createCellStyle();
	newRowStyle.setFont(font);
	newRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	newRowStyle.setDataFormat(spreadsheet.getWorkbook().createDataFormat().getFormat("0"));
	newRowStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
	newRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	HSSFCellStyle normalStyle = spreadsheet.getExcelStyle().getValueStyle();

	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.employeeNumber"));
	spreadsheet.addHeader(bundle.getString("label.employee.name"));
	spreadsheet.addHeader(bundle.getString("label.efectiveWorkYear"));
	spreadsheet.addHeader(bundle.getString("label.efectiveWorkDays"));
	spreadsheet.addHeader(bundle.getString("label.art17And18MaximumLimitDays"));
	spreadsheet.addHeader(bundle.getString("label.art17And18LimitDays"));
	spreadsheet.addHeader(bundle.getString("label.art18Days"));
	spreadsheet.addHeader(bundle.getString("label.usedArt18Days"));
	spreadsheet.addHeader(bundle.getString("label.art17Days"));
	spreadsheet.addHeader(bundle.getString("label.monthsToDiscountInArt17Vacations"));
	spreadsheet.addHeader(bundle.getString("label.usedArt17Days"));
	spreadsheet.addHeader("Férias por A17 a passar para " + (assiduousnessExportChoices.getYearMonth().getYear() + 1));

	for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {

	    Employee employee = assiduousness.getEmployee();
	    EmployeeAssiduousnessExemption employeeAssiduousnessExemption = new EmployeeAssiduousnessExemption(employee,
		    assiduousnessExportChoices.getYearMonth().getYear());

	    HSSFCellStyle styleToUse = normalStyle;
	    if (employeeAssiduousnessExemption.getYear().intValue() == employeeAssiduousnessExemption.getEfectiveWorkYear()
		    .intValue()) {
		styleToUse = newRowStyle;
	    }

	    spreadsheet.newRow();
	    spreadsheet.addCell(employee.getEmployeeNumber().toString(), styleToUse);
	    spreadsheet.addCell(employee.getPerson().getName(), styleToUse);
	    spreadsheet.addCell(employeeAssiduousnessExemption.getEfectiveWorkYear(), styleToUse);

	    int efectiveWorkDays = employeeAssiduousnessExemption.getEfectiveWorkDays().intValue();
	    spreadsheet.addCell(efectiveWorkDays, styleToUse);

	    spreadsheet.addCell(employeeAssiduousnessExemption.getArt17And18MaximumLimitDays(), styleToUse);
	    spreadsheet.addCell(employeeAssiduousnessExemption.getArt17And18LimitDays(), styleToUse);
	    spreadsheet.addCell(employeeAssiduousnessExemption.getNumberOfArt18(), styleToUse);

	    if (BigDecimal.valueOf(employeeAssiduousnessExemption.getNumberOfArt18()).compareTo(
		    employeeAssiduousnessExemption.getUsedArt18()) < 0) {
		spreadsheet.addCell(employeeAssiduousnessExemption.getUsedArt18(), redStyle);
	    } else {
		spreadsheet.addCell(employeeAssiduousnessExemption.getUsedArt18(), styleToUse);
	    }

	    spreadsheet.addCell(employeeAssiduousnessExemption.getNumberOfArt17(), styleToUse);

	    if (employeeAssiduousnessExemption.getMonthsToDiscountInArt17Vacations() > 0) {
		spreadsheet.addCell(employeeAssiduousnessExemption.getMonthsToDiscountInArt17Vacations(), redStyle);
	    } else {
		spreadsheet.addCell(employeeAssiduousnessExemption.getMonthsToDiscountInArt17Vacations(), styleToUse);
	    }

	    if (employeeAssiduousnessExemption.getNumberOfArt17() < employeeAssiduousnessExemption.getUsedArt17()) {
		spreadsheet.addCell(employeeAssiduousnessExemption.getUsedArt17(), redStyle);
	    } else {
		spreadsheet.addCell(employeeAssiduousnessExemption.getUsedArt17(), styleToUse);
	    }
	    spreadsheet.addCell(employeeAssiduousnessExemption.getArt17Vacations(), styleToUse);
	}
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

}
