package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistribution;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base implements Comparable<ExecutionYear> {

    static {
	ExecutionPeriodExecutionYear.addListener(new ExecutionPeriodExecutionYearListener());
    }

    static final public Comparator<ExecutionYear> COMPARATOR_BY_YEAR = new Comparator<ExecutionYear>() {
	public int compare(ExecutionYear o1, ExecutionYear o2) {
	    return o1.getYear().compareTo(o2.getYear());
	}
    };

    static final public Comparator<ExecutionYear> REVERSE_COMPARATOR_BY_YEAR = new Comparator<ExecutionYear>() {
	public int compare(ExecutionYear o1, ExecutionYear o2) {
	    return -COMPARATOR_BY_YEAR.compare(o1, o2);
	}
    };

    private ExecutionYear() {
	super();
	setRootDomainObjectForExecutionYear(RootDomainObject.getInstance());
    }

    public ExecutionYear(AcademicInterval academicInterval, String year) {
	this();
	setAcademicInterval(academicInterval);
	setBeginDateYearMonthDay(academicInterval.getBeginYearMonthDayWithoutChronology());
	setEndDateYearMonthDay(academicInterval.getEndYearMonthDayWithoutChronology());
	setYear(year);
    }

    public String getYear() {
	return getName();
    }

    public void setYear(String year) {
	if (year == null || StringUtils.isEmpty(year.trim())) {
	    throw new DomainException("error.ExecutionYear.empty.year");
	}
	super.setName(year);
    }

    public Collection<ExecutionDegree> getExecutionDegreesByType(final DegreeType degreeType) {
	return CollectionUtils.select(getExecutionDegrees(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		ExecutionDegree executionDegree = (ExecutionDegree) arg0;
		return executionDegree.getDegreeCurricularPlan().getDegreeType() == degreeType;
	    }
	});
    }

    public ExecutionYear getNextExecutionYear() {
	AcademicYearCE year = getAcademicInterval().plusYear(1);
	return getExecutionYear(year);
    }

    public ExecutionYear getPreviousExecutionYear() {
	AcademicYearCE year = getAcademicInterval().minusYear(1);
	return getExecutionYear(year);
    }

    public ExecutionYear getPreviousExecutionYear(final Integer previousCivilYears) {
	if (previousCivilYears >= 0) {
	    AcademicYearCE year = getAcademicInterval().minusYear(previousCivilYears);
	    return getExecutionYear(year);
	}
	return null;
    }

    public boolean hasPreviousExecutionYear() {
	return getPreviousExecutionYear() != null;
    }

    public boolean hasNextExecutionYear() {
	return getNextExecutionYear() != null;
    }

    public int compareTo(ExecutionYear object) {
	if (object == null) {
	    return 1;
	}
	return getAcademicInterval().getStartDateTimeWithoutChronology().compareTo(
		object.getAcademicInterval().getStartDateTimeWithoutChronology());
    }

    public boolean isAfter(final ExecutionYear executionYear) {
	return this.compareTo(executionYear) > 0;
    }

    public boolean isAfterOrEquals(final ExecutionYear executionYear) {
	return this.compareTo(executionYear) >= 0;
    }

    public boolean isBefore(final ExecutionYear executionYear) {
	return this.compareTo(executionYear) < 0;
    }

    public boolean isBeforeOrEquals(final ExecutionYear executionYear) {
	return this.compareTo(executionYear) <= 0;
    }

    public Collection<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(getExecutionDegrees());
	Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
	return executionDegrees;
    }

    public ExecutionSemester readExecutionPeriodForSemester(final Integer semester) {
	for (final ExecutionSemester executionSemester : getExecutionPeriods()) {
	    if (executionSemester.isFor(semester)) {
		return executionSemester;
	    }
	}
	return null;
    }

    public ExecutionSemester getFirstExecutionPeriod() {
	return (ExecutionSemester) Collections.min(this.getExecutionPeriods(),
		ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
    }

    public ExecutionSemester getLastExecutionPeriod() {
	return (ExecutionSemester) Collections.max(this.getExecutionPeriods(),
		ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
    }

    public List<ExecutionSemester> readNotClosedPublicExecutionPeriods() {
	final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	for (final ExecutionSemester executionSemester : getExecutionPeriodsSet()) {
	    if (!executionSemester.isClosed() && !executionSemester.isNotOpen()) {
		result.add(executionSemester);
	    }
	}
	return result;
    }

    public ExecutionSemester readExecutionPeriodByName(final String name) {
	for (final ExecutionSemester executionSemester : getExecutionPeriodsSet()) {
	    if (executionSemester.getName().equals(name)) {
		return executionSemester;
	    }
	}
	return null;
    }

    public String getNextYearsYearString() {
	final int yearPart1 = Integer.parseInt(getYear().substring(0, 4)) + 1;
	final int yearPart2 = Integer.parseInt(getYear().substring(5, 9)) + 1;
	return Integer.toString(yearPart1) + getYear().charAt(4) + Integer.toString(yearPart2);
    }

    public DegreeInfo getDegreeInfo(final Degree degree) {
	for (final DegreeInfo degreeInfo : getDegreeInfos()) {
	    if (degreeInfo.getDegree().equals(degree)) {
		return degreeInfo;
	    }
	}
	return null;
    }

    public boolean containsDate(final DateTime dateTime) {
	final DateMidnight begin = getBeginDateYearMonthDay().toDateMidnight();
	final DateMidnight end = getEndDateYearMonthDay().plusDays(1).toDateMidnight();
	return new Interval(begin, end).contains(dateTime);
    }

    public List<ExecutionDegree> getExecutionDegreesFor(final DegreeType... degreeTypes) {
	final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
	for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
	    if (degreeTypesList.contains(executionDegree.getDegreeCurricularPlan().getDegree().getDegreeType())) {
		result.add(executionDegree);
	    }
	}
	return result;
    }

    public boolean isCurrent() {
	return this.getState().equals(PeriodState.CURRENT);
    }

    public boolean isOpen() {
	return getState().equals(PeriodState.OPEN);
    }

    public boolean isClosed() {
	return getState().equals(PeriodState.CLOSED);
    }

    public boolean isFor(final String year) {
	return getYear().equals(year);
    }

    public ShiftDistribution createShiftDistribution() {
	return new ShiftDistribution(this);
    }

    public void delete() {
	if (!getExecutionDegreesSet().isEmpty()) {
	    throw new Error("cannot.delete.execution.year.because.execution.degrees.exist");
	}
	for (; hasAnyExecutionPeriods(); getExecutionPeriodsSet().iterator().next().delete())
	    ;

	removeRootDomainObject();
	deleteDomainObject();
    }

    public boolean belongsToCivilYear(int civilYear) {
	return (getBeginDateYearMonthDay().getYear() == civilYear || getEndDateYearMonthDay().getYear() == civilYear);
    }

    public boolean belongsToCivilYearInterval(int beginCivilYear, int endCivilYear) {
	for (int year = beginCivilYear; year <= endCivilYear; year++) {
	    if (belongsToCivilYear(year)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isBeforeCivilYear(int civilYear) {
	return getEndDateYearMonthDay().getYear() < civilYear;
    }

    public boolean isAfterCivilYear(int civilYear) {
	return getBeginDateYearMonthDay().getYear() > civilYear;
    }

    public Collection<DegreeCurricularPlan> getDegreeCurricularPlans() {
	final Collection<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
	for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
	    result.add(executionDegree.getDegreeCurricularPlan());
	}
	return result;
    }

    private static class ExecutionPeriodExecutionYearListener extends RelationAdapter<ExecutionYear, ExecutionSemester> {
	@Override
	public void beforeAdd(ExecutionYear executionYear, ExecutionSemester executionSemester) {
	    if (executionYear != null && executionSemester != null && executionYear.getExecutionPeriodsCount() == 2) {
		throw new DomainException("error.ExecutionYear.exceeded.number.of.executionPeriods", executionYear.getYear());
	    }
	}
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static ExecutionYear getExecutionYear(AcademicYearCE entry) {
	if (entry != null) {
	    entry = (AcademicYearCE) entry.getOriginalTemplateEntry();
	    for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
		if (executionYear.getAcademicInterval().getAcademicCalendarEntry().equals(entry)) {
		    return executionYear;
		}
	    }
	}
	return null;
    }

    static public ExecutionYear readCurrentExecutionYear() {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.isCurrent()) {
		return executionYear;
	    }
	}
	return null;
    }

    static public List<ExecutionYear> readOpenExecutionYears() {
	final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.isOpen()) {
		result.add(executionYear);
	    }
	}
	return result;
    }

    static public List<ExecutionYear> readNotClosedExecutionYears() {
	final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (!executionYear.isClosed()) {
		result.add(executionYear);
	    }
	}
	return result;
    }

    static public ExecutionYear readExecutionYearByName(final String year) {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.isFor(year)) {
		return executionYear;
	    }
	}
	return null;
    }

    static public ExecutionYear readFirstBolonhaExecutionYear() {
	return ExecutionSemester.readFirstBolonhaExecutionPeriod().getExecutionYear();
    }

    public static class ExecutionYearSearchCache {
	private Map<Integer, Set<ExecutionYear>> map = new HashMap<Integer, Set<ExecutionYear>>();

	public ExecutionYear findByDateTime(final DateTime dateTime) {
	    final Integer year = Integer.valueOf(dateTime.getYear());
	    Set<ExecutionYear> executionYears = map.get(year);
	    if (executionYears == null || executionYears.isEmpty()) {
		for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
		    add(executionYear);
		}
		executionYears = map.get(year);
	    }
	    if (executionYears != null) {
		for (final ExecutionYear executionYear : executionYears) {
		    if (executionYear.containsDate(dateTime)) {
			return executionYear;
		    }
		}
	    }
	    return null;
	}

	private void add(final ExecutionYear executionYear) {
	    final Integer year1 = executionYear.getBeginDateYearMonthDay().getYear();
	    final Integer year2 = executionYear.getEndDateYearMonthDay().getYear();
	    add(executionYear, year1);
	    add(executionYear, year2);
	}

	private void add(final ExecutionYear executionYear, final Integer year) {
	    Set<ExecutionYear> executionYears = map.get(year);
	    if (executionYears == null) {
		executionYears = new HashSet<ExecutionYear>();
		map.put(year, executionYears);
	    }
	    executionYears.add(executionYear);
	}
    }

    private static final ExecutionYearSearchCache executionYearSearchCache = new ExecutionYearSearchCache();

    static public ExecutionYear readByDateTime(final DateTime dateTime) {
	return executionYearSearchCache.findByDateTime(dateTime);
    }

    public static ExecutionYear readBy(final YearMonthDay begin, YearMonthDay end) {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.getBeginDateYearMonthDay().isEqual(begin) && executionYear.getEndDateYearMonthDay().isEqual(end)) {
		return executionYear;
	    }
	}
	return null;
    }

    static public ExecutionYear getExecutionYearByDate(final YearMonthDay date) {
	return readByDateTime(date.toDateTimeAtMidnight());
    }

    static public List<ExecutionYear> readExecutionYearsByCivilYear(int civilYear) {
	final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
	    if (executionYear.belongsToCivilYear(civilYear)) {
		result.add(executionYear);
	    }
	}

	return result;

    }

    static public ExecutionYear readFirstExecutionYear() {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
	    if (!executionYear.hasPreviousExecutionYear()) {
		return executionYear;
	    }
	}
	return null;
    }

    static public ExecutionYear readLastExecutionYear() {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
	    if (!executionYear.hasNextExecutionYear()) {
		return executionYear;
	    }
	}
	return null;
    }

    private Set<AccountingTransaction> getPaymentsFor(final Class<? extends AnnualEvent> eventClass) {
	final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
	for (final AnnualEvent each : getAnnualEvents()) {
	    if (eventClass.equals(each.getClass()) && !each.isCancelled()) {
		result.addAll(each.getNonAdjustingTransactions());
	    }
	}

	return result;
    }

    public Set<AccountingTransaction> getDFAGratuityPayments() {
	return getPaymentsFor(DfaGratuityEvent.class);
    }
}
