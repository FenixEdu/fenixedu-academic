/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

/**
 * @author Manuel Pinto
 * 
 */
public class Summary extends Summary_Base {

    public static final Comparator<Summary> COMPARATOR_BY_DATE_AND_HOUR = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("summaryDateYearMonthDay"), true);
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("summaryHourHourMinuteSecond"), true);
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public Summary(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber,
	    Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher,
	    Shift shift, Lesson lesson, YearMonthDay date, OldRoom room, Partial hour) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());
	fillSummaryWithInfo(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName,
		teacher, shift, lesson, date, room, hour);
    }

    public void edit(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber,
	    Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher,
	    Shift shift, Lesson lesson, YearMonthDay date, OldRoom room, Partial hour) {

	fillSummaryWithInfo(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName,
		teacher, shift, lesson, date, room, hour);
    }

    private void fillSummaryWithInfo(MultiLanguageString title, MultiLanguageString summaryText,
	    Integer studentsNumber, Boolean isExtraLesson, Professorship professorship,
	    String teacherName, Teacher teacher, Shift shift, Lesson lesson, YearMonthDay date,
	    OldRoom room, Partial hour) {
	
	setShift(shift);
	setSummaryDateYearMonthDay(date);
	setExecutionCourse(shift.getDisciplinaExecucao());
	setTitle(title);
	setSummaryText(summaryText);	
	setIsExtraLesson(isExtraLesson);

	checkSpecialParameters(isExtraLesson, professorship, teacherName, teacher, lesson, hour);	
	checkIfInternalTeacherHasProfessorhipInExecutionCourse(teacher, shift.getDisciplinaExecucao());
	checkIfSummaryDateIsValid(date, shift.getDisciplinaExecucao().getExecutionPeriod(), lesson, isExtraLesson);
			
	setStudentsNumber(studentsNumber);	
	setProfessorship(professorship);
	setTeacherName(teacherName);
	setTeacher(teacher);		
	setLastModifiedDateDateTime(new DateTime());
	setSummaryType(shift.getTipo());
	
	if (isExtraLesson) {
	    HourMinuteSecond hourMinuteSecond = new HourMinuteSecond(hour.get(DateTimeFieldType.hourOfDay()), hour.get(DateTimeFieldType.minuteOfHour()), 0);
	    setSummaryHourHourMinuteSecond(hourMinuteSecond);
	    setRoom(room);
	    removeLesson();
	} else {
	    setLesson(lesson);
	    setRoom(lesson.getSala());
	    setSummaryHourHourMinuteSecond(lesson.getBeginHourMinuteSecond());
	}
    }

    @Override
    public void setIsExtraLesson(Boolean isExtraLesson) {
	if(isExtraLesson == null) {
	    throw new DomainException("error.summary.no.type");
	}
	super.setIsExtraLesson(isExtraLesson);
    }
    
    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
	if(executionCourse == null) {
	    throw new DomainException("error.summary.no.executionCourse");
	}
	super.setExecutionCourse(executionCourse);
    }
    
    @Override
    public void setSummaryDateYearMonthDay(YearMonthDay summaryDateYearMonthDay) {
	if (summaryDateYearMonthDay == null) {
	    throw new DomainException("error.summary.no.date");
	}
	super.setSummaryDateYearMonthDay(summaryDateYearMonthDay);
    }
    
    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.getAllContents().isEmpty()) {
	    throw new DomainException("error.summary.no.title");
	}
	super.setTitle(title);
    }
    
    @Override
    public void setSummaryText(MultiLanguageString summaryText) {
	if (summaryText == null || summaryText.getAllContents().isEmpty()) {
	    throw new DomainException("error.summary.no.summaryText");
	}
	super.setSummaryText(summaryText);
    }
    
    @Override
    public void setShift(Shift shift) {
	if (shift == null) {
	    throw new DomainException("error.summary.no.shift");
	}
	super.setShift(shift);
    }
    
    private void checkIfSummaryDateIsValid(YearMonthDay date, ExecutionPeriod executionPeriod, Lesson lesson, Boolean isExtraLesson) {
	if (!isExtraLesson) {	 	   	    	    
	    Summary summary = lesson.getSummaryByDate(date);
	    if (summary != null && !summary.equals(this)) {
		throw new DomainException("error.summary.already.exists");
	    }
	    if (!lesson.isDateValidToInsertSummary(date)) {
		throw new DomainException("error.summary.no.valid.date.to.lesson");
	    }
	    if (!lesson.isTimeValidToInsertSummary(new HourMinuteSecond(), date)) {
		throw new DomainException("error.summary.no.valid.time.to.lesson");	       
	    }
	} else if (date.isAfter(new YearMonthDay()) || date.isBefore(executionPeriod.getBeginDateYearMonthDay())) {
	    throw new DomainException("error.summary.no.valid.date");
	}
    }

    private void checkIfInternalTeacherHasProfessorhipInExecutionCourse(Teacher teacher, ExecutionCourse executionCourse) {
	if (teacher != null && teacher.getProfessorshipByExecutionCourse(executionCourse) != null) {
	    throw new DomainException("error.summary.teacher.is.executionCourse.professorship");
	}
    }

    private void checkSpecialParameters(Boolean isExtraLesson, Professorship professorship, String teacherName, 
	    Teacher teacher, Lesson lesson, Partial hour) {
		
	if (professorship == null && StringUtils.isEmpty(teacherName) && teacher == null) {
	    throw new DomainException("error.summary.no.teacher");
	}	
	if (isExtraLesson) {
	    if (hour == null) {
		throw new DomainException("error.summary.no.hour");
	    }
	} else {
	    if (lesson == null) {
		throw new DomainException("error.summary.no.lesson");
	    }
	}
    }

    public void delete() {
	super.setExecutionCourse(null);
	super.setShift(null);
	removeProfessorship();
	removeRoom();		
	removeTeacher();
	removeLesson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public String getOrder() {
	StringBuilder stringBuilder = new StringBuilder();
	Lesson lesson = getLesson();
	if (lesson != null) {
	    List<YearMonthDay> allLessonDates = lesson.getAllLessonDates();
	    if (!allLessonDates.isEmpty()) {
		int index = allLessonDates.indexOf(getSummaryDateYearMonthDay());
		if (index != -1) {
		    stringBuilder.append("(").append(index + 1).append("/");
		    return stringBuilder.append(allLessonDates.size()).append(")").toString();
		}
	    }
	}
	return "";
    }

    @Override
    public OldRoom getRoom() {
	if (getLesson() != null) {
	    return (OldRoom) getLesson().getSala();
	}
	return (OldRoom) super.getRoom();
    }

    @Override
    public HourMinuteSecond getSummaryHourHourMinuteSecond() {
	if (getLesson() != null) {
	    return getLesson().getBeginHourMinuteSecond();
	}
	return super.getSummaryHourHourMinuteSecond();
    }

    public void moveFromTeacherToProfessorship(Professorship professorship) {
	if(getTeacher() != null 
		&& professorship != null 
		&& professorship.getExecutionCourse().equals(getExecutionCourse())
		&& professorship.getTeacher().equals(getTeacher())) {
	    	    
	    setTeacher(null);
	    setProfessorship(professorship);
	}
    }
}
