package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author Tânia & Alexandra
 *  
 */
public class ShiftProfessorship extends ShiftProfessorship_Base implements PersistenceBrokerAware, ICreditsEventOriginator {

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IShiftProfessorship) {
            IShiftProfessorship shiftProfessorship = (IShiftProfessorship) obj;

            resultado = (getProfessorship().equals(shiftProfessorship.getProfessorship()))
                    && (getShift().equals(shiftProfessorship.getShift()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[CREDITS_TEACHER";
        result += ", codInt=" + getIdInternal();
        result += ", shift=" + getShift();
        result += "]";
        return result;
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }
    
    private void notifyTeacher() {
        ITeacher teacher = this.getProfessorship().getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.LESSONS, this);
    }
    
    public void afterUpdate(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void beforeInsert(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void beforeDelete(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterDelete(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }
    
    public void beforeUpdate(PersistenceBroker pb) throws PersistenceBrokerException {
    }

    public void afterInsert(PersistenceBroker pb) throws PersistenceBrokerException {
    }

    public void afterLookup(PersistenceBroker pb) throws PersistenceBrokerException {
    }

}