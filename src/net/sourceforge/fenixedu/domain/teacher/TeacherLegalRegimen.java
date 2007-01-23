/*
 * Created on Dec 12, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.RegimenType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public static final Comparator<TeacherLegalRegimen> TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE)
		.addComparator(new BeanComparator("beginDateYearMonthDay"));
	((ComparatorChain) TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE)
		.addComparator(new BeanComparator("idInternal"));
    }

    public TeacherLegalRegimen(Teacher teacher, Category category, YearMonthDay beginDate,
	    YearMonthDay endDate, Double totalHoursNumber, Integer lessonHoursNumber,
	    LegalRegimenType legalRegimenType, RegimenType regimenType, Integer percentage) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setTeacher(teacher);
	setCategory(category);
	setLegalRegimenType(legalRegimenType);
	setTotalHours(totalHoursNumber);
	setLessonHours(lessonHoursNumber);
	setPercentage(percentage);
	setRegimenType(regimenType);
	setOccupationInterval(beginDate, endDate);
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !this.getBeginDateYearMonthDay().isAfter(endDate)) && (this
		.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(beginDate)));
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    public void delete() {
	super.setCategory(null);
	super.setTeacher(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isEndSituation() {
	return isEndLegalRegimenType(getLegalRegimenType());
    }
    
    public boolean isFunctionAccumulation() {
	return isFunctionsAccumulation(getLegalRegimenType());
    }

    @Override
    public void setBeginDateYearMonthDay(YearMonthDay beginDate) {
	if (!isEndLegalRegimenType(getLegalRegimenType())) {
	    checkTeacherLegalRegimenDatesIntersection(getTeacher(), beginDate, getEndDateYearMonthDay());
	} else {
	    checkBeginDateAndEndDate(beginDate, getEndDateYearMonthDay());
	}
	super.setBeginDateYearMonthDay(beginDate);
    }

    @Override
    public void setEndDateYearMonthDay(YearMonthDay endDate) {
	if (!isEndLegalRegimenType(getLegalRegimenType())) {
	    checkTeacherLegalRegimenDatesIntersection(getTeacher(), getBeginDateYearMonthDay(), endDate);
	} else {
	    checkBeginDateAndEndDate(getBeginDateYearMonthDay(), endDate);
	}
	super.setEndDateYearMonthDay(endDate);
    }

    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	if (!isEndLegalRegimenType(getLegalRegimenType())) {
	    checkTeacherLegalRegimenDatesIntersection(getTeacher(), beginDate, endDate);
	} else {
	    checkBeginDateAndEndDate(beginDate, endDate);
	}
	super.setBeginDateYearMonthDay(beginDate);
	super.setEndDateYearMonthDay(endDate);
    }

    @Override
    public void setCategory(Category category) {
	if (category == null) {
	    throw new DomainException("error.teacherLegalRegimen.no.category");
	}
	super.setCategory(category);
    }

    @Override
    public void setLegalRegimenType(LegalRegimenType legalRegimenType) {
	if (legalRegimenType == null) {
	    throw new DomainException("error.teacherLegalRegimen.no.legalRegimenType");
	}
	super.setLegalRegimenType(legalRegimenType);
    }

    @Override
    public void setTeacher(Teacher teacher) {
	if (teacher == null) {
	    throw new DomainException("error.teacherLegalRegimen.no.teacher");
	}
	super.setTeacher(teacher);
    }

    private void checkTeacherLegalRegimenDatesIntersection(Teacher teacher, YearMonthDay begin,
	    YearMonthDay end) {
	checkBeginDateAndEndDate(begin, end);
	// for (TeacherLegalRegimen legalRegimen :
	// teacher.getAllLegalRegimensWithoutEndSituations()) {
	// if (legalRegimen.checkDatesIntersections(begin, end)) {
	// System.out.println("Teacher Number: " + teacher.getTeacherNumber());
	// throw new
	// DomainException("error.teacherLegalRegimen.dates.intersection");
	// }
	// }
    }

    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.teacherLegalRegimen.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.teacherLegalRegimen.endDateBeforeBeginDate");
	}
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || this.getBeginDateYearMonthDay().isBefore(end)) && (this
		.getEndDateYearMonthDay() == null || this.getEndDateYearMonthDay().isAfter(begin)));
    }

    private boolean isEndLegalRegimenType(LegalRegimenType legalRegimenType) {
	return (legalRegimenType.equals(LegalRegimenType.DEATH)
		|| legalRegimenType.equals(LegalRegimenType.TERM_WORK_CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.EMERITUS)
		|| legalRegimenType.equals(LegalRegimenType.RETIREMENT)
		|| legalRegimenType.equals(LegalRegimenType.RETIREMENT_IN_PROGRESS)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS)
		|| legalRegimenType.equals(LegalRegimenType.CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.DENUNCIATION)
		|| legalRegimenType.equals(LegalRegimenType.IST_OUT_NOMINATION)
		|| legalRegimenType.equals(LegalRegimenType.SERVICE_TURN_OFF)
		|| legalRegimenType.equals(LegalRegimenType.TEMPORARY_SUBSTITUTION_CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.EXONERATION)
		|| legalRegimenType.equals(LegalRegimenType.RESCISSION) 
		|| legalRegimenType.equals(LegalRegimenType.TRANSFERENCE));
    }
    
    public boolean isFunctionsAccumulation(LegalRegimenType legalRegimenType) {
	return legalRegimenType.equals(LegalRegimenType.FUNCTIONS_ACCUMULATION_WITH_LEADING_POSITIONS);
    }
}
