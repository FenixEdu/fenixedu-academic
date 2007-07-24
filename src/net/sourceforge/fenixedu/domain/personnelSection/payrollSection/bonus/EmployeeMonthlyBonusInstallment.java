package net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;

public class EmployeeMonthlyBonusInstallment extends EmployeeMonthlyBonusInstallment_Base {

    public EmployeeMonthlyBonusInstallment() {
	super();
    }

    public EmployeeMonthlyBonusInstallment(EmployeeBonusInstallment employeeBonusInstallment, int month,
	    double p1Value, double p2Value) {
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployeeBonusInstallment(employeeBonusInstallment);
	setMonth(month);
	setP1Value(p1Value);
	setP2Value(p2Value);
    }

    public void edit(double p1Value, double p2Value) {
	setP1Value(p1Value);
	setP2Value(p2Value);
    }

    public Integer getAbsences() {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(new YearMonth(getEmployeeBonusInstallment()
		.getAnualBonusInstallment().getYear(), getMonth()));
	if (closedMonth != null) {
	    AssiduousnessClosedMonth assiduousnessClosedMonth = closedMonth
		    .getAssiduousnessClosedMonth(getEmployeeBonusInstallment().getEmployee()
			    .getAssiduousness());
	    if (assiduousnessClosedMonth != null) {
		return new Integer(assiduousnessClosedMonth.getMaximumWorkingDays()
			- assiduousnessClosedMonth.getWorkedDays());
	    }
	}
	return null;
    }

}
