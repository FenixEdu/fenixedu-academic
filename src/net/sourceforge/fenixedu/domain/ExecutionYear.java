package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base implements Comparable {

    public static final Comparator<ExecutionYear> EXECUTION_YEAR_COMPARATOR_BY_YEAR = new BeanComparator(
	    "year", Collator.getInstance());

    public ExecutionYear() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Collection<ExecutionDegree> getExecutionDegreesByType(final DegreeType degreeType) {

	return CollectionUtils.select(getExecutionDegrees(), new Predicate() {
	    public boolean evaluate(Object arg0) {
		ExecutionDegree executionDegree = (ExecutionDegree) arg0;
		return executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso() == degreeType;
	    }
	});

    }

    public ExecutionYear getPreviousExecutionYear() {
	ExecutionYear previousExecutionYear = null;
	ExecutionPeriod currentExecutionPeriod = this.getExecutionPeriods().get(0);

	while (currentExecutionPeriod.getPreviousExecutionPeriod() != null) {
	    currentExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

	    if (!currentExecutionPeriod.getExecutionYear().equals(this)) {
		previousExecutionYear = currentExecutionPeriod.getExecutionYear();
		break;
	    }
	}

	return previousExecutionYear;
    }

    public ExecutionYear getNextExecutionYear() {
	for (ExecutionPeriod executionPeriod = this.getExecutionPeriods().get(0); executionPeriod != null; executionPeriod = executionPeriod
		.getNextExecutionPeriod()) {
	    if (executionPeriod.getExecutionYear() != this) {
		return executionPeriod.getExecutionYear();
	    }
	}

	return null;
    }

    public int compareTo(Object object) {
	final ExecutionYear executionYear = (ExecutionYear) object;
	return getYear().compareTo(executionYear.getYear());
    }

    public boolean isAfter(ExecutionYear executionYear) {
	return this.compareTo(executionYear) > 0;
    }

    public boolean isBefore(ExecutionYear executionYear) {
	return this.compareTo(executionYear) < 0;
    }

    public Collection<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(
		getExecutionDegrees());
	Collections.sort(executionDegrees,
		ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
	return executionDegrees;
    }

    public ExecutionPeriod readExecutionPeriodForSemester(Integer semester) {
	for (final ExecutionPeriod executionPeriod : this.getExecutionPeriods()) {
	    if (executionPeriod.getSemester().equals(semester)) {
		return executionPeriod;
	    }
	}
	return null;
    }

    public List<ExecutionPeriod> readNotClosedPublicExecutionPeriods() {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	for (final ExecutionPeriod executionPeriod : this.getExecutionPeriodsSet()) {
	    if (executionPeriod.getState() != PeriodState.NOT_OPEN
		    && executionPeriod.getState() != PeriodState.CLOSED) {
		result.add(executionPeriod);
	    }
	}
	return result;
    }

    public ExecutionPeriod readExecutionPeriodByName(final String name) {
	for (final ExecutionPeriod executionPeriod : getExecutionPeriodsSet()) {
	    if (executionPeriod.getName().equals(name)) {
		return executionPeriod;
	    }
	}
	return null;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static ExecutionYear readCurrentExecutionYear() {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.getState() == PeriodState.CURRENT) {
		return executionYear;
	    }
	}
	return null;
    }

    public static List<ExecutionYear> readOpenExecutionYears() {
	final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.getState() == PeriodState.OPEN) {
		result.add(executionYear);
	    }
	}
	return result;
    }

    public static List<ExecutionYear> readNotClosedExecutionYears() {
	final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.getState() != PeriodState.CLOSED) {
		result.add(executionYear);
	    }
	}
	return result;
    }

    public static ExecutionYear readExecutionYearByName(final String year) {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.getYear().equals(year)) {
		return executionYear;
	    }
	}
	return null;
    }

    public String getNextYearsYearString() {
	final int yearPart1 = Integer.parseInt(getYear().substring(0, 4)) + 1;
	final int yearPart2 = Integer.parseInt(getYear().substring(5, 9)) + 1;
	return Integer.toString(yearPart1) + getYear().charAt(4) + Integer.toString(yearPart2);
    }

    public DegreeInfo getDegreeInfo(Degree degree) {
	for (final DegreeInfo degreeInfo : getDegreeInfos()) {
	    if (degreeInfo.getDegree().equals(degree)) {
		return degreeInfo;
	    }
	}
	return null;
    }

    public boolean containsDate(DateTime dateTime) {
	return new Interval(getBeginDateYearMonthDay().toDateMidnight(), getEndDateYearMonthDay()
		.plusDays(1).toDateMidnight()).contains(dateTime);
    }

    public List<ExecutionDegree> getExecutionDegreesFor(DegreeType degreeType) {
	final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
	    if (executionDegree.getDegreeCurricularPlan().getDegree().getDegreeType() == degreeType) {
		result.add(executionDegree);
	    }
	}
	return result;
    }

    public boolean isCurrent() {
	return this.getState() == PeriodState.CURRENT;
    }
    
    public static ExecutionYear getExecutionYearByDate(YearMonthDay date){
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.containsDate(date.toDateTimeAtMidnight())) {
		return executionYear;
	    }
	}
	return null;
    }
    
}
