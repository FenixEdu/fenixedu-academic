package net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.util.BonusType;

import org.apache.commons.beanutils.BeanComparator;

public class EmployeeBonusInstallment extends EmployeeBonusInstallment_Base {

    public EmployeeBonusInstallment(AnualBonusInstallment anualBonusInstallment, Employee employee,
	    Double installmentValue, BonusType bonusType, Integer costCenterCode,
	    String subCostCenterCode, Integer explorationUnit) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAnualBonusInstallment(anualBonusInstallment);
	setEmployee(employee);
	setValue(installmentValue);
	setBonusType(bonusType);
	setCostCenterCode(costCenterCode);
	setSubCostCenterCode(subCostCenterCode);
	setExplorationUnit(explorationUnit);
    }

    public void edit(Double installmentValue, BonusType bonusType, Integer costCenterCode,
	    String subCostCenterCode, Integer explorationUnit) {
	setValue(installmentValue);
	setBonusType(bonusType);
	setCostCenterCode(costCenterCode);
	setSubCostCenterCode(subCostCenterCode);
	setExplorationUnit(explorationUnit);
    }

    public EmployeeMonthlyBonusInstallment getEmployeeMonthlyBonusInstallment(YearMonth yearMonth) {
	for (EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment : getEmployeeMonthlyBonusInstallments()) {
	    if (new YearMonth(employeeMonthlyBonusInstallment.getPartialYearMonth()).equals(yearMonth)) {
		return employeeMonthlyBonusInstallment;
	    }
	}
	return null;
    }

    public List<EmployeeMonthlyBonusInstallment> getEmployeeMonthlyBonusInstallmentsOrdered() {
	List<EmployeeMonthlyBonusInstallment> result = new ArrayList<EmployeeMonthlyBonusInstallment>(
		getEmployeeMonthlyBonusInstallments());
	Collections.sort(result, new BeanComparator("partialYearMonth"));
	return result;
    }

}
