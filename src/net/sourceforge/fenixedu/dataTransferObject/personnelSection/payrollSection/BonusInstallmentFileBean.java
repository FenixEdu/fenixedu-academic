package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeMonthlyBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.util.BonusType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class BonusInstallmentFileBean implements Serializable, FactoryExecutor {

    private static int miniumWorkedDays = 17;

    private Integer year;

    private Integer installment;

    private transient InputStream inputStream;

    private String fileName;

    public BonusInstallmentFileBean(Integer year, Integer installment) {
	setYear(year);
	setInstallment(installment);
    }

    public BonusInstallmentFileBean() {
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
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

    public Object execute() {
	if (isComplete()) {
	    List<EmployeeBonusInstallmentBean> employeeBonusInstallmentList = new ArrayList<EmployeeBonusInstallmentBean>();
	    try {
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
		    HSSFRow row = sheet.getRow(rowIndex);
		    String bonusType = row.getCell((short) 0).getStringCellValue();
		    if (bonusType != null
			    && (bonusType.trim().toUpperCase().equals("P1") || bonusType.trim()
				    .toUpperCase().equals("P2"))) {
			try {
			    employeeBonusInstallmentList.add(new EmployeeBonusInstallmentBean(row,
				    bonusType.trim().toUpperCase()));
			} catch (Exception e) {
			    return new ActionMessage("error.errorReadingFileLine",
				    new Object[] { rowIndex + 1 });
			}
		    } else {
			return new ActionMessage("error.errorReadingFileLine",
				new Object[] { rowIndex + 1 });
		    }
		}
	    } catch (IOException e) {
		return new ActionMessage("error.errorReadingFile");
	    }
	    List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
		    .readByYear(getYear());
	    Collections.sort(anualBonusInstallmentList, new BeanComparator("paymentPartialDate"));
	    AnualBonusInstallment choosenAnualBonusInstallment = anualBonusInstallmentList
		    .get(getInstallment() - 1);

	    Map<Integer, ClosedMonth> closedMonthMap = new HashMap<Integer, ClosedMonth>();
	    for (YearMonth yearMonth : choosenAnualBonusInstallment.getAssiduousnessYearMonths()
		    .getYearsMonths()) {
		ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
		if (closedMonth == null) {
		    return new ActionMessage("error.notAllInstallmentMonthsAreClosed");
		}
		closedMonthMap.put(yearMonth.getNumberOfMonth(), closedMonth);
	    }

	    AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment
		    .readByYearAndInstallment(getYear(), getInstallment());
	    for (EmployeeBonusInstallmentBean employeeBonusInstallmentBean : employeeBonusInstallmentList) {
		EmployeeBonusInstallment thisEmployeeBonusInstallment = null;
		if (!anualBonusInstallment.getEmployeeBonusInstallments().isEmpty()) {
		    thisEmployeeBonusInstallment = anualBonusInstallment.getEmployeeBonusInstallment(
			    employeeBonusInstallmentBean.getEmployee(),
			    employeeBonusInstallmentBean.bonusType);
		}
		if (thisEmployeeBonusInstallment == null) {
		    thisEmployeeBonusInstallment = new EmployeeBonusInstallment(anualBonusInstallment,
			    employeeBonusInstallmentBean.getEmployee(), employeeBonusInstallmentBean
				    .getValue(), employeeBonusInstallmentBean.getBonusType(),
			    employeeBonusInstallmentBean.getCostCenterCode(),
			    employeeBonusInstallmentBean.getSubCostCenterCode(),
			    employeeBonusInstallmentBean.getExplorationUnit());
		} else {
		    thisEmployeeBonusInstallment.edit(employeeBonusInstallmentBean.getValue(),
			    employeeBonusInstallmentBean.getBonusType(), employeeBonusInstallmentBean
				    .getCostCenterCode(), employeeBonusInstallmentBean
				    .getSubCostCenterCode(), employeeBonusInstallmentBean
				    .getExplorationUnit());
		}
		for (YearMonth yearMonth : choosenAnualBonusInstallment.getAssiduousnessYearMonths()
			.getYearsMonths()) {
		    EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment = thisEmployeeBonusInstallment
			    .getEmployeeMonthlyBonusInstallment(yearMonth);

		    double value = thisEmployeeBonusInstallment.getValue()
			    / choosenAnualBonusInstallment.getAssiduousnessYearMonths().getYearsMonths()
				    .size();
		    ClosedMonth closedMonth = closedMonthMap.get(yearMonth.getNumberOfMonth());
		    if (closedMonth.getAssiduousnessClosedMonth(thisEmployeeBonusInstallment
			    .getEmployee().getAssiduousness()) == null
			    || closedMonth.getAssiduousnessClosedMonth(
				    thisEmployeeBonusInstallment.getEmployee().getAssiduousness())
				    .getWorkedDays() < miniumWorkedDays) {
			value = 0;
		    }
		    if (employeeMonthlyBonusInstallment == null) {
			DateTimeFieldType[] dateTimeFields = { DateTimeFieldType.year(),
				DateTimeFieldType.monthOfYear() };
			int[] fieldValues = { yearMonth.getYear(), yearMonth.getNumberOfMonth() };
			employeeMonthlyBonusInstallment = new EmployeeMonthlyBonusInstallment(
				thisEmployeeBonusInstallment, new Partial(dateTimeFields, fieldValues),
				value);
		    } else {
			employeeMonthlyBonusInstallment.edit(value);
		    }
		}
	    }
	}
	return null;
    }

    public class EmployeeBonusInstallmentBean implements Serializable {
	private Employee employee;

	private Double value;

	private BonusType bonusType;

	private Integer costCenterCode;

	private Integer subCostCenterCode;

	private Integer explorationUnit;

	public EmployeeBonusInstallmentBean(HSSFRow row, String bonusType) {
	    setBonusType(getBonusTypeEnum(bonusType));
	    setEmployee(Employee.readByNumber(new Double(row.getCell((short) 1).getNumericCellValue())
		    .intValue()));
	    setValue(row.getCell((short) 3).getNumericCellValue());
	    setCostCenterCode(new Double(row.getCell((short) 4).getNumericCellValue()).intValue());
	    setSubCostCenterCode(new Double(row.getCell((short) 5).getNumericCellValue()).intValue());
	    setExplorationUnit(new Double(row.getCell((short) 6).getNumericCellValue()).intValue());
	}

	private BonusType getBonusTypeEnum(String bonusType) {
	    if (bonusType.equals("P1")) {
		return BonusType.DEDICATION_BONUS;
	    } else if (bonusType.equals("P2")) {
		return BonusType.EXCEPTIONAL_BONUS;
	    }
	    return null;
	}

	public Integer getCostCenterCode() {
	    return costCenterCode;
	}

	public void setCostCenterCode(Integer costCenterCode) {
	    this.costCenterCode = costCenterCode;
	}

	public Employee getEmployee() {
	    return employee;
	}

	public void setEmployee(Employee employee) {
	    this.employee = employee;
	}

	public Integer getExplorationUnit() {
	    return explorationUnit;
	}

	public void setExplorationUnit(Integer explorationUnit) {
	    this.explorationUnit = explorationUnit;
	}

	public Integer getSubCostCenterCode() {
	    return subCostCenterCode;
	}

	public void setSubCostCenterCode(Integer subCostCenterCode) {
	    this.subCostCenterCode = subCostCenterCode;
	}

	public BonusType getBonusType() {
	    return bonusType;
	}

	public void setBonusType(BonusType bonusType) {
	    this.bonusType = bonusType;
	}

	public Double getValue() {
	    return value;
	}

	public void setValue(Double value) {
	    this.value = value;
	}

    }

    public boolean isComplete() {
	return getYear() != null && getInstallment() != null && getInputStream() != null;
    }

}
