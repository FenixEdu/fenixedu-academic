/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author jpvl
 */
public abstract class CreditLine extends CreditLine_Base implements PersistenceBrokerAware {

    protected abstract CreditsEvent getCreditEventGenerated();

    protected abstract void notifyTeacher();
    
    public void afterDelete(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterLookup(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeDelete(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeInsert(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
    }

}
