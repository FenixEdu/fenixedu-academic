package net.sourceforge.fenixedu.domain.assiduousness;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class ClosedMonth extends ClosedMonth_Base {

    static final public Comparator<ClosedMonth> COMPARATOR_BY_DATE = new Comparator<ClosedMonth>() {
	public int compare(final ClosedMonth o1, final ClosedMonth o2) {
	    return o1.getClosedYearMonth().compareTo(o2.getClosedYearMonth());
	}
    };

    public static final int dayOfMonthToCloseLastMonth = 4;

    public ClosedMonth(Partial closedYearMonth) {
	setRootDomainObject(RootDomainObject.getInstance());
	setClosedForBalance(true);
	setClosedForExtraWork(false);
	setClosedYearMonth(closedYearMonth);
    }

    public ClosedMonth(LocalDate day) {
	setRootDomainObject(RootDomainObject.getInstance());
	setClosedForBalance(true);
	setClosedForExtraWork(false);
	setClosedYearMonth(new Partial().with(DateTimeFieldType.monthOfYear(), day.getMonthOfYear()).with(
		DateTimeFieldType.year(), day.getYear()));
    }

    public static boolean isMonthClosed(Partial yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().equals(yearMonth) && closedMonth.getClosedForBalance()) {
		return true;
	    }
	}
	return false;
    }

    public static boolean isMonthClosedForExtraWork(Partial yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().equals(yearMonth) && closedMonth.getClosedForExtraWork()) {
		return true;
	    }
	}
	return false;
    }

    public static boolean isMonthClosed(LocalDate day) {
	Partial yearMonth = new Partial().with(DateTimeFieldType.monthOfYear(), day.getMonthOfYear()).with(
		DateTimeFieldType.year(), day.getYear());
	return isMonthClosed(yearMonth);
    }

    public static ClosedMonth getLastMonthClosed() {
	return getLastMonthClosed(false);
    }

    public static LocalDate getLastClosedLocalDate() {
	Partial lastClosedYearMonth = getLastMonthClosed(false).getClosedYearMonth();
	LocalDate firstDay = new LocalDate(lastClosedYearMonth.get(DateTimeFieldType.year()), lastClosedYearMonth
		.get(DateTimeFieldType.monthOfYear()), 1);
	return new LocalDate(lastClosedYearMonth.get(DateTimeFieldType.year()), lastClosedYearMonth.get(DateTimeFieldType
		.monthOfYear()), firstDay.dayOfMonth().getMaximumValue());
    }

    public static ClosedMonth getLastMonthClosed(boolean extraWork) {
	ClosedMonth resultClosedMonth = null;
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if ((resultClosedMonth == null || closedMonth.getClosedYearMonth().isAfter(resultClosedMonth.getClosedYearMonth()))
		    && ((!extraWork) || closedMonth.getClosedForExtraWork()) && closedMonth.getClosedForBalance()) {
		resultClosedMonth = closedMonth;
	    }
	}
	return resultClosedMonth;
    }

    public boolean sameClosedMonth(ClosedMonth closedMonth) {
	return closedMonth.getClosedYearMonth().isEqual(getClosedYearMonth());
    }

    public static boolean getCanCloseMonth(Partial yearMonth) {
	LocalDate yearMonthBefore = new LocalDate(yearMonth.get(DateTimeFieldType.year()), yearMonth.get(DateTimeFieldType
		.monthOfYear()), 1).minusMonths(1);
	if (isMonthClosed(yearMonthBefore) || !hasAnyMonthClosed()) {
	    LocalDate yearMonthAfter = new LocalDate(yearMonth.get(DateTimeFieldType.year()), yearMonth.get(DateTimeFieldType
		    .monthOfYear()), dayOfMonthToCloseLastMonth).plusMonths(1);
	    LocalDate now = new LocalDate();
	    if (!now.isBefore(yearMonthAfter)) {
		return true;
	    }
	}
	return false;
    }

    public static boolean getCanOpenMonth(Partial yearMonth) {
	LocalDate today = new LocalDate();
	Partial monthBefore = new Partial().with(DateTimeFieldType.year(), today.getYear()).with(DateTimeFieldType.monthOfYear(),
		today.getMonthOfYear() - 1);
	if (isMonthClosed(yearMonth) && monthBefore.isEqual(yearMonth)) {
	    return true;
	}
	return false;
    }

    public static boolean hasAnyMonthClosed() {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedForBalance()) {
		return true;
	    }
	}
	return false;
    }

    public static ClosedMonth getClosedMonth(LocalDate date) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()) == date.getYear()
		    && closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) == date.getMonthOfYear()
		    && closedMonth.getClosedForBalance()) {
		return closedMonth;
	    }
	}
	return null;
    }

    public static ClosedMonth getClosedMonth(YearMonth yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()) == yearMonth.getYear()
		    && closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) == yearMonth.getNumberOfMonth()
		    && closedMonth.getClosedForBalance()) {
		return closedMonth;
	    }
	}
	return null;
    }

    public static ClosedMonth getOpenClosedMonth(YearMonth yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()) == yearMonth.getYear()
		    && closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) == yearMonth.getNumberOfMonth()
		    && !closedMonth.getClosedForBalance()) {
		return closedMonth;
	    }
	}
	return null;
    }

    public static ClosedMonth getClosedMonth(Partial partial) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()) == partial.get(DateTimeFieldType.year())
		    && closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) == partial.get(DateTimeFieldType
			    .monthOfYear()) && closedMonth.getClosedForBalance()) {
		return closedMonth;
	    }
	}
	return null;
    }

    public LocalDate getClosedMonthFirstDay() {
	return new LocalDate(getClosedYearMonth().get(DateTimeFieldType.year()), getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()), 01);
    }

    public LocalDate getClosedMonthLastDay() {
	LocalDate beginDate = new LocalDate(getClosedYearMonth().get(DateTimeFieldType.year()), getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()), 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	return new LocalDate(getClosedYearMonth().get(DateTimeFieldType.year()), getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()), endDay);
    }

    public void delete() {
	if (canBeDelete()) {
	    removeRootDomainObject();
	    List<AssiduousnessClosedMonth> assiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>(
		    getAssiduousnessClosedMonths());
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessClosedMonths) {
		getAssiduousnessClosedMonths().remove(assiduousnessClosedMonth);
		assiduousnessClosedMonth.delete();
	    }
	    deleteDomainObject();
	} else {
	    throw new DomainException("error.cannot.delete.ClosedMonth");
	}
    }

    private boolean canBeDelete() {
	return !hasAnyClosedMonthDocuments();
    }

    public AssiduousnessClosedMonth getAssiduousnessClosedMonth(AssiduousnessStatusHistory assiduousnessStatusHistory) {
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getAssiduousnessStatusHistory().equals(assiduousnessStatusHistory)) {
		return assiduousnessClosedMonth;
	    }
	}
	return null;
    }

    public List<AssiduousnessClosedMonth> getAssiduousnessClosedMonths(Assiduousness assiduousness) {
	List<AssiduousnessClosedMonth> result = new ArrayList<AssiduousnessClosedMonth>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().equals(assiduousness)) {
		result.add(assiduousnessClosedMonth);
	    }
	}
	return result;
    }

    public ClosedMonthDocument addFile(byte[] file, String fileName, ClosedMonthDocumentType closedMonthDocumentType)
	    throws FileNotFoundException {
	ClosedMonthFile closedMonthFile = new ClosedMonthFile(getClosedYearMonth(), fileName, file);
	return new ClosedMonthDocument(closedMonthFile, this, closedMonthDocumentType);
    }

    public List<ClosedMonthDocument> getClosedMonthDocumentsByType(ClosedMonthDocumentType closedMonthDocumentType) {
	List<ClosedMonthDocument> closedMonthDocuments = new ArrayList<ClosedMonthDocument>();
	for (ClosedMonthDocument closedMonthDocument : getClosedMonthDocuments()) {
	    if (closedMonthDocument.getClosedMonthDocumentType().equals(closedMonthDocumentType)) {
		closedMonthDocuments.add(closedMonthDocument);
	    }
	}
	return closedMonthDocuments;
    }

    public void openMonth() {
	List<AssiduousnessClosedMonth> assiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>(
		getAssiduousnessClosedMonths());
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessClosedMonths) {
	    getAssiduousnessClosedMonths().remove(assiduousnessClosedMonth);
	    assiduousnessClosedMonth.delete();
	}
	setClosedForBalance(Boolean.FALSE);
	setClosedForExtraWork(Boolean.FALSE);
    }

    public EmployeeWorkSheet getEmployeeWorkSheet(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate) {
	List<AssiduousnessStatusHistory> assiduousnessStatusHistories = assiduousness.getStatusBetween(getClosedMonthFirstDay(),
		getClosedMonthLastDay());
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet(assiduousness.getEmployee(), beginDate, endDate);
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
	    AssiduousnessClosedMonth assiduousnessClosedMonth = getAssiduousnessClosedMonth(assiduousnessStatusHistory);
	    employeeWorkSheet.addWorkDaySheets(assiduousnessClosedMonth, beginDate, endDate);
	}
	return employeeWorkSheet;
    }
}
