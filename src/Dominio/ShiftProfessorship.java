package Dominio;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

import Dominio.credits.event.CreditsEvent;
import Dominio.credits.event.ICreditsEventOriginator;

/**
 * @author Tânia & Alexandra
 *  
 */
public class ShiftProfessorship extends DomainObject implements IShiftProfessorship,
        PersistenceBrokerAware, ICreditsEventOriginator {
    private IProfessorship professorship = null;

    private Integer keyProfessorship = null;

    private IShift shift = null;

    private Integer keyShift = null;

    private Double percentage = null;

    /* construtor */
    public ShiftProfessorship() {
    }

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

    public Integer getKeyProfessorship() {
        return keyProfessorship;
    }

    public void setKeyProfessorship(Integer integer) {
        keyProfessorship = integer;
    }

    public IProfessorship getProfessorship() {
        return professorship;
    }

    public void setProfessorship(IProfessorship professorship) {
        this.professorship = professorship;
    }

    public IShift getShift() {
        return this.shift;
    }

    public void setShift(IShift shift) {
        this.shift = shift;
    }

    public Integer getKeyShift() {
        return keyShift;
    }

    public void setKeyShift(Integer integer) {
        keyShift = integer;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeUpdate(PersistenceBroker pb) throws PersistenceBrokerException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterUpdate(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeInsert(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterInsert(PersistenceBroker pb) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeDelete(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterDelete(PersistenceBroker pb) throws PersistenceBrokerException {
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterLookup(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterLookup(PersistenceBroker pb) throws PersistenceBrokerException {
    }

    private void notifyTeacher() {
        ITeacher teacher = this.getProfessorship().getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.LESSONS, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.event.ICreditsEventOriginator#belongsToExecutionPeriod(java.lang.Integer)
     */
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }
}