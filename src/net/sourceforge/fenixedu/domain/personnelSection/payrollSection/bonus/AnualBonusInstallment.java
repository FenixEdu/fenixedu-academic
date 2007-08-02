package net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.YearMonthList;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.util.BonusType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.Partial;

public class AnualBonusInstallment extends AnualBonusInstallment_Base {

    public AnualBonusInstallment(Integer year, Partial paymentPartialDate,
            YearMonthList assiudousnessYearMonths) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setYear(year);
        setPaymentPartialDate(paymentPartialDate);
        setAssiduousnessYearMonths(assiudousnessYearMonths);
    }

    public static List<AnualBonusInstallment> readByYear(Integer year) {
        List<AnualBonusInstallment> result = new ArrayList<AnualBonusInstallment>();
        if (year != null) {
            for (AnualBonusInstallment anualBonusInstallment : RootDomainObject.getInstance()
                    .getAnualBonusInstallments()) {
                if (anualBonusInstallment.getYear().equals(year)) {
                    result.add(anualBonusInstallment);
                }
            }
        }
        return result;
    }

    public static AnualBonusInstallment readByYearAndInstallment(Integer year, Integer installment) {
        List<AnualBonusInstallment> anualBonusInstallmentList = readByYear(year);
        Collections.sort(anualBonusInstallmentList, new BeanComparator("paymentPartialDate"));
        if (installment <= anualBonusInstallmentList.size()) {
            return anualBonusInstallmentList.get(installment - 1);
        }
        return null;
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("error.cannotDeleteObject.stillConectedToOtherObjects");
        }
    }

    private boolean canBeDeleted() {
        return getEmployeeBonusInstallments().isEmpty();
    }

    public EmployeeBonusInstallment getEmployeeBonusInstallment(Employee employee, BonusType type) {
        for (EmployeeBonusInstallment employeeBonusInstallment : getEmployeeBonusInstallments()) {
            if (employeeBonusInstallment.getEmployee().equals(employee)
                    && employeeBonusInstallment.getBonusType().equals(type)) {
                return employeeBonusInstallment;
            }
        }
        return null;
    }

}
