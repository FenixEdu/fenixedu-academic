/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain.teacher.workTime;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author jpvl
 */
public class TeacherInstitutionWorkTime extends TeacherInstitutionWorkTime_Base implements
        ICreditsEventOriginator, PersistenceBrokerAware {

    public boolean equals(Object obj) {
        if (obj instanceof ITeacherInstitutionWorkTime) {
            final ITeacherInstitutionWorkTime teacherInstitutionWorkTime = (ITeacherInstitutionWorkTime) obj;
            return this.getIdInternal().equals(teacherInstitutionWorkTime.getIdInternal());
        }
        return false;
    }

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

    private void notifyTeacher() {
        ITeacher teacher = this.getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.WORKING_TIME, this);
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
