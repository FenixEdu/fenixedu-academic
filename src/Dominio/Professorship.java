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

import Dominio.credits.event.CreditsEvent;
import Dominio.credits.event.ICreditsEventOriginator;

/**
 * @author João Mota
 */
public class Professorship extends DomainObject implements IProfessorship, ICreditsEventOriginator,
        PersistenceBrokerAware {
    protected ITeacher teacher;

    protected IExecutionCourse executionCourse;

    private Integer keyTeacher;

    private Integer keyExecutionCourse;

    private List associatedShiftProfessorshift;

    private List supportLessons;

    /**
     * This attributes only matter if executionCourse is only from a master
     * degree.
     */
    private Double hours;

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

    /**
     * @return Returns the supportLessons.
     */
    public List getSupportLessons() {
        return supportLessons;
    }

    /**
     * @param supportLessons
     *            The supportLessons to set.
     */
    public void setSupportLessons(List supportLessons) {
        this.supportLessons = supportLessons;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.event.ICreditsEventOriginator#belongsToExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
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
        notifyTeacher();
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
        notifyTeacher();
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
        notifyTeacher();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterLookup(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterLookup(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    private void notifyTeacher() {
        if (this.getExecutionCourse().isMasterDegreeOnly()) {
            ITeacher teacher = this.getTeacher();
            teacher.notifyCreditsChange(CreditsEvent.MASTER_DEGREE_LESSONS, this);
        }
    }
}