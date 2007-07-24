package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeMonthlyBonusInstallment;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Month;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionMessage;

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
		for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
		    HSSFRow row = sheet.getRow(rowIndex);
		    HSSFCell cell = row.getCell((short) 0);
		    if (cell != null) {
			try {
			    new Integer(new Double(cell.getNumericCellValue()).intValue());
			    try {
				employeeBonusInstallmentList.add(new EmployeeBonusInstallmentBean(row));
			    } catch (Exception e) {
				return new ActionMessage("error.errorReadingFileLine",
					new Object[] { rowIndex + 1 });
			    }
			} catch (NumberFormatException e) {
			}
		    }
		}
	    } catch (IOException e) {
		System.out.println("ERROOOO->" + e);
		return new ActionMessage("error.errorReadingFile");
	    }
	    List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
		    .readByYear(getYear());
	    int monthsNumber = Month.values().length / anualBonusInstallmentList.size();
	    int beginMonth = (getInstallment().intValue() - 1) * monthsNumber + 1;
	    if (anualBonusInstallmentList.size() == getInstallment()) {
		monthsNumber = monthsNumber + Month.values().length % anualBonusInstallmentList.size();
	    }
	    int endMonth = beginMonth + monthsNumber;
	    Map<Integer, ClosedMonth> closedMonthMap = new HashMap<Integer, ClosedMonth>();
	    for (int monthIndex = beginMonth; monthIndex <= endMonth; monthIndex++) {
		ClosedMonth closedMonth = ClosedMonth
			.getClosedMonth(new YearMonth(getYear(), monthIndex));
		if (closedMonth == null) {
		    return new ActionMessage("error.notAllInstallmentMonthsAreClosed");
		}
		closedMonthMap.put(monthIndex, closedMonth);
	    }

	    AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment
		    .readByYearAndInstallment(getYear(), getInstallment());
	    for (EmployeeBonusInstallmentBean employeeBonusInstallmentBean : employeeBonusInstallmentList) {
		EmployeeBonusInstallment thisEmployeeBonusInstallment = null;
		if (!anualBonusInstallment.getEmployeeBonusInstallments().isEmpty()) {
		    thisEmployeeBonusInstallment = anualBonusInstallment
			    .getEmployeeBonusInstallment(employeeBonusInstallmentBean.getEmployee());
		}
		if (thisEmployeeBonusInstallment == null) {
		    thisEmployeeBonusInstallment = new EmployeeBonusInstallment(anualBonusInstallment,
			    employeeBonusInstallmentBean.getEmployee(), employeeBonusInstallmentBean
				    .getP1(), employeeBonusInstallmentBean.getP2(),
			    employeeBonusInstallmentBean.getCostCenterCode(),
			    employeeBonusInstallmentBean.getSubCostCenterCode(),
			    employeeBonusInstallmentBean.getExplorationUnit());
		} else {
		    thisEmployeeBonusInstallment.edit(employeeBonusInstallmentBean.getP1(),
			    employeeBonusInstallmentBean.getP2(), employeeBonusInstallmentBean
				    .getCostCenterCode(), employeeBonusInstallmentBean
				    .getSubCostCenterCode(), employeeBonusInstallmentBean
				    .getExplorationUnit());
		}
		for (int monthIndex = beginMonth; monthIndex <= endMonth; monthIndex++) {
		    EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment = thisEmployeeBonusInstallment
			    .getEmployeeMonthlyBonusInstallment(employeeBonusInstallmentBean
				    .getEmployee(), monthIndex);

		    double p1Value = thisEmployeeBonusInstallment.getInstallmentP1Value() / monthsNumber;
		    double p2Value = thisEmployeeBonusInstallment.getInstallmentP2Value() / monthsNumber;
		    ClosedMonth closedMonth = closedMonthMap.get(monthIndex);
		    if (closedMonth.getAssiduousnessClosedMonth(thisEmployeeBonusInstallment
			    .getEmployee().getAssiduousness()) == null
			    || closedMonth.getAssiduousnessClosedMonth(
				    thisEmployeeBonusInstallment.getEmployee().getAssiduousness())
				    .getWorkedDays() < miniumWorkedDays) {
			p1Value = 0;
			p2Value = 0;
		    }
		    if (employeeMonthlyBonusInstallment == null) {
			employeeMonthlyBonusInstallment = new EmployeeMonthlyBonusInstallment(
				thisEmployeeBonusInstallment, monthIndex, p1Value, p2Value);
		    } else {
			employeeMonthlyBonusInstallment.edit(p1Value, p2Value);
		    }
		}

	    }
	}
	return null;
    }

    public class EmployeeBonusInstallmentBean implements Serializable {
	private Employee employee;

	private Double p1;

	private Double p2;

	private Integer costCenterCode;

	private Integer subCostCenterCode;

	private Integer explorationUnit;

	public EmployeeBonusInstallmentBean(HSSFRow row) {
	    setEmployee(Employee.readByNumber(new Double(row.getCell((short) 0).getNumericCellValue())
		    .intValue()));
	    setP1(row.getCell((short) 2).getNumericCellValue());
	    setP2(row.getCell((short) 3).getNumericCellValue());
	    setCostCenterCode(new Double(row.getCell((short) 4).getNumericCellValue()).intValue());
	    setSubCostCenterCode(new Double(row.getCell((short) 5).getNumericCellValue()).intValue());
	    setExplorationUnit(new Double(row.getCell((short) 6).getNumericCellValue()).intValue());
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

	public Double getP1() {
	    return p1;
	}

	public void setP1(Double p1) {
	    this.p1 = p1;
	}

	public Double getP2() {
	    return p2;
	}

	public void setP2(Double p2) {
	    this.p2 = p2;
	}

	public Integer getSubCostCenterCode() {
	    return subCostCenterCode;
	}

	public void setSubCostCenterCode(Integer subCostCenterCode) {
	    this.subCostCenterCode = subCostCenterCode;
	}

    }

    public boolean isComplete() {
	return getYear() != null && getInstallment() != null && getInputStream() != null;
    }

}
