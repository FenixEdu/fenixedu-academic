package net.sourceforge.fenixedu.domain.assiduousness;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.Region;
import org.joda.time.DateTimeFieldType;

public class UnitExtraWorkAmount extends UnitExtraWorkAmount_Base {

    public UnitExtraWorkAmount(Integer year, Unit unit) {
	super();
	if (!isUniqueYearUnit(year, unit)) {
	    throw new DomainException("error.extraWorkAmount.alreadyExistsUnitYear");
	}
	setRootDomainObject(RootDomainObject.getInstance());
	setYear(year);
	setUnit(unit);
	setSpent(0.0);
    }

    private boolean isUniqueYearUnit(Integer year, Unit unit) {
	for (UnitExtraWorkAmount unitExtraWorkAmount : RootDomainObject.getInstance()
		.getUnitsExtraWorkAmounts()) {
	    if (unitExtraWorkAmount != this && unitExtraWorkAmount.getYear() == year
		    && unitExtraWorkAmount.getUnit() == unit) {
		return false;
	    }
	}
	return true;
    }

    public Double getTotal() {
	Double total = 0.0;
	for (UnitExtraWorkMovement unitExtraWorkMovement : getUnitExtraWorkMovements()) {
	    total += unitExtraWorkMovement.getAmount();
	}
	return total;
    }

    public Double getInitial() {
	UnitExtraWorkMovement initial = null;
	for (UnitExtraWorkMovement unitExtraWorkMovement : getUnitExtraWorkMovements()) {
	    if (initial == null || initial.getDate().isAfter(unitExtraWorkMovement.getDate())) {
		initial = unitExtraWorkMovement;
	    }
	}
	return initial == null ? 0.0 : initial.getAmount();
    }

    public Double getBalance() {
	return getTotal() - getSpent();
    }

    public void sumSpent(Double amount) {
	setSpent(getSpent() + amount);
    }

    public void subtractSpent(Double amount) {
	setSpent(getSpent() - amount);
    }

    public void updateValue(Double oldValue, Double newValue) {
	sumSpent(oldValue);
	subtractSpent(newValue);
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle,
	    ResourceBundle enumBundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.workingUnit"), 15000);
	spreadsheet.addHeader(bundle.getString("label.initial"));
	spreadsheet.addHeader(bundle.getString("label.actual"));
	spreadsheet.addHeader(bundle.getString("label.balance"));
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(enumBundle.getString(month.getName()));
	}
    }

    public static void getExcelFooter(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	int lastRow = spreadsheet.getSheet().getLastRowNum();
	int lastColumn = spreadsheet.getSheet().getRow(2).getLastCellNum();
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(bundle.getString("label.total").toUpperCase());
	spreadsheet.sumColumn(3, lastRow, 1, lastColumn, spreadsheet.getExcelStyle().getDoubleStyle());
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet) {
	HSSFCellStyle doubleStyle = spreadsheet.getExcelStyle().getDoubleStyle();
	HSSFCellStyle valueStyle = spreadsheet.getExcelStyle().getValueStyle();
	if (getBalance().compareTo(50.0) <= 0) {
	    doubleStyle = spreadsheet.getExcelStyle().getDoubleNegativeStyle();
	    valueStyle = spreadsheet.getExcelStyle().getRedValueStyle();
	}
	spreadsheet.newRow();
	spreadsheet.addCell(getUnit().getCostCenterCode().toString() + "  " + getUnit().getName(),
		valueStyle);
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.addCell(new Double(decimalFormat.format(getInitial())), doubleStyle);
	spreadsheet.addCell(new Double(decimalFormat.format(getTotal())), doubleStyle);
	if (getBalance().compareTo(50.0) <= 0) {
	    spreadsheet.addCell(new Double(decimalFormat.format(getBalance())), doubleStyle);
	} else {
	    spreadsheet.addCell(new Double(decimalFormat.format(getBalance())), doubleStyle);
	}
	for (Month month : Month.values()) {
	    Double monthValue = getMonthValue(month);
	    if (monthValue.equals(0.0)) {
		spreadsheet.addCell("");
	    } else {
		spreadsheet.addCell(new Double(decimalFormat.format(monthValue)), spreadsheet
			.getExcelStyle().getDoubleStyle());
	    }
	}
    }

    private Double getMonthValue(Month month) {
	Double monthTotal = 0.0;
	for (ExtraWorkRequest extraWorkRequest : getUnit().getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.year()) == getYear()
		    && extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.monthOfYear()) == month
			    .getNumberOfMonth() && extraWorkRequest.getApproved()) {
		monthTotal += extraWorkRequest.getAmount();
	    }
	}
	return monthTotal;
    }

    public void getExtraWorkAuthorizationsExcelRows(StyledExcelSpreadsheet spreadsheet,
	    ResourceBundle bundle, ResourceBundle enumBundle) {
	for (ExtraWorkAuthorization extraWorkAuthorization : getUnit()
		.getExtraPayingUnitAuthorizations()) {
	    if (!spreadsheet.hasSheet(getUnit().getCostCenterCode().toString())) {
		spreadsheet.getSheet(getUnit().getCostCenterCode().toString());

		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		spreadsheet.newHeaderRow();
		spreadsheet.addCell(getUnit().getCostCenterCode().toString() + "  "
			+ getUnit().getName(), spreadsheet.getExcelStyle().getLabelStyle());
		spreadsheet.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 1));

		spreadsheet.newHeaderRow();
		spreadsheet.newHeaderRow();
		spreadsheet.addCell(bundle.getString("label.initial"), spreadsheet.getExcelStyle()
			.getLabelStyle());
		spreadsheet.addCell(new Double(decimalFormat.format(getInitial())));
		spreadsheet.newHeaderRow();
		spreadsheet.addCell(bundle.getString("label.actual"), spreadsheet.getExcelStyle()
			.getLabelStyle());
		spreadsheet.addCell(new Double(decimalFormat.format(getTotal())));
		spreadsheet.newHeaderRow();
		spreadsheet.addCell(bundle.getString("label.balance"), spreadsheet.getExcelStyle()
			.getLabelStyle());
		spreadsheet.addCell(new Double(decimalFormat.format(getBalance())));

		spreadsheet.newHeaderRow();
		spreadsheet.newHeaderRow();
		EmployeeExtraWorkAuthorization.getExcelHeader(spreadsheet, bundle, enumBundle);

	    } else {
		spreadsheet.getSheet(getUnit().getCostCenterCode().toString());
	    }
	    for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : extraWorkAuthorization
		    .getEmployeeExtraWorkAuthorizations()) {
		employeeExtraWorkAuthorization.getExcelRow(spreadsheet, getYear());
	    }
	}
    }

}
