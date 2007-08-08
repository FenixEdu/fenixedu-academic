package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.util.Month;

public class YearMonthList implements Serializable {

    private List<YearMonth> yearsMonths;

    public YearMonthList() {
    };

    public YearMonthList(List<YearMonth> yearsMonths) {
	setYearsMonths(yearsMonths);
    }

    public List<YearMonth> getYearsMonths() {
	if (yearsMonths == null) {
	    yearsMonths = new ArrayList<YearMonth>();
	}
	return yearsMonths;
    }

    public List<YearMonth> getSortedYearsMonths() {
	List<YearMonth> result = new ArrayList<YearMonth>(getYearsMonths());
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("year"));
	comparatorChain.addComparator(new BeanComparator("month"));
	Collections.sort(result, comparatorChain);
	return result;
    }

    public void setYearsMonths(List<YearMonth> yearsMonths) {
	this.yearsMonths = yearsMonths;
    }

    public String exportAsString() {
	final StringBuilder result = new StringBuilder();
	for (final YearMonth yearMonth : getYearsMonths()) {
	    if (result.length() != 0) {
		result.append(";");
	    }
	    result.append(yearMonth.getYear() + "," + yearMonth.getMonth());
	}
	return result.toString();
    }

    public static YearMonthList importFromString(String monthsString) {
	YearMonthList result = new YearMonthList();
	for (final String yearMonthString : monthsString.split(";")) {
	    String[] yearMonthArray = yearMonthString.split(",");
	    result.getYearsMonths().add(
		    new YearMonth(new Integer(yearMonthArray[0]), Month.valueOf(yearMonthArray[1])));
	}
	return result;
    }

}
