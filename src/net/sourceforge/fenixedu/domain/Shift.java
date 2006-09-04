package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class Shift extends Shift_Base {

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_NAME = new BeanComparator("nome", Collator.getInstance());

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS = new ComparatorChain();
    static {
        ((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(new BeanComparator("tipo"));
        ((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(new BeanComparator("lessonsStringComparator"));
        ((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(new BeanComparator("idInternal"));
    }

    public Shift(final ExecutionCourse executionCourse, final ShiftType shiftType, final Integer lotacao, final Integer availabilityFinal) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDisciplinaExecucao(executionCourse);
	setTipo(shiftType);
	setLotacao(lotacao);
	setAvailabilityFinal(availabilityFinal);
	executionCourse.setShiftNames();
    }

    public void delete() {
        if (canBeDeleted()) {
            for (; hasAnyAssociatedLessons(); getAssociatedLessons().get(0).delete());
            for (; hasAnyAssociatedShiftProfessorship(); getAssociatedShiftProfessorship().get(0).delete());
            
            getAssociatedClasses().clear();
            final ExecutionCourse executionCourse = getDisciplinaExecucao();
            removeDisciplinaExecucao();
            removeRootDomainObject();
            deleteDomainObject();

            executionCourse.setShiftNames();
        } else {
            throw new DomainException("shift.cannot.be.deleted");
        }
    }

	@Override
	public void setTipo(final ShiftType tipo) {
		super.setTipo(tipo);
		for (final Lesson lesson : getAssociatedLessonsSet()) {
			lesson.setTipo(tipo);
		}
	}

	public boolean canBeDeleted() {
	    if (hasAnyAssociatedSummaries()
                || hasAnyAssociatedStudentGroups()
                || hasAnyStudents()
                || hasAnyDegreeTeachingServices()) {
            return false;
        }
        return true;
    }

    public double hours() {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = (Lesson) lessons.get(i);
            hours += lesson.hours();
        }
        return hours;
    }
    
    public double hoursAfter(int hour) {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = (Lesson) lessons.get(i);
            hours += lesson.hoursAfter(hour);
        }
        return hours;
    }

    public void associateSchoolClass(SchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedClasses().contains(schoolClass)) {
            this.getAssociatedClasses().add(schoolClass);
        }
        if (!schoolClass.getAssociatedShifts().contains(this)) {
            schoolClass.getAssociatedShifts().add(this);
        }
    }
            
    public Double getAvailableShiftPercentageForTeacher(Teacher teacher) {
        Double availablePercentage = 100.0;
        for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
            /**
             * if shift's type is LABORATORIAL the shift professorship
             * percentage can exceed 100%
             */
            if (degreeTeachingService.getProfessorship().getTeacher() != teacher
                    && !getTipo().equals(ShiftType.LABORATORIAL)) {
                availablePercentage -= degreeTeachingService.getPercentage();
            }
        }
        return availablePercentage;
    }

    public SortedSet<Lesson> getLessonsOrderedByWeekDayAndStartTime() {
        final SortedSet<Lesson> lessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
        lessons.addAll(getAssociatedLessonsSet());
        return lessons;
    }
 
    public String getLessonsStringComparator() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Lesson lesson : getLessonsOrderedByWeekDayAndStartTime()) {
            stringBuilder.append(lesson.getDiaSemana().getDiaSemana().toString());
            stringBuilder.append(lesson.getBeginHourMinuteSecond().toString());            
        }       
        return stringBuilder.toString();
    }
    
    public boolean reserveForStudent(final Registration registration) {
        if (getLotacao().intValue() > getStudentsCount()) {
            addStudents(registration);
            return true;
        } else {
            return false;
        }
    }
    
    public Lesson getLessonByBeginTime(HourMinuteSecond time) {
        for (Lesson lesson : getAssociatedLessonsSet()) {
            if(lesson.getBeginHourMinuteSecond().isEqual(time)) {
                return lesson;
            }
        }
        return null;
    }
    
    //OLD FUNCTIONS    
    public void transferSummary(Summary summary, Date summaryDate, Date summaryHour, OldRoom room, boolean newSummary) {
        if(newSummary){
            checkIfSummaryExistFor(summaryDate, summaryHour);
        }
        summary.modifyShift(this, summaryDate, summaryHour, room);
    }

    private void checkIfSummaryExistFor(final Date summaryDate, final Date summaryHour) {
        final Iterator associatedSummaries = getAssociatedSummariesIterator();
        Date summaryDateAux = prepareDate(summaryDate);               
        while (associatedSummaries.hasNext()) {
            Summary summary = (Summary) associatedSummaries.next();
            Date iterSummaryDate = prepareDate(summary.getSummaryDate());
            if (iterSummaryDate.equals(summaryDateAux)
                    && summary.getSummaryHour().equals(summaryHour)) {
                throw new DomainException("error.summary.already.exists");
            }
        }
    }
  
    private Date prepareDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    } 
    
    public String getShiftLabel() {
        final StringBuilder lessonsLabel = new StringBuilder();
        int index = 0;
        for (Lesson lesson : getAssociatedLessonsSet()) {
            index++;
            lessonsLabel.append(lesson.getDiaSemana().toString()).append(" (");
            lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getInicio().getTime()))
                    .append("-");
            lessonsLabel.append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime())).append(") ");
            if (lesson.getSala() != null) {
                lessonsLabel.append(lesson.getSala().getName().toString());
            }
            if (index < getAssociatedLessonsCount()) {
                lessonsLabel.append(" , ");
            } else {
                lessonsLabel.append(" ");
            }
        }
        return lessonsLabel.toString();
    }
    
}
