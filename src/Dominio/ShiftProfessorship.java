package Dominio;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.OJB.credits.CreditsOJB;
import ServidorPersistente.credits.IPersistentCredits;

/**
 * @author Tânia & Alexandra
 *  
 */
public class ShiftProfessorship extends DomainObject implements IShiftProfessorship,
        PersistenceBrokerAware {
    private IProfessorship professorship = null;

    private Integer keyProfessorship = null;

    private ITurno shift = null;

    private Integer keyShift = null;

    private Double percentage = null;

    private Double previousPercentage = null;

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

    public ITurno getShift() {
        return this.shift;
    }

    public void setShift(ITurno shift) {
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
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            CreditsOJB creditsDAO = (CreditsOJB) sp.getIPersistentCredits();

            ITeacher teacher = this.getProfessorship().getTeacher();

            IExecutionPeriod executionPeriod = this.getProfessorship().getExecutionCourse()
                    .getExecutionPeriod();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null) {
                credits = new Credits();
                credits.setTeacher(teacher);
                credits.setExecutionPeriod(executionPeriod);
            }
            //      creditsDAO.simpleLockWrite(credits);

            Double lessons = credits.getLessons();

            double lessonsValue = lessons != null ? lessons.doubleValue() : 0;

            double actualPercentage = (this.getPercentage().doubleValue() / 100) * getShift().hours();
            double previousPercentage = (this.previousPercentage.doubleValue() / 100)
                    * getShift().hours();

            double newValue = lessonsValue + actualPercentage - previousPercentage;
            credits.setLessons(new Double(newValue));
            // JPVL: used pb.store because I couldn't figure out how to put
            // previous
            // commented code to work! :(.
            // The problem is in create a new credits register.
            pb.store(credits);
        } catch (ExcepcaoPersistencia e) {
            throw new PersistenceBrokerException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterUpdate(PersistenceBroker pb) throws PersistenceBrokerException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeInsert(PersistenceBroker pb) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterInsert(PersistenceBroker pb) throws PersistenceBrokerException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();

            ITeacher teacher = this.getProfessorship().getTeacher();

            IExecutionPeriod executionPeriod = this.getProfessorship().getExecutionCourse()
                    .getExecutionPeriod();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null) {
                credits = new Credits();
                credits.setTeacher(teacher);
                credits.setExecutionPeriod(executionPeriod);
            }
            //            creditsDAO.simpleLockWrite(credits);
            credits.setTeacher(teacher);
            credits.setExecutionPeriod(executionPeriod);
            Double lessons = credits.getLessons();
            double lessonsValue = lessons != null ? lessons.doubleValue() : 0;
            double newValue = lessonsValue
                    + ((this.getPercentage().doubleValue() / 100) * getShift().hours());
            credits.setLessons(new Double(newValue));
            // JPVL: used pb.store because I couldn't figure out how to put
            // previous
            // commented code to work! :(.
            // The problem is in create a new credits register.
            pb.store(credits);
        } catch (ExcepcaoPersistencia e) {
            throw new PersistenceBrokerException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeDelete(PersistenceBroker pb) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterDelete(PersistenceBroker pb) throws PersistenceBrokerException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();

            ITeacher teacher = this.getProfessorship().getTeacher();

            IExecutionPeriod executionPeriod = this.getProfessorship().getExecutionCourse()
                    .getExecutionPeriod();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits != null) {
                creditsDAO.simpleLockWrite(credits);
                Double lessons = credits.getLessons();
                double newValue = lessons.doubleValue()
                        - ((previousPercentage.doubleValue() / 100) * getShift().hours());
                credits.setLessons(new Double(newValue));
            }
        } catch (ExcepcaoPersistencia e) {
            throw new PersistenceBrokerException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterLookup(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterLookup(PersistenceBroker pb) throws PersistenceBrokerException {
        this.previousPercentage = this.getPercentage();
    }
}