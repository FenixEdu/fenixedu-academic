package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;
import java.util.Comparator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class NextPossibleSummaryLessonsAndDatesBean implements Serializable, Comparable<NextPossibleSummaryLessonsAndDatesBean> {

    public static final ResourceBundle enumerationResourcesBundle = ResourceBundle.getBundle("resources/EnumerationResources");
    public static final Comparator<NextPossibleSummaryLessonsAndDatesBean> COMPARATOR_BY_DATE_AND_HOUR = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("date"), true);
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("time"), true);
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("shift.externalId"));
    }

    private ShiftType lessonType;

    private Lesson lessonReference;

    private Shift shiftReference;

    private YearMonthDay date;

    private Integer studentsNumber;

    private boolean extraLesson;

    private HourMinuteSecond time;

    private AllocatableSpace roomReference;

    public NextPossibleSummaryLessonsAndDatesBean() {
    }

    public NextPossibleSummaryLessonsAndDatesBean(Lesson lesson, YearMonthDay date) {
        setLesson(lesson);
        setShift(lesson.getShift());
        setDate(date);
        setExtraLesson(false);
        setTime(lesson.getBeginHourMinuteSecond());
    }

    public NextPossibleSummaryLessonsAndDatesBean(Shift shift, YearMonthDay date, HourMinuteSecond time, AllocatableSpace room) {
        setShift(shift);
        setDate(date);
        setExtraLesson(true);
        setTime(time);
        setRoom(room);
    }

    public boolean getHasLessonInstance() {
        return getLesson().getLessonInstanceFor(getDate()) != null;
    }

    public String getLessonInstancePrettyPrint() {

        if (isExtraLesson()) {
            StringBuilder builder = new StringBuilder();
            builder.append(getDate().toDateTimeAtMidnight().toString("E")).append(" (");
            builder.append(getTime().toString("HH:mm")).append(")");
            AllocatableSpace room = getRoom();
            if (room != null) {
                builder.append(" ").append(room.getIdentification());
            }
            return builder.toString();
        }

        Lesson lesson = getLesson();
        LessonInstance lessonInstance = lesson.getLessonInstanceFor(getDate());
        return lessonInstance != null ? lessonInstance.prettyPrint() : lesson.prettyPrint();
    }

    public String getShiftTypesPrettyPrint() {
        return isExtraLesson() ? enumerationResourcesBundle.getString("EXTRA_SUMMARY") : getShift().getShiftTypesPrettyPrint();
    }

    public boolean getWrittenSummary() {
        return isExtraLesson() ? true : getLesson().getSummaryByDate(getDate()) != null;
    }

    public String getMonthString() {
        return getDate().toDateTimeAtMidnight().toString("MMMM");
    }

    public boolean getWithoutSummary() {

        if (isExtraLesson()) {
            return false;
        }

        Lesson lesson = getLesson();
        if (lesson.isDateValidToInsertSummary(getDate()) && lesson.isTimeValidToInsertSummary(new HourMinuteSecond(), getDate())
                && !getWrittenSummary()) {
            return true;
        }

        return false;
    }

    public boolean getIsPossibleDeleteLessonInstance() {
        return isExtraLesson() ? false : getLesson().getSummaryByDate(getDate()) == null;
    }

    public String getCheckBoxValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDate().toString("yyyyMMdd"));
        stringBuilder.append(getLesson().getExternalId());
        return stringBuilder.toString();
    }

    public static NextPossibleSummaryLessonsAndDatesBean getNewInstance(String value) {

        int year = Integer.parseInt(value.substring(0, 4));
        int month = Integer.parseInt(value.substring(4, 6));
        int day = Integer.parseInt(value.substring(6, 8));
        if (year == 0 || month == 0 || day == 0) {
            return null;
        }

        YearMonthDay date = new YearMonthDay(year, month, day);
        Lesson lesson = AbstractDomainObject.fromExternalId(value.substring(8));
        NextPossibleSummaryLessonsAndDatesBean bean = new NextPossibleSummaryLessonsAndDatesBean(lesson, date);

        return bean;
    }

    public HourMinuteSecond getTime() {
        return time;
    }

    public void setTime(HourMinuteSecond time) {
        this.time = time;
    }

    public boolean isExtraLesson() {
        return extraLesson;
    }

    public void setExtraLesson(boolean extraLesson) {
        this.extraLesson = extraLesson;
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
        return this.lessonReference;
    }

    public void setLesson(Lesson lesson) {
        this.lessonReference = lesson;
    }

    public AllocatableSpace getRoom() {
        return this.roomReference;
    }

    public void setRoom(AllocatableSpace room) {
        this.roomReference = room;
    }

    public Shift getShift() {
        return this.shiftReference;
    }

    public void setShift(Shift shift) {
        this.shiftReference = shift;
    }

    public ShiftType getLessonType() {
        return lessonType;
    }

    public void setLessonType(ShiftType lessonType) {
        this.lessonType = lessonType;
    }

    @Override
    public int compareTo(final NextPossibleSummaryLessonsAndDatesBean o) {
        return date.compareTo(o.getDate());
    }
}
