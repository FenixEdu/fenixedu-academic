package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base implements IProfessorship, ICreditsEventOriginator,
        PersistenceBrokerAware {
    protected ITeacher teacher;

    protected IExecutionCourse executionCourse;

    private List associatedShiftProfessorshift;

    private List supportLessons;

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
     * @return IDisciplinaExecucao
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
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