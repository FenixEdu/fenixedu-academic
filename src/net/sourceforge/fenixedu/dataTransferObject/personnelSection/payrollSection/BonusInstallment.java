package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeMonthlyBonusInstallment;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.poi.hssf.util.Region;

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
	this.bonusInstallmentList = new DomainListReference<EmployeeBonusInstallment>(
		bonusInstallmentList);
    }

    public void updateList() {
	if (getYear() != null && getInstallment() != null) {
	    AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment
		    .readByYearAndInstallment(getYear(), getInstallment());
	    setBonusInstallmentList(anualBonusInstallment.getEmployeeBonusInstallments());
	}
    }

    public void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle,
	    ResourceBundle enumBundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.number"), 1250);
	spreadsheet.addHeader(bundle.getString("label.employee.name"), 7500);
	spreadsheet.addHeader(bundle.getString("label.p1"), 2000);
	spreadsheet.addHeader(bundle.getString("label.p2"), 2000);
	spreadsheet.addHeader(bundle.getString("label.workingUnit"), 2000);
	spreadsheet.addHeader(bundle.getString("label.subCostCenter"), 2000);
	spreadsheet.addHeader(bundle.getString("label.explorationUnit"), 2000);

	List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
		.readByYear(getYear());
	int monthsNumber = Month.values().length / anualBonusInstallmentList.size();
	int beginMonth = (getInstallment().intValue() - 1) * monthsNumber + 1;
	if (anualBonusInstallmentList.size() == getInstallment().intValue()) {
	    monthsNumber = monthsNumber + Month.values().length % anualBonusInstallmentList.size();
	}
	int endMonth = beginMonth + monthsNumber;

	for (int monthIndex = beginMonth; monthIndex <= endMonth; monthIndex++) {
	    spreadsheet.addHeader("");
	    spreadsheet.addHeader("");
	    spreadsheet.addHeader(enumBundle.getString(Month.values()[monthIndex].name()));
	}

	for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
	    spreadsheet.getSheet().addMergedRegion(
		    new Region(0, (short) columnIndex, 1, (short) columnIndex));
	}
	spreadsheet.newHeaderRow();
	int rowNumber = spreadsheet.getSheet().getLastRowNum() - 1;
	for (int index = 8, monthIndex = beginMonth; monthIndex <= endMonth; monthIndex++, index += 3) {
	    spreadsheet.addHeader(bundle.getString("label.absences"), 2000, index);
	    spreadsheet.addHeader(bundle.getString("label.p1"), 2000, index + 1);
	    spreadsheet.addHeader(bundle.getString("label.p2"), 2000, index + 2);
	    spreadsheet.getSheet().addMergedRegion(
		    new Region(rowNumber, (short) index, rowNumber, (short) (index + 2)));
	}
    }

    public void getRows(StyledExcelSpreadsheet spreadsheet) {
	for (EmployeeBonusInstallment employeeBonusInstallment : getBonusInstallmentList()) {
	    spreadsheet.newRow();
	    spreadsheet.addCell(employeeBonusInstallment.getEmployee().getEmployeeNumber());
	    spreadsheet.addCell(employeeBonusInstallment.getEmployee().getPerson().getName());
	    spreadsheet.addCell(employeeBonusInstallment.getInstallmentP1Value());
	    spreadsheet.addCell(employeeBonusInstallment.getInstallmentP2Value());
	    spreadsheet.addCell(employeeBonusInstallment.getCostCenterCode(), spreadsheet
		    .getExcelStyle().getIntegerStyle());
	    spreadsheet.addCell(employeeBonusInstallment.getSubCostCenterCode(), spreadsheet
		    .getExcelStyle().getIntegerStyle());
	    spreadsheet.addCell(employeeBonusInstallment.getExplorationUnit(), spreadsheet
		    .getExcelStyle().getIntegerStyle());
	    for (EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment : employeeBonusInstallment
		    .getEmployeeMonthlyBonusInstallmentsOrdered()) {
		spreadsheet.addCell(employeeMonthlyBonusInstallment.getAbsences(), spreadsheet
			.getExcelStyle().getIntegerStyle());
		spreadsheet.addCell(employeeMonthlyBonusInstallment.getP1Value());
		spreadsheet.addCell(employeeMonthlyBonusInstallment.getP2Value());
	    }
	}
    }
}