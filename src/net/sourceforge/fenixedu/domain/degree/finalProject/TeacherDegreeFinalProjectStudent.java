/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain.degree.finalProject;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base implements
        ICreditsEventOriginator, PersistenceBrokerAware {

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

    private void notifyTeacher() {
        ITeacher teacher = this.getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.DEGREE_FINAL_PROJECT_STUDENT, this);
    }

    public void afterInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
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
