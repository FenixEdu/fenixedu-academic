package net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.util.PartialList;

import org.joda.time.Partial;

public class AnualBonusInstallmentBean implements Serializable {
    private Integer year;

    private List<YearMonth> yearMonths;

    private YearMonth paymentYearMonth;

    public AnualBonusInstallmentBean(Integer year, PartialList partials, Partial paymentPartial) {
	setYearMonths(partials);
	setPaymentYearMonth(new YearMonth(paymentPartial));
	setYear(year);
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public List<YearMonth> getYearMonths() {
	if (yearMonths == null) {
	    yearMonths = new ArrayList<YearMonth>();
	}
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

    public void setYearMonths(PartialList partials) {
	if (partials != null) {
	    for (Partial partial : partials.getPartials()) {
		getYearMonths().add(new YearMonth(partial));
	    }
	}
    }

    public List<Partial> getPartials() {
	List<Partial> partialList = new ArrayList<Partial>();
	for (YearMonth yearMonth : getYearMonths()) {
	    partialList.add(yearMonth.getPartial());
	}
	return partialList;
    }

    public Partial getPaymentPartial() {
	return paymentYearMonth.getPartial();
    }

}