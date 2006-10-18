package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StringNormalizer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota
 * @author jpvl
 * 
 */
public class ExecutionPeriod extends ExecutionPeriod_Base implements Comparable {

    public static final Comparator EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR = new ComparatorChain();                
    static {
        ((ComparatorChain)EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR).addComparator(new BeanComparator("executionYear.year"));
        ((ComparatorChain)EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR).addComparator(new BeanComparator("semester"));            
    }
    
    public ExecutionPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public int compareTo(Object object) {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) object;
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();

        if (getExecutionYear() == executionYear) {
            return getSemester().compareTo(executionPeriod.getSemester());
        } else {
            return getExecutionYear().compareTo(executionYear);
        }
    }

    public String getQualifiedName() {
        return new StringBuilder().append(this.getName()).append(" ").append(
                this.getExecutionYear().getYear()).toString();
    }

    public boolean containsDay(Date day) {
        return ! (this.getBeginDate().after(day) || this.getEndDate().before(day));
    }
    
    public boolean containsDay(YearMonthDay date) {
        return ! (this.getBeginDateYearMonthDay().isAfter(date) || this.getEndDateYearMonthDay().isBefore(date));
    }

    public DateMidnight getThisMonday() {
        final DateTime beginningOfSemester = new DateTime(this.getBeginDate());
        final DateTime endOfSemester = new DateTime(this.getEndDate());

        if (beginningOfSemester.isAfterNow() || endOfSemester.isBeforeNow()) {
            return null;
        }

        final DateMidnight now = new DateMidnight();
        return now.withField(DateTimeFieldType.dayOfWeek(), 1);
    }

    public Interval getCurrentWeek() {
        final DateMidnight thisMonday = getThisMonday();
        return thisMonday == null ? null : new Interval(thisMonday, thisMonday.plusWeeks(1));
    }

    public Interval getPreviousWeek() {
        final DateMidnight thisMonday = getThisMonday();
        return thisMonday == null ? null : new Interval(thisMonday.minusWeeks(1), thisMonday);
    }

    public boolean isAfter(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) > 0;
    }

    public boolean isAfterOrEquals(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) >= 0;
    }

    public boolean isBefore(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) < 0;
    }

    public boolean isBeforeOrEquals(ExecutionPeriod executionPeriod) {
        return this.compareTo(executionPeriod) <= 0;
    }

    public ExecutionCourse getExecutionCourseByInitials(String courseInitials) {
        for (ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (executionCourse.getSigla().equalsIgnoreCase(courseInitials)) {
                return executionCourse;
            }
        }
        return null;
    }

    public List<ExecutionCourse> getExecutionCoursesWithNoCurricularCourses() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (!executionCourse.hasAnyAssociatedCurricularCourses()) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public List<ExecutionCourse> getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
            DegreeCurricularPlan degreeCurricularPlan, CurricularYear curricularYear,
            String name) {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        String normalizedName = (name != null) ? StringNormalizer.normalize(name).toLowerCase().replaceAll("%", ".*") : null; 
        for (ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            String executionCourseName = StringNormalizer.normalize(executionCourse.getNome()).toLowerCase();            
            if (normalizedName != null && executionCourseName.matches(normalizedName)) {
                if (executionCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan)) {
                    result.add(executionCourse);
                }
            }
        }
        return result;
    }
    
    public Collection<MarkSheet> getWebMarkSheetsNotPrinted(){
    	Collection<MarkSheet> markSheets = new HashSet<MarkSheet>(); 
    	for (MarkSheet sheet : this.getMarkSheets()) {
			if(sheet.getSubmittedByTeacher() && !sheet.getPrinted()) {
				markSheets.add(sheet);
			}
		}
    	return markSheets;
    }
    
    public Collection<ExecutionCourse> getExecutionCoursesWithDegreeGradesToSubmit(DegreeCurricularPlan degreeCurricularPlan){
    	final Collection<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>(); 
    	for (final ExecutionCourse executionCourse : this.getAssociatedExecutionCoursesSet()) {
			if(executionCourse.hasAnyDegreeGradeToSubmit(this, degreeCurricularPlan)) {
				executionCourses.add(executionCourse);
			}
		}
    	return executionCourses;
    }
    
    public Collection<MarkSheet> getMarkSheetsToConfirm(DegreeCurricularPlan degreeCurricularPlan){
    	final Collection<MarkSheet> markSheets = new ArrayList<MarkSheet>(); 
    	for (final MarkSheet markSheet : this.getMarkSheetsSet()) {
    		if((degreeCurricularPlan == null || markSheet.getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan))
    				&& markSheet.isNotConfirmed()) {
    			markSheets.add(markSheet);
    		}
		}
    	return markSheets;
    }


    // -------------------------------------------------------------
    // read static methods    
    // -------------------------------------------------------------

    private static transient ExecutionPeriod currentExecutionPeriod = null;
    public static ExecutionPeriod readActualExecutionPeriod() {
        if (currentExecutionPeriod == null
                || currentExecutionPeriod.getRootDomainObject() != RootDomainObject.getInstance()
                || !currentExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
            for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
                if (executionPeriod.getState().equals(PeriodState.CURRENT)) {
                    currentExecutionPeriod = executionPeriod;
                    break;
                }
            }
        }
        return currentExecutionPeriod;
    }

    public static List<ExecutionPeriod> readNotClosedExecutionPeriods() {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (!executionPeriod.getState().equals(PeriodState.CLOSED)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static List<ExecutionPeriod> readPublicExecutionPeriods() {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (!executionPeriod.getState().equals(PeriodState.NOT_OPEN)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static List<ExecutionPeriod> readNotClosedPublicExecutionPeriods() {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (!executionPeriod.getState().equals(PeriodState.NOT_OPEN)
                    && !executionPeriod.getState().equals(PeriodState.CLOSED)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static List<ExecutionPeriod> readExecutionPeriodsInTimePeriod(final Date beginDate,
            final Date endDate) {
        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getBeginDate().before(endDate)
                    && executionPeriod.getEndDate().after(beginDate)) {
                result.add(executionPeriod);
            }
        }
        return result;
    }

    public static ExecutionPeriod readByNameAndExecutionYear(String name, String year) {
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getName().equals(name)
                    && executionPeriod.getExecutionYear().getYear().equals(year)) {
                return executionPeriod;
            }
        }
        return null;
    }

    public static ExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year) {
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance()
                .getExecutionPeriodsSet()) {
            if (executionPeriod.getSemester().equals(semester)
                    && executionPeriod.getExecutionYear().getYear().equals(year)) {
                return executionPeriod;
            }
        }
        return null;
    }

    public static ExecutionPeriod readByDateTime(final DateTime dateTime) {
        final YearMonthDay yearMonthDay = new YearMonthDay(dateTime);
	
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
            if (executionPeriod.containsDay(yearMonthDay)) {
                return executionPeriod;
            }
        }
        
        return null;
    }

    public void checkValidCreditsPeriod(RoleType roleType) {
        if (roleType != RoleType.SCIENTIFIC_COUNCIL) {
            Interval validCreditsPerid = getValidCreditsPeriod(roleType);
            if (validCreditsPerid == null) {
                throw new DomainException("message.invalid.credits.period2");
            } else if (!validCreditsPerid.containsNow()) {
                throw new DomainException("message.invalid.credits.period", validCreditsPerid.getStart()
                        .toString("dd-MM-yyy HH:mm"), validCreditsPerid.getEnd().toString(
                        "dd-MM-yyy HH:mm"));
            }
        }
    }

    public Interval getValidCreditsPeriod(RoleType roleType) {
        switch (roleType) {
        case DEPARTMENT_MEMBER:
            if (getTeacherCreditsPeriodBegin() != null && getTeacherCreditsPeriodEnd() != null) {
                return new Interval(getTeacherCreditsPeriodBegin(), getTeacherCreditsPeriodEnd());
            } else {
                return null;
            }
        case DEPARTMENT_ADMINISTRATIVE_OFFICE:
            if (getDepartmentAdmOfficeCreditsPeriodBegin() != null
                    && getDepartmentAdmOfficeCreditsPeriodEnd() != null) {
                return new Interval(getDepartmentAdmOfficeCreditsPeriodBegin(),
                        getDepartmentAdmOfficeCreditsPeriodEnd());
            } else {
                return null;
            }
        default:
            throw new DomainException("invalid.role.type");
        }
    }
    
    public OccupationPeriod getLessonsPeriod() {
        for (ExecutionDegree executionDegree : getExecutionYear().getExecutionDegreesByType(DegreeType.DEGREE)) {
            if (getSemester() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester();
            } else {
                return executionDegree.getPeriodLessonsSecondSemester();
            }
        }
        return null;
    }

    public String getYear() {
	return getExecutionYear().getYear();
    }
}
