package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.EmployeeMonthlyBonusInstallment;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.util.BonusType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class BonusInstallmentFileBean implements Serializable, FactoryExecutor {

    private static int miniumWorkedDays = 17;

    private Integer year;

    private Integer installment;

    private transient InputStream inputStream;

    private File temporaryFile;

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
	List<ActionMessage> errorMessages = new ArrayList<ActionMessage>();
	if (isComplete()) {
	    HashMap<Employee, EmployeeBonusInstallmentBean> employeeBonusInstallmentMap = new HashMap<Employee, EmployeeBonusInstallmentBean>();
	    List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment.readByYear(getYear());
	    Collections.sort(anualBonusInstallmentList, new BeanComparator("paymentPartialDate"));
	    AnualBonusInstallment choosenAnualBonusInstallment = anualBonusInstallmentList.get(getInstallment() - 1);
	    Map<Integer, ClosedMonth> closedMonthMap = new HashMap<Integer, ClosedMonth>();
	    YearMonthDay begin = new YearMonthDay();
	    YearMonthDay end = begin;
	    for (Partial partial : choosenAnualBonusInstallment.getAssiduousnessPartials().getPartials()) {
		ClosedMonth closedMonth = ClosedMonth.getClosedMonth(partial);
		if (closedMonth == null) {
		    errorMessages.add(new ActionMessage("error.notAllInstallmentMonthsAreClosed"));
		} else {
		    closedMonthMap.put(partial.get(DateTimeFieldType.monthOfYear()), closedMonth);
		    if (begin.isAfter(closedMonth.getClosedMonthFirstDay())) {
			begin = closedMonth.getClosedMonthFirstDay();
		    }
		    if (end.isBefore(closedMonth.getClosedMonthLastDay())) {
			end = closedMonth.getClosedMonthLastDay();
		    }
		}
	    }

	    FileInputStream fileInputStream = null;
	    try {
		fileInputStream = new FileInputStream(getTemporaryFile());
		POIFSFileSystem fs = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
		    HSSFRow row = sheet.getRow(rowIndex);
		    try {
			String bonusType = row.getCell((short) 1).getStringCellValue();
			if (bonusType != null) {
			    String bonusTypeFormatted = bonusType.trim().toUpperCase();
			    if (bonusTypeFormatted.equals("P1") || bonusTypeFormatted.equals("P2")) {
				EmployeeBonusInstallmentBean employeeBonusInstallmentBean = new EmployeeBonusInstallmentBean(row,
					bonusTypeFormatted);
				if (employeeBonusInstallmentBean.getEmployee().getAssiduousness() == null
					|| employeeBonusInstallmentBean.getEmployee().getAssiduousness()
						.getAssiduousnessStatusHistories().size() == 0) {
				    errorMessages.add(new ActionMessage("warning.bonus.employeeWithNoAssiduousness",
					    employeeBonusInstallmentBean.getEmployee().getEmployeeNumber().toString()));
				} else {
				    if (employeeBonusInstallmentBean.getEmployee().getAssiduousness()
					    .getLastAssiduousnessStatusHistoryBetween(begin, end) == null) {
					errorMessages.add(new ActionMessage("warning.bonus.employeeWithNoAssiduousness",
						employeeBonusInstallmentBean.getEmployee().getEmployeeNumber().toString()));
				    }
				}
				EmployeeBonusInstallmentBean old = employeeBonusInstallmentMap.get(employeeBonusInstallmentBean
					.getEmployee());
				if (old != null) {
				    errorMessages.add(new ActionMessage("error.duplicatedBonus", employeeBonusInstallmentBean
					    .getEmployee().getEmployeeNumber().toString()));
				}
				employeeBonusInstallmentMap.put(employeeBonusInstallmentBean.getEmployee(),
					employeeBonusInstallmentBean);
			    } else {
				errorMessages.add(new ActionMessage("error.errorReadingFileLine", new Object[] { rowIndex + 1 }));
			    }
			} else {
			    errorMessages.add(new ActionMessage("error.errorReadingFileLine", new Object[] { rowIndex + 1 }));
			}
		    } catch (Exception e) {
			errorMessages.add(new ActionMessage("error.errorReadingFileLine", new Object[] { rowIndex + 1 }));
		    }
		}
	    } catch (IOException e) {
		errorMessages.add(new ActionMessage("error.errorReadingFile"));
	    } finally {
		try {
		    if (fileInputStream != null) {
			fileInputStream.close();
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }

	    if (errorMessages.size() != 0) {
		return errorMessages;
	    }

	    AnualBonusInstallment anualBonusInstallment = AnualBonusInstallment.readByYearAndInstallment(getYear(),
		    getInstallment());
	    for (EmployeeBonusInstallmentBean employeeBonusInstallmentBean : employeeBonusInstallmentMap.values()) {
		EmployeeBonusInstallment thisEmployeeBonusInstallment = null;
		if (!anualBonusInstallment.getEmployeeBonusInstallments().isEmpty()) {
		    thisEmployeeBonusInstallment = anualBonusInstallment.getEmployeeBonusInstallment(employeeBonusInstallmentBean
			    .getEmployee(), employeeBonusInstallmentBean.bonusType);
		}
		if (thisEmployeeBonusInstallment == null) {
		    thisEmployeeBonusInstallment = new EmployeeBonusInstallment(anualBonusInstallment,
			    employeeBonusInstallmentBean.getEmployee(), employeeBonusInstallmentBean.getValue(),
			    employeeBonusInstallmentBean.getBonusType(), employeeBonusInstallmentBean.getCostCenterCode(),
			    employeeBonusInstallmentBean.getSubCostCenterCode(), employeeBonusInstallmentBean
				    .getExplorationUnit());
		} else {
		    thisEmployeeBonusInstallment.edit(employeeBonusInstallmentBean.getValue(), employeeBonusInstallmentBean
			    .getBonusType(), employeeBonusInstallmentBean.getCostCenterCode(), employeeBonusInstallmentBean
			    .getSubCostCenterCode(), employeeBonusInstallmentBean.getExplorationUnit());
		}

		double value = formatDouble(thisEmployeeBonusInstallment.getValue()
			/ choosenAnualBonusInstallment.getAssiduousnessPartials().getPartials().size());
		double lastValue = formatDouble(thisEmployeeBonusInstallment.getValue()
			- (value * (choosenAnualBonusInstallment.getAssiduousnessPartials().getPartials().size() - 1)));

		for (int partialIndex = 0; partialIndex < choosenAnualBonusInstallment.getAssiduousnessPartials().getPartials()
			.size(); partialIndex++) {
		    Partial partial = choosenAnualBonusInstallment.getAssiduousnessPartials().getPartials().get(partialIndex);

		    double thisValue = value;
		    if (partialIndex == choosenAnualBonusInstallment.getAssiduousnessPartials().getPartials().size() - 1) {
			thisValue = lastValue;
		    }
		    EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment = thisEmployeeBonusInstallment
			    .getEmployeeMonthlyBonusInstallment(partial);
		    ClosedMonth closedMonth = closedMonthMap.get(partial.get(DateTimeFieldType.monthOfYear()));
		    List<AssiduousnessClosedMonth> assiduousnessClosedMonthList = closedMonth
			    .getAssiduousnessClosedMonths(thisEmployeeBonusInstallment.getEmployee().getAssiduousness());
		    int workedDays = 0;
		    for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessClosedMonthList) {
			workedDays += assiduousnessClosedMonth.getWorkedDaysWithBonusDaysDiscount();
		    }

		    if (workedDays < miniumWorkedDays) {
			thisValue = -thisValue;
		    }
		    if (employeeMonthlyBonusInstallment == null) {
			DateTimeFieldType[] dateTimeFields = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
			int[] fieldValues = { partial.get(DateTimeFieldType.year()), partial.get(DateTimeFieldType.monthOfYear()) };
			employeeMonthlyBonusInstallment = new EmployeeMonthlyBonusInstallment(thisEmployeeBonusInstallment,
				new Partial(dateTimeFields, fieldValues), thisValue);
		    } else {
			employeeMonthlyBonusInstallment.edit(thisValue);
		    }
		}
	    }
	}
	return null;
    }

    private Double formatDouble(Double value) {
	Double result = value;
	String pattern = "0.00";
	try {
	    DecimalFormat simpledf = new DecimalFormat(pattern);
	    result = new Double(simpledf.parse(simpledf.format(value)).toString());
	} catch (ParseException e) {
	}
	return result;
    }

    public class EmployeeBonusInstallmentBean implements Serializable {
	private Employee employee;

	private Double value;

	private BonusType bonusType;

	private Integer costCenterCode;

	private String subCostCenterCode;

	private Integer explorationUnit;

	public EmployeeBonusInstallmentBean(HSSFRow row, String bonusType) {
	    setBonusType(getBonusTypeEnum(bonusType));
	    setEmployee(Employee.readByNumber(getDoubleFromCell(row.getCell((short) 0)).intValue()));
	    setValue(formatDouble(getDoubleFromCell(row.getCell((short) 2))));
	    setCostCenterCode(getDoubleFromCell(row.getCell((short) 3)).intValue());
	    if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_STRING) {
		setSubCostCenterCode(row.getCell((short) 4).getStringCellValue());
	    } else {
		setSubCostCenterCode(new Double(row.getCell((short) 4).getNumericCellValue()).toString());
	    }
	    setExplorationUnit(getDoubleFromCell(row.getCell((short) 5)).intValue());
	}

	private Double getDoubleFromCell(HSSFCell cell) {
	    try {
		return new Double(cell.getNumericCellValue());
	    } catch (Exception e) {
		return new Double(cell.getStringCellValue());
	    }
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

	public String getSubCostCenterCode() {
	    return subCostCenterCode;
	}

	public void setSubCostCenterCode(String subCostCenterCode) {
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

    public File getTemporaryFile() {
	if (temporaryFile == null) {
	    try {
		temporaryFile = inputStream != null ? FileUtils.copyToTemporaryFile(inputStream) : null;
	    } catch (IOException exception) {
		temporaryFile = null;
	    }
	}
	return temporaryFile;
    }

    public void setTemporaryFile(File temporaryFile) {
	this.temporaryFile = temporaryFile;
    }

}
