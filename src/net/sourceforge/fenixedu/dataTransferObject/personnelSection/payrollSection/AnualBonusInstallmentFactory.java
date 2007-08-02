package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.util.YearMonthList;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;

public class AnualBonusInstallmentFactory implements Serializable, FactoryExecutor {
    private Integer year;

    private Integer installmentsNumber;

    private List<AnualBonusInstallmentBean> anualBonusInstallmentBeanList;

    public AnualBonusInstallmentFactory() {
    }

    public Object execute() {
        List<ActionMessage> errors = new ArrayList<ActionMessage>();

        if (getInstallmentsNumber().intValue() > Month.values().length
                || getInstallmentsNumber().intValue() <= 0) {
            return errors.add(new ActionMessage("error.invalidInstallmentsNumber"));
        }
        List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
                .readByYear(getYear());
        if (!getCanEditAnualBonusInstallment(anualBonusInstallmentList)) {
            errors.add(new ActionMessage("error.cantEditAnualBonusInstallment"));
        }

        List<Month> paymentMonths = new ArrayList<Month>();
        List<YearMonth> yearMonthList = new ArrayList<YearMonth>();
        for (AnualBonusInstallmentBean anualBonusInstallmentBean : anualBonusInstallmentBeanList) {
            if (yearMonthList.removeAll(anualBonusInstallmentBean.getYearMonths())) {
                errors.add(new ActionMessage("error.installmentsCantReferToSameMonths"));
            }
            yearMonthList.addAll(anualBonusInstallmentBean.getYearMonths());
            if (anualBonusInstallmentBean.getYearMonths().isEmpty()) {
                errors.add(new ActionMessage("error.installmentsCantBeEmpty",
                        anualBonusInstallmentBean.getPaymentYearMonth().getMonth()));
            }
            if (paymentMonths.contains(anualBonusInstallmentBean.getPaymentYearMonth().getMonth())) {
                errors.add(new ActionMessage("error.diferentInstallmentsCantBePayedAtSameTime"));
            } else {
                paymentMonths.add(anualBonusInstallmentBean.getPaymentYearMonth().getMonth());
            }
        }

        if (errors.isEmpty()) {
            for (AnualBonusInstallment anualBonusInstallment : anualBonusInstallmentList) {
                anualBonusInstallment.delete();
            }
            for (AnualBonusInstallmentBean anualBonusInstallmentBean : anualBonusInstallmentBeanList) {
                new AnualBonusInstallment(getYear(), anualBonusInstallmentBean.getPaymentYearMonth()
                        .getPartial(), new YearMonthList(anualBonusInstallmentBean.getYearMonths()));
            }
        }
        return errors;
    }

    public List<AnualBonusInstallmentBean> getAnualBonusInstallmentBeanList() {
        return anualBonusInstallmentBeanList;
    }

    public void setAnualBonusInstallmentBeanList(
            List<AnualBonusInstallmentBean> anualBonusInstallmentBean) {
        this.anualBonusInstallmentBeanList = anualBonusInstallmentBean;
    }

    public void addAnualBonusInstallmentBeanList(AnualBonusInstallmentBean anualBonusInstallmentBean) {
        if (this.anualBonusInstallmentBeanList == null) {
            this.anualBonusInstallmentBeanList = new ArrayList<AnualBonusInstallmentBean>();
        }
        this.anualBonusInstallmentBeanList.add(anualBonusInstallmentBean);
    }

    public Integer getInstallmentsNumber() {
        return installmentsNumber;
    }

    public void setInstallmentsNumber(Integer installmentsNumber) {
        this.installmentsNumber = installmentsNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void updateAnualBonusInstallment() {

        if (getYear() != null && getInstallmentsNumber() == null) {
            List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
                    .readByYear(getYear());
            if (anualBonusInstallmentList.size() != 0) {
                setInstallmentsNumber(anualBonusInstallmentList.size());
                if (!getCanEditAnualBonusInstallment(anualBonusInstallmentList)) {
                    setAnualBonusInstallmentBeanList();
                }
            }
        } else if (getYear() != null && getInstallmentsNumber() != null) {
            setAnualBonusInstallmentBeanList();
        }

    }

    private void setAnualBonusInstallmentBeanList() {
        int monthsPerInstallment = Month.values().length / getInstallmentsNumber();
        int rest = Month.values().length % getInstallmentsNumber();
        for (int installmentNumber = 1; installmentNumber <= getInstallmentsNumber(); installmentNumber++) {
            YearMonthList yearMonths = getInstallmentMonths(getInstallmentsNumber(), installmentNumber,
                    monthsPerInstallment, rest);
            int paymentMonthIndex = ((YearMonth) yearMonths.getYearsMonths().toArray()[(yearMonths
                    .getYearsMonths().size() - 1)]).getNumberOfMonth();
            if (paymentMonthIndex == 12) {
                paymentMonthIndex = 0;
            }
            YearMonth paymentYearMonth = new YearMonth(getYear(), Month.values()[paymentMonthIndex]);
            AnualBonusInstallmentBean anualBonusInstallmentBean = new AnualBonusInstallmentBean(
                    getYear(), yearMonths, paymentYearMonth);
            addAnualBonusInstallmentBeanList(anualBonusInstallmentBean);
        }
    }

    private YearMonthList getInstallmentMonths(int totalInstallmentsNumber, int installmentNumber,
            int monthsPerInstallment, int rest) {
        YearMonthList yearMonths = new YearMonthList();
        int firstMonth = ((installmentNumber - 1) * monthsPerInstallment);
        if (installmentNumber > (totalInstallmentsNumber - rest)) {
            firstMonth += ((installmentNumber - 1) - (totalInstallmentsNumber - rest));
        }
        int lastMonth = firstMonth + monthsPerInstallment;
        if (installmentNumber > (totalInstallmentsNumber - rest)) {
            lastMonth++;
        }

        for (int index = firstMonth; index < lastMonth; index++) {
            if (index == 0) {
                yearMonths.getYearsMonths().add(
                        new YearMonth(new Integer(getYear() - 1),
                                Month.values()[Month.values().length - 1]));
            } else {
                yearMonths.getYearsMonths().add(new YearMonth(getYear(), Month.values()[index - 1]));
            }
        }
        return yearMonths;
    }

    public boolean getCanEditAnualBonusInstallment(List<AnualBonusInstallment> anualBonusInstallmentList) {
        for (AnualBonusInstallment anualBonusInstallment : anualBonusInstallmentList) {
            if (anualBonusInstallment.getEmployeeBonusInstallmentsCount() != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean getCanEditAnualBonusInstallment() {
        List<AnualBonusInstallment> anualBonusInstallmentList = AnualBonusInstallment
                .readByYear(getYear());
        return getCanEditAnualBonusInstallment(anualBonusInstallmentList);
    }
}