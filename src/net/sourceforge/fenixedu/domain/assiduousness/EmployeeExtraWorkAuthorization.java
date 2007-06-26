package net.sourceforge.fenixedu.domain.assiduousness;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExtraWorkAuthorizationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.lang.exception.NestableException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

public class EmployeeExtraWorkAuthorization extends EmployeeExtraWorkAuthorization_Base {

    public EmployeeExtraWorkAuthorization(ExtraWorkAuthorization extraWorkAuthorization,
	    EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setExtraWorkAuthorization(extraWorkAuthorization);
	setAssiduousness(employeeExtraWorkAuthorizationBean.getEmployee().getAssiduousness());
	setModifiedBy(employeeExtraWorkAuthorizationBean.getModifiedBy());
	if (!employeeExtraWorkAuthorizationBean.getAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getNightExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusOneHundredHours()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusTwoHours()
		&& !employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork()) {
	    throw new DomainException("error.extraWorkAuthorization.options");
	}
	setAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getAuxiliarPersonel());
	setExecutiveAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel());
	setNightExtraWork(employeeExtraWorkAuthorizationBean.getNightExtraWork());
	setNormalExtraWork(employeeExtraWorkAuthorizationBean.getNormalExtraWork());
	setNormalExtraWorkPlusOneHundredHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusOneHundredHours());
	setNormalExtraWorkPlusTwoHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusTwoHours());
	setWeeklyRestExtraWork(employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork());
	setLastModifiedDate(new DateTime());
    }

    public void edit(EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean) {
	if (!employeeExtraWorkAuthorizationBean.getAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getNightExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusOneHundredHours()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusTwoHours()
		&& !employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork()) {
	    throw new DomainException("error.extraWorkAuthorization.options");
	}
	setModifiedBy(employeeExtraWorkAuthorizationBean.getModifiedBy());
	setAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getAuxiliarPersonel());
	setExecutiveAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel());
	setNightExtraWork(employeeExtraWorkAuthorizationBean.getNightExtraWork());
	setNormalExtraWork(employeeExtraWorkAuthorizationBean.getNormalExtraWork());
	setNormalExtraWorkPlusOneHundredHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusOneHundredHours());
	setNormalExtraWorkPlusTwoHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusTwoHours());
	setWeeklyRestExtraWork(employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork());
	setLastModifiedDate(new DateTime());
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousness();
	removeExtraWorkAuthorization();
	removeModifiedBy();
	deleteDomainObject();
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle,
	    ResourceBundle enumBundle) {
	spreadsheet.newHeaderRow();
	int firstHeaderRow = spreadsheet.getSheet().getLastRowNum();
	spreadsheet.addHeader(bundle.getString("label.number"), 1500);
	spreadsheet.addHeader(bundle.getString("label.employee.name"), 5000);
	int cellNum = 0;
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(enumBundle.getString(month.getName()), spreadsheet.getExcelStyle()
		    .getVerticalHeaderStyle());
	    spreadsheet.addHeader("");
	    cellNum = (short) spreadsheet.getSheet().getRow(firstHeaderRow).getLastCellNum();
	    spreadsheet.getSheet().addMergedRegion(
		    new Region(firstHeaderRow, (short) (cellNum - 1), firstHeaderRow, (short) cellNum));
	}
	spreadsheet.getRow().setHeight((short) 1000);
	spreadsheet.addHeader(bundle.getString("label.total"), 2000);
	spreadsheet.newHeaderRow();
	spreadsheet.getSheet().addMergedRegion(
		new Region(firstHeaderRow, (short) 0, firstHeaderRow + 1, (short) 0));
	spreadsheet.getSheet().addMergedRegion(
		new Region(firstHeaderRow, (short) 1, firstHeaderRow + 1, (short) 1));

	spreadsheet.getSheet().addMergedRegion(
		new Region(firstHeaderRow, (short) (cellNum + 1), firstHeaderRow + 1,
			(short) (cellNum + 1)));
	spreadsheet.addHeader("");
	spreadsheet.addHeader("");
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(bundle.getString("label.hoursNum"), spreadsheet.getExcelStyle()
		    .getVerticalHeaderStyle(), 600);
	    spreadsheet.addHeader(bundle.getString("label.value"), spreadsheet.getExcelStyle()
		    .getVerticalHeaderStyle(), 1600);
	}
	spreadsheet.addHeader("");
    }

    public static void getExcelFooter(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	int firstRow = 9;
	int firstColumn = 3;
	int lastRow = spreadsheet.getSheet().getLastRowNum();
	int lastColumn = spreadsheet.getMaxiumColumnNumber() - 1;
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(bundle.getString("label.total").toUpperCase());

	for (int col = firstColumn; col <= lastColumn; col += 2) {
	    spreadsheet.sumColumn(firstRow, lastRow, col, col, spreadsheet.getExcelStyle()
		    .getDoubleStyle());
	}
	spreadsheet.newRow();
	spreadsheet.newRow();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.sumRows(firstRow, lastRow, firstColumn, lastColumn, 2, spreadsheet.getExcelStyle()
		.getDoubleStyle());
	spreadsheet.setRegionBorder(firstRow, spreadsheet.getSheet().getLastRowNum() - 1, 0, spreadsheet
		.getMaxiumColumnNumber() + 1);
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet, int year) {
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.newRow();
	spreadsheet.addCell(getAssiduousness().getEmployee().getEmployeeNumber().toString());
	spreadsheet.addCell(getSmallName());
	List<ExtraWorkRequest> extraWorkRequests = getAssiduousness().getExtraWorkRequestsByUnit(
		getExtraWorkAuthorization().getPayingUnit(), year);
	for (ExtraWorkRequest extraWorkRequest : extraWorkRequests) {
	    Integer oldValue = getOldValue(spreadsheet,
		    extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.monthOfYear()) * 2)
		    .intValue();
	    Double oldDouble = getOldValue(spreadsheet, extraWorkRequest.getPartialPayingDate().get(
		    DateTimeFieldType.monthOfYear()) * 2 + 1);

	    spreadsheet.addCell(new Integer(extraWorkRequest.getTotalHours() + oldValue),
		    (extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.monthOfYear()) * 2));
	    spreadsheet.addCell(new Double(decimalFormat
		    .format(extraWorkRequest.getAmount() + oldDouble)), spreadsheet.getExcelStyle()
		    .getDoubleStyle(), (extraWorkRequest.getPartialPayingDate().get(
		    DateTimeFieldType.monthOfYear()) * 2) + 1);
	}
    }

    private Double getOldValue(StyledExcelSpreadsheet spreadsheet, int colNumber) {
	HSSFCell cell = spreadsheet.getRow().getCell((short) colNumber);
	Double oldValue = 0.0;
	if (cell != null) {
	    oldValue = cell.getNumericCellValue();
	}
	return oldValue;
    }

    private String getSmallName() {
	String name = getAssiduousness().getEmployee().getPerson().getName();
	return name.substring(0, name.indexOf(" ")).concat(name.substring(name.lastIndexOf(" ")));
    }
}
