/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain.degree.finalProject;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudent extends DomainObject implements
        ITeacherDegreeFinalProjectStudent, ICreditsEventOriginator, PersistenceBrokerAware {
    private IExecutionPeriod executionPeriod;

    private Integer keyExecutionPeriod;

    private Integer keyStudent;

    private Integer keyTeacher;

    private Double percentage;

    private IStudent student;

    private ITeacher teacher;

    public TeacherDegreeFinalProjectStudent() {
    }

    /**
     * @param teacherDegreeFinalProjectStudentId
     */
    public TeacherDegreeFinalProjectStudent(Integer teacherDegreeFinalProjectStudentId) {
        super(teacherDegreeFinalProjectStudentId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof TeacherDegreeFinalProjectStudent) {
            TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (TeacherDegreeFinalProjectStudent) obj;

            return (this.getStudent().equals(teacherDegreeFinalProjectStudent.getStudent())
                    && this.getTeacher().equals(teacherDegreeFinalProjectStudent.getTeacher()) && this
                    .getExecutionPeriod().equals(teacherDegreeFinalProjectStudent.getExecutionPeriod()));

        }
        return false;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @return Returns the keyExecutionYear.
     */
    public Integer getKeyExecutionPeriod() {
        return this.keyExecutionPeriod;
    }

    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent() {
        return this.keyStudent;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return this.keyTeacher;
    }

    /**
     * @return Returns the percentage.
     */
    public Double getPercentage() {
        return this.percentage;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return this.student;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return this.teacher;
    }

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @param keyExecutionYear
     *            The keyExecutionYear to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionYear) {
        this.keyExecutionPeriod = keyExecutionYear;
    }

    /**
     * @param keyStudent
     *            The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @param percentage
     *            The percentage to set.
     */
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.event.ICreditsEventOriginator#belongsToExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
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
        ITeacher teacher = this.getTeacher();
        teacher.notifyCreditsChange(CreditsEvent.DEGREE_FINAL_PROJECT_STUDENT, this);
    }
}