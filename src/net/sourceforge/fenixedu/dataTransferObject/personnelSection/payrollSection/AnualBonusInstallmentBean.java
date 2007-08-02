package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.util.YearMonthList;

public class AnualBonusInstallmentBean implements Serializable {
    private Integer year;

    private List<YearMonth> yearMonths;

    private YearMonth paymentYearMonth;

    public AnualBonusInstallmentBean(Integer year, YearMonthList yearMonths, YearMonth paymentYearMonth) {
	setYearMonths(yearMonths.getYearsMonths());
	setPaymentYearMonth(paymentYearMonth);
	setYear(year);
    }

    public List<YearMonth> getYearMonths() {
	return yearMonths;
    }

    public void setYearMonths(List<YearMonth> yearMonths) {
	this.yearMonths = yearMonths;
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