package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author Fernanda Quitério 17/10/2003
 * @author jpvl
 */
public class SupportLesson extends SupportLesson_Base implements PersistenceBrokerAware,
        ICreditsEventOriginator {

    public SupportLesson() {
    }

    public SupportLesson(Integer idInternal) {
        this.setIdInternal(idInternal);
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof ISupportLesson) {
            ISupportLesson supportLessonsTimetable = (ISupportLesson) arg0;

            if (elementsAreEqual(supportLessonsTimetable.getProfessorship(), this.getProfessorship())
                    && elementsAreEqual(supportLessonsTimetable.getStartTime(), this.getStartTime())
                    && elementsAreEqual(supportLessonsTimetable.getEndTime(), this.getEndTime())
                    && elementsAreEqual(supportLessonsTimetable.getWeekDay(), this.getWeekDay())) {
                result = true;
            }
        }
        return result;
    }

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    private boolean elementsAreEqual(Object element1, Object element2) {
        boolean result = false;
        if ((element1 == null && element2 == null)
                || (element1 != null && element2 != null && element1.equals(element2))) {
            result = true;
        }
        return result;
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    private void notifyTeacher() {
        ITeacher teacher = this.getProfessorship().getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.SUPPORT_LESSONS, this);
    }

    public void afterUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterDelete(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void beforeUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeInsert(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeDelete(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void afterLookup(PersistenceBroker broker) throws PersistenceBrokerException {
    }

}
