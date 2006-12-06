package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class NextPossibleSummaryLessonsAndDatesBean implements Serializable {

    public static final Comparator<NextPossibleSummaryLessonsAndDatesBean> COMPARATOR_BY_DATE_AND_HOUR = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("date"), true);
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("lesson.beginHourMinuteSecond"), true);
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("lesson.idInternal"));
    }
    
    private DomainReference<Lesson> lessonReference;

    private YearMonthDay date;

    private Integer studentsNumber;

    public NextPossibleSummaryLessonsAndDatesBean(Lesson lesson, YearMonthDay date) {
	setLesson(lesson);
	setDate(date);
    }

    public Integer getStudentsNumber() {
	return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
	this.studentsNumber = studentsNumber;
    }

    public YearMonthDay getDate() {
	return date;
    }

    public void setDate(YearMonthDay date) {
	this.date = date;
    }

    public Lesson getLesson() {
	return (this.lessonReference != null) ? this.lessonReference.getObject() : null;
    }

    public void setLesson(Lesson lesson) {
	this.lessonReference = (lesson != null) ? new DomainReference<Lesson>(lesson) : null;
    }

    public boolean getWrittenSummary() {
	return getLesson().getSummaryByDate(getDate()) != null;
    }

    private static final DateTimeFormatter monthOfYearTextFormatter = new DateTimeFormatterBuilder().appendMonthOfYearText()
	    .toFormatter();

    public String getMonthString() {	
	return getDate().toString(monthOfYearTextFormatter);
    }

    public boolean getWithoutSummary() {
	Lesson lesson = getLesson();
	if (lesson.isDateValidToInsertSummary(getDate())
		&& lesson.isTimeValidToInsertSummary(new HourMinuteSecond(), getDate())
		&& !getWrittenSummary()) {
	    return true;
	}
	return false;
    }

    public String getCheckBoxValue() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getDate().toString());
	stringBuilder.append(":").append(getLesson().getIdInternal());
	return stringBuilder.toString();
    }

    public static NextPossibleSummaryLessonsAndDatesBean getNewInstance(String value) {
	int year = Integer.parseInt(value.substring(0, 4));
	int month = Integer.parseInt(value.substring(5, 7));
	int day = Integer.parseInt(value.substring(8, 10));
	if (year == 0 || month == 0 || day == 0) {
	    return null;
	}
	YearMonthDay date = new YearMonthDay(year, month, day);
	Integer lessonIdInternal = Integer.parseInt(value.substring(11));
	Lesson lesson = RootDomainObject.getInstance().readLessonByOID(lessonIdInternal);
	return new NextPossibleSummaryLessonsAndDatesBean(lesson, date);
    }
}
