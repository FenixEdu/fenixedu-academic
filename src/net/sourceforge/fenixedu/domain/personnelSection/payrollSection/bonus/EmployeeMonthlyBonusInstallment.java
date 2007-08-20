package net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Partial;

public class EmployeeMonthlyBonusInstallment extends EmployeeMonthlyBonusInstallment_Base {

    public EmployeeMonthlyBonusInstallment(EmployeeBonusInstallment employeeBonusInstallment,
	    Partial yearMonth, double value) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployeeBonusInstallment(employeeBonusInstallment);
	setPartialYearMonth(yearMonth);
	setValue(value);
    }

    public void edit(double value) {
	setValue(value);
    }

}
