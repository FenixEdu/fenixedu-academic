package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.util.YearMonthList;

public class AnualBonusInstallmentBean implements Serializable {
    private Integer year;

    private List<YearMonth> months;

    private YearMonth paymentYearMonth;

    public AnualBonusInstallmentBean(Integer year, YearMonthList months, YearMonth paymentYearMonth) {
	setMonths(months.getYearsMonths());
	setPaymentYearMonth(paymentYearMonth);
	setYear(year);
    }

    public List<YearMonth> getMonths() {
	return months;
    }

    public void setMonths(List<YearMonth> months) {
	this.months = months;
    }

    public YearMonth getPaymentYearMonth() {
	return paymentYearMonth;
    }

    public void setPaymentYearMonth(YearMonth paymentYearMonth) {
	this.paymentYearMonth = paymentYearMonth;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

}