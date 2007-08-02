package net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;

import org.joda.time.DateTimeFieldType;
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

    public Integer getAbsences() {
        ClosedMonth closedMonth = ClosedMonth.getClosedMonth(new YearMonth(getPartialYearMonth().get(
                DateTimeFieldType.year()), getPartialYearMonth().get(DateTimeFieldType.monthOfYear())));
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
