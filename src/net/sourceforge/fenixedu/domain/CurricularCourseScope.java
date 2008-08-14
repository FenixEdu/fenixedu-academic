package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class CurricularCourseScope extends CurricularCourseScope_Base {

    public static Comparator<CurricularCourseScope> CURRICULAR_COURSE_NAME_COMPARATOR = new ComparatorChain();
    static {
	((ComparatorChain) CURRICULAR_COURSE_NAME_COMPARATOR).addComparator(new BeanComparator("curricularCourse.name", Collator
		.getInstance()));
	((ComparatorChain) CURRICULAR_COURSE_NAME_COMPARATOR).addComparator(new BeanComparator("curricularCourse.idInternal"));
    }

    public CurricularCourseScope() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CurricularCourseScope(Branch branch, CurricularCourse curricularCourse, CurricularSemester curricularSemester,
	    Calendar beginDate, Calendar endDate, String Annotation) {
	this();
	// check that there isn't another scope active with the same curricular
	// course, branch and semester

	if (curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(curricularSemester, branch)) {
	    throw new DomainException("error.curricular.course.scope.conflict.creation");
	}

	setBranch(branch);
	setCurricularCourse(curricularCourse);
	setCurricularSemester(curricularSemester);

	setBeginDate(beginDate);
	setEndDate(endDate);
	setAnotation(Annotation);
    }

    public Calendar getBeginDate() {
	if (this.getBegin() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getBegin());
	    return result;
	}
	return null;
    }

    public void setBeginDate(Calendar beginDate) {
	if (beginDate != null) {
	    this.setBegin(beginDate.getTime());
	} else {
	    this.setBegin(null);
	}
    }

    public Calendar getEndDate() {
	if (this.getEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnd());
	    return result;
	}
	return null;
    }

    public void setEndDate(Calendar endDate) {
	if (endDate != null) {
	    this.setEnd(endDate.getTime());
	} else {
	    this.setEnd(null);
	}
    }

    public Boolean isActive() {
	return isActive(new Date());
    }

    public boolean getActive() {
	return this.isActive();
    }

    public Boolean canBeDeleted() {
	return !hasAnyAssociatedWrittenEvaluations();
    }

    public void edit(Branch branch, CurricularSemester curricularSemester, Calendar beginDate, Calendar endDate, String Annotation) {

	setBranch(branch);
	setCurricularSemester(curricularSemester);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setAnotation(Annotation);
    }

    public void end(Calendar endDate) {
	setEndDate(endDate);
    }

    public void delete() throws DomainException {
	if (canBeDeleted()) {
	    removeCurricularSemester();
	    removeCurricularCourse();
	    removeBranch();

	    removeRootDomainObject();
	    super.deleteDomainObject();
	} else {
	    throw new DomainException("error.curricular.course.scope.has.written.evaluations");
	}
    }

    public Boolean isActive(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	CalendarDateComparator calendarDateComparator = new CalendarDateComparator();
	Boolean result = Boolean.FALSE;
	if (calendarDateComparator.compare(getBeginDate(), calendar) <= 0) {
	    if (getEndDate() == null || calendarDateComparator.compare(getEndDate(), calendar) >= 0) {
		result = Boolean.TRUE;
	    }
	}
	return result;
    }

    public boolean intersects(final Date begin, final Date end) {
	return intersects(YearMonthDay.fromDateFields(begin), YearMonthDay.fromDateFields(end));
    }

    public boolean intersects(final YearMonthDay begin, final YearMonthDay end) {
	return !getBeginYearMonthDay().isAfter(end) && (getEndYearMonthDay() == null || !getEndYearMonthDay().isBefore(begin));
    }

    public boolean isActiveForExecutionPeriod(final ExecutionSemester executionSemester) {
	return intersects(executionSemester.getBeginDateYearMonthDay(), executionSemester.getEndDateYearMonthDay())
		&& executionSemester.getSemester().equals(getCurricularSemester().getSemester());
    }

    public boolean isActiveForExecutionYear(final ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
	    if (isActiveForExecutionPeriod(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    private DegreeModuleScopeCurricularCourseScope degreeModuleScopeCurricularCourseScope = null;

    private synchronized void initDegreeModuleScopeCurricularCourseScope() {
	if (degreeModuleScopeCurricularCourseScope == null) {
	    degreeModuleScopeCurricularCourseScope = new DegreeModuleScopeCurricularCourseScope(this);
	}
    }

    public DegreeModuleScopeCurricularCourseScope getDegreeModuleScopeCurricularCourseScope() {
	if (degreeModuleScopeCurricularCourseScope == null) {
	    initDegreeModuleScopeCurricularCourseScope();
	}
	return degreeModuleScopeCurricularCourseScope;
    }

    public boolean hasEndYearMonthDay() {
	return getEndYearMonthDay() != null;
    }

    public class DegreeModuleScopeCurricularCourseScope extends DegreeModuleScope {

	private final CurricularCourseScope curricularCourseScope;

	private DegreeModuleScopeCurricularCourseScope(CurricularCourseScope curricularCourseScope) {
	    this.curricularCourseScope = curricularCourseScope;
	}

	@Override
	public Integer getIdInternal() {
	    return curricularCourseScope.getIdInternal();
	}

	@Override
	public Integer getCurricularSemester() {
	    return curricularCourseScope.getCurricularSemester().getSemester();
	}

	@Override
	public Integer getCurricularYear() {
	    return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
	}

	@Override
	public String getBranch() {
	    return curricularCourseScope.getBranch() == null ? "" : curricularCourseScope.getBranch().getName();
	}

	public CurricularCourseScope getCurricularCourseScope() {
	    return curricularCourseScope;
	}

	@Override
	public boolean isActiveForExecutionPeriod(final ExecutionSemester executionSemester) {
	    return curricularCourseScope.isActiveForExecutionPeriod(executionSemester);
	}

	@Override
	public CurricularCourse getCurricularCourse() {
	    return curricularCourseScope.getCurricularCourse();
	}

	@Override
	public String getAnotation() {
	    return curricularCourseScope.getAnotation();
	}

	@Override
	public String getClassName() {
	    return curricularCourseScope.getClass().getName();
	}

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof DegreeModuleScopeCurricularCourseScope)) {
		return false;
	    }
	    return curricularCourseScope.equals(((DegreeModuleScopeCurricularCourseScope) obj).getCurricularCourseScope());
	}

	@Override
	public int hashCode() {
	    return curricularCourseScope.hashCode();
	}
    }

}
