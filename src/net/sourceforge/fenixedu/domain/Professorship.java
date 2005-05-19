package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base implements ICreditsEventOriginator,
        PersistenceBrokerAware {

    public Professorship() {
    }

    public Professorship(ITeacher teacher, IExecutionCourse executionCourse) {
        setTeacher(teacher);
        setExecutionCourse(executionCourse);
    }

    public String toString() {
        String result = "Professorship :\n";
        result += "\n  - ExecutionCourse : " + getExecutionCourse();
        result += "\n  - Teacher : " + getTeacher();

        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IProfessorship) {
            IProfessorship professorship = (IProfessorship) obj;

            return this.getTeacher().equals(professorship.getTeacher())
                    && this.getExecutionCourse().equals(professorship.getExecutionCourse());

        }
        return false;
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    private void notifyTeacher() {
        if (this.getExecutionCourse().isMasterDegreeOnly()) {
            ITeacher teacher = this.getTeacher();
            teacher.notifyCreditsChange(CreditsEvent.MASTER_DEGREE_LESSONS, this);
        }
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