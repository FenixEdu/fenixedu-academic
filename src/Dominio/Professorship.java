/*
 * Created on 26/Mar/2003
 *
 * 
 */
package Dominio;

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentCredits;

/**
 * @author João Mota
 */
public class Professorship extends DomainObject implements IProfessorship, PersistenceBrokerAware {
    protected ITeacher teacher;

    protected IExecutionCourse executionCourse;

    private Integer keyTeacher;

    private Integer keyExecutionCourse;

    private List associatedShiftProfessorshift;

    /**
     * This attributes only matter if executionCourse is only from a master
     * degree.
     */
    private Double hours;

    private Double previousHours;

    /**
     *  
     */
    public Professorship() {
    }

    public Professorship(ITeacher teacher, IExecutionCourse executionCourse) {
        setTeacher(teacher);
        setExecutionCourse(executionCourse);
    }

    /**
     * @param integer
     */
    public Professorship(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return IDisciplinaExecucao
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @return Integer
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @return Integer
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @return ITeacher
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * Sets the executionCourse.
     * 
     * @param executionCourse
     *            The executionCourse to set
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /**
     * Sets the keyExecutionCourse.
     * 
     * @param keyExecutionCourse
     *            The keyExecutionCourse to set
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }

    /**
     * Sets the keyTeacher.
     * 
     * @param keyTeacher
     *            The keyTeacher to set
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * Sets the teacher.
     * 
     * @param teacher
     *            The teacher to set
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

    public String toString() {
        String result = "Professorship :\n";
        result += "\n  - ExecutionCourse : " + getExecutionCourse();
        result += "\n  - Teacher : " + getTeacher();

        return result;
    }

    public List getAssociatedShiftProfessorship() {
        return associatedShiftProfessorshift;
    }

    public void setAssociatedShiftProfessorship(List associatedTeacherShiftPercentage) {
        this.associatedShiftProfessorshift = associatedTeacherShiftPercentage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof IProfessorship) {
            IProfessorship professorship = (IProfessorship) obj;
            return this.getTeacher().equals(professorship.getTeacher())
                    && this.getExecutionCourse().equals(professorship.getExecutionCourse());
        }
        return false;
    }

    public Double getHours() {
        return hours;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IProfessorship#setCredits(java.lang.Float)
     */
    public void setHours(Double hours) {
        this.hours = hours;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();

            ITeacher teacher = this.getTeacher();

            IExecutionPeriod executionPeriod = this.getExecutionCourse().getExecutionPeriod();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null) {
                credits = new Credits();
//                credits.setTeacher(teacher);
//                credits.setExecutionPeriod(executionPeriod);
            }
            //            creditsDAO.simpleLockWrite(credits);
            credits.setTeacher(teacher);
            credits.setExecutionPeriod(executionPeriod);
            Double masterDegreeCredits = credits.getMasterDegreeCredits();

            double masterDegreeCreditsValue = masterDegreeCredits != null ? masterDegreeCredits
                    .doubleValue() : 0;

            double newValue = masterDegreeCreditsValue + this.getHours().doubleValue()
                    - this.previousHours.doubleValue();
            credits.setMasterDegreeCredits(new Double(newValue));

            // JPVL: used pb.store because I couldn't figure out how to put
            // previous
            // commented code to work! :(
            // The problem is in create a new credits register.
            broker.store(credits);
        } catch (ExcepcaoPersistencia e) {
            throw new PersistenceBrokerException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeInsert(PersistenceBroker broker) throws PersistenceBrokerException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();

            ITeacher teacher = this.getTeacher();

            IExecutionPeriod executionPeriod = this.getExecutionCourse().getExecutionPeriod();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null) {
                credits = new Credits();
            }
            //            creditsDAO.simpleLockWrite(credits);
            credits.setTeacher(teacher);
            credits.setExecutionPeriod(executionPeriod);
            Double masterDegreeCredits = credits.getMasterDegreeCredits();
            double masterDegreeCreditsValue = masterDegreeCredits != null ? masterDegreeCredits
                    .doubleValue() : 0;
            double newValue = masterDegreeCreditsValue + (this.getHours() != null ? this.getHours().doubleValue() : 0);
            credits.setMasterDegreeCredits(new Double(newValue));

            // JPVL: used pb.store because I couldn't figure out how to put
            // previous
            // commented code to work! :(
            // The problem is in create a new credits register.
            broker.store(credits);
        } catch (ExcepcaoPersistencia e) {
            throw new PersistenceBrokerException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeDelete(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterDelete(PersistenceBroker broker) throws PersistenceBrokerException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();

            ITeacher teacher = this.getTeacher();

            IExecutionPeriod executionPeriod = this.getExecutionCourse().getExecutionPeriod();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits != null) {
                creditsDAO.simpleLockWrite(credits);
                Double masterDegreeCredits = credits.getMasterDegreeCredits();
                double newValue = masterDegreeCredits.doubleValue() - previousHours.doubleValue();
                credits.setMasterDegreeCredits(new Double(newValue));
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
    public void afterLookup(PersistenceBroker broker) throws PersistenceBrokerException {
        this.previousHours = this.getHours() == null ? new Double(0) : this.getHours();
    }

}