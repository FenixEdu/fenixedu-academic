package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeMonthlyBonusInstallment;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.poi.hssf.util.Region;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class BonusInstallment implements Serializable {
    private Integer year;

    private Integer installment;

    private DomainListReference<EmployeeBonusInstallment> bonusInstallmentList;

    public BonusInstallment() {
    }

    public Integer getInstallment() {
	return installment;
    }

    public void setInstallment(Integer installment) {
	this.installment = installment;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public List<EmployeeBonusInstallment> getBonusInstallmentList() {
	return bonusInstallmentList;
    }

    public void setBonusInstallmentList(List<EmployeeBonusInstallment> bonusInstallmentList) {
	this.bonusInstallmentList = new DomainListReference<EmployeeBonusInstallment>(bonusInstallmentList);
    }

    public void updateList() {
	if (getYear() != null && getInstallment() != null) {
	    AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment.readByYearAndInstallment(getYear(),
		    getInstallment());
	    setBonusInstallmentList(anualBonusInstallment.getEmployeeBonusInstallments());
	}
    }

    public void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle, ResourceBundle enumBundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.number"), 1250);
	spreadsheet.addHeader(bundle.getString("label.employee.name"), 7500);
	spreadsheet.addHeader(bundle.getString("label.contractType"), 4000);
	spreadsheet.addHeader(bundle.getString("label.bonusType"), 2000);
	spreadsheet.addHeader(bundle.getString("label.value"), 2000);
	spreadsheet.addHeader(bundle.getString("label.workingUnit"), 2000);
	spreadsheet.addHeader(bundle.getString("label.subCostCenter"), 2000);
	spreadsheet.addHeader(bundle.getString("label.explorationUnit"), 2000);
	int nextFirstRow = spreadsheet.getRow().getLastCellNum() + 1;
	AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment.readByYearAndInstallment(getYear(), getInstallment());

	List<Partial> sortedPartials = anualBonusInstallment.getAssiduousnessPartials().getSortedPartials();

	for (Partial partial : sortedPartials) {
	    spreadsheet.addHeader(enumBundle.getString(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1].name()));
	    spreadsheet.addHeader(enumBundle.getString(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1].name()));
	    spreadsheet.addHeader(enumBundle.getString(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1].name()));
	    spreadsheet.addHeader(enumBundle.getString(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1].name()));
	    spreadsheet.addHeader(enumBundle.getString(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1].name()));
	}

	for (int columnIndex = 0; columnIndex < nextFirstRow; columnIndex++) {
	    spreadsheet.getSheet().addMergedRegion(new Region(0, (short) columnIndex, 1, (short) columnIndex));
	}
	spreadsheet.newHeaderRow();
	int rowNumber = spreadsheet.getSheet().getLastRowNum() - 1;
	for (int index = nextFirstRow, monthIndex = 0; monthIndex < sortedPartials.size(); monthIndex++, index += 5) {
	    spreadsheet.addHeader(bundle.getString("label.maximumWorkingDays"), 2000, index);
	    spreadsheet.addHeader(bundle.getString("label.workedDays"), 2000, index + 1);
	    spreadsheet.addHeader(bundle.getString("label.absences"), 2000, index + 2);
	    spreadsheet.addHeader(bundle.getString("label.bonusType"), 2000, index + 3);
	    spreadsheet.addHeader(bundle.getString("label.value"), 2000, index + 4);
	    spreadsheet.getSheet().addMergedRegion(new Region(rowNumber, (short) index, rowNumber, (short) (index + 4)));
	}
    }

    private AssiduousnessStatusHistory getLastAssiduousnessStatusHistoryBefore(EmployeeBonusInstallment employeeBonusInstallment) {
	LocalDate day = new LocalDate(employeeBonusInstallment.getAnualBonusInstallment().getPaymentPartialDate().get(
		DateTimeFieldType.year()), employeeBonusInstallment.getAnualBonusInstallment().getPaymentPartialDate().get(
		DateTimeFieldType.monthOfYear()), 1);
	if (!employeeBonusInstallment.getEmployee().hasAssiduousness()) {
	    System.out.println("Employee " + employeeBonusInstallment.getEmployee().getEmployeeNumber()
		    + " does not have assidousness");
	    return null;
	}
	AssiduousnessStatusHistory lastAssiduousnessStatusHistory = null;
	List<AssiduousnessStatusHistory> assiduousnessStatusHistories = employeeBonusInstallment.getEmployee().getAssiduousness()
		.getStatusBetween(day, day);
	if (assiduousnessStatusHistories != null && assiduousnessStatusHistories.size() != 0) {
	    lastAssiduousnessStatusHistory = assiduousnessStatusHistories.get(0);
	    if (lastAssiduousnessStatusHistory == null) {
		for (AssiduousnessStatusHistory assiduousnessStatusHistory : employeeBonusInstallment.getEmployee()
			.getAssiduousness().getAssiduousnessStatusHistories()) {
		    if (assiduousnessStatusHistory.getEndDate() == null) {
			return assiduousnessStatusHistory;
		    }
		    if (lastAssiduousnessStatusHistory == null
			    || assiduousnessStatusHistory.getEndDate().isAfter(lastAssiduousnessStatusHistory.getEndDate())) {
			lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		    }
		}
	    }
	}
	return lastAssiduousnessStatusHistory;
    }

    public void getRows(StyledExcelSpreadsheet spreadsheet, ResourceBundle enumBundle) {
	for (EmployeeBonusInstallment employeeBonusInstallment : getBonusInstallmentList()) {
	    spreadsheet.newRow();
	    spreadsheet.addCell(employeeBonusInstallment.getEmployee().getEmployeeNumber());
	    spreadsheet.addCell(employeeBonusInstallment.getEmployee().getPerson().getName());

	    AssiduousnessStatusHistory assiduousnessStatusHistory = getLastAssiduousnessStatusHistoryBefore(employeeBonusInstallment);
	    spreadsheet.addCell(assiduousnessStatusHistory != null ? assiduousnessStatusHistory.getAssiduousnessStatus()
		    .getDescription() : "");

	    spreadsheet.addCell(enumBundle.getString(employeeBonusInstallment.getBonusType().name()));
	    spreadsheet.addCell(employeeBonusInstallment.getValue());
	    spreadsheet.addCell(new DecimalFormat("0000").format(employeeBonusInstallment.getCostCenterCode()), spreadsheet
		    .getExcelStyle().getIntegerStyle());
	    spreadsheet.addCell(employeeBonusInstallment.getSubCostCenterCode());
	    spreadsheet.addCell(employeeBonusInstallment.getExplorationUnit(), spreadsheet.getExcelStyle().getIntegerStyle());
	    for (EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment : employeeBonusInstallment
		    .getEmployeeMonthlyBonusInstallmentsOrdered()) {
		ClosedMonth closedMonth = ClosedMonth.getClosedMonth(new YearMonth(employeeMonthlyBonusInstallment
			.getPartialYearMonth()));
		int maximumWorkingDays = 0;
		int workedDays = 0;
		int absences = 0;
		if (closedMonth != null) {
		    Assiduousness assiduousness = employeeMonthlyBonusInstallment.getEmployeeBonusInstallment().getEmployee()
			    .getAssiduousness();
		    for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth
			    .getAssiduousnessClosedMonths(assiduousness)) {
			if (assiduousnessClosedMonth != null) {
			    maximumWorkingDays += assiduousnessClosedMonth.getMaximumWorkingDays().intValue();
			    workedDays += assiduousnessClosedMonth.getWorkedDaysWithBonusDaysDiscount().intValue();
			    absences += (assiduousnessClosedMonth.getMaximumWorkingDays().intValue()
				    - assiduousnessClosedMonth.getWorkedDaysWithBonusDaysDiscount().intValue());
			}
		    }
		}
		spreadsheet.addCell(Integer.valueOf(maximumWorkingDays));
		spreadsheet.addCell(Integer.valueOf(workedDays));
		spreadsheet.addCell(Integer.valueOf(absences));
		spreadsheet.addCell(enumBundle.getString(employeeBonusInstallment.getBonusType().name()));
		spreadsheet.addCell(employeeMonthlyBonusInstallment.getValue());
	    }
	}
    }
}