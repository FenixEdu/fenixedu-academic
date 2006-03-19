package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;

public class Shift extends Shift_Base {

    public Shift() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
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

}
