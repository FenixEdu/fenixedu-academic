package net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class EmployeeBonusInstallment extends EmployeeBonusInstallment_Base {

    public EmployeeBonusInstallment(AnualBonusInstallment anualBonusInstallment, Employee employee,
	    Double installmentP1Value, Double installmentP2Value, Integer costCenterCode,
	    Integer subCostCenterCode, Integer explorationUnit) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAnualBonusInstallment(anualBonusInstallment);
	setEmployee(employee);
	setInstallmentP1Value(installmentP1Value);
	setInstallmentP2Value(installmentP2Value);
	setCostCenterCode(costCenterCode);
	setSubCostCenterCode(subCostCenterCode);
	setExplorationUnit(explorationUnit);
    }

    public void edit(Double installmentP1Value, Double installmentP2Value, Integer costCenterCode,
	    Integer subCostCenterCode, Integer explorationUnit) {
	setInstallmentP1Value(installmentP1Value);
	setInstallmentP2Value(installmentP2Value);
	setCostCenterCode(costCenterCode);
	setSubCostCenterCode(subCostCenterCode);
	setExplorationUnit(explorationUnit);
    }

    public EmployeeMonthlyBonusInstallment getEmployeeMonthlyBonusInstallment(Employee employee,
	    int monthIndex) {
	for (EmployeeMonthlyBonusInstallment employeeMonthlyBonusInstallment : getEmployeeMonthlyBonusInstallments()) {
	    if (employee.equals(employeeMonthlyBonusInstallment.getEmployeeBonusInstallment())
		    && employeeMonthlyBonusInstallment.getMonth() == monthIndex) {
		return employeeMonthlyBonusInstallment;
	    }
	}
	return null;
    }

    public List<EmployeeMonthlyBonusInstallment> getEmployeeMonthlyBonusInstallmentsOrdered() {
	List<EmployeeMonthlyBonusInstallment> result = new ArrayList<EmployeeMonthlyBonusInstallment>(
		getEmployeeMonthlyBonusInstallments());
	Collections.sort(result, new BeanComparator("month"));
	return result;
    }

}
