package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.log.IEnrolmentLog;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.enrollment.EnrollmentCondition;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Enrolment extends Enrolment_Base {

    private EnrollmentState enrollmentState;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private EnrollmentCondition condition;

    public Enrolment() {
        this.setOjbConcreteClass(this.getClass().getName());
    }



    /**
     * @return Returns the enrolmentEvaluationType.
     */
    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    /**
     * @param enrolmentEvaluationType
     *            The enrolmentEvaluationType to set.
     */
    public void setEnrolmentEvaluationType(
            EnrolmentEvaluationType enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
    }

    /**
     * @return Returns the enrollmentState.
     */
    public EnrollmentState getEnrollmentState() {
        return enrollmentState;
    }

    /**
     * @param enrollmentState
     *            The enrollmentState to set.
     */
    public void setEnrollmentState(EnrollmentState enrollmentState) {
        this.enrollmentState = enrollmentState;
    }



    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IEnrolment) {
            IEnrolment enrolment = (IEnrolment) obj;

            result = this.getStudentCurricularPlan().equals(
                    enrolment.getStudentCurricularPlan())
                    && this.getCurricularCourse().equals(
                            enrolment.getCurricularCourse())
                    && this.getExecutionPeriod().equals(
                            enrolment.getExecutionPeriod());
        }
        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "idInternal = " + super.getIdInternal() + "; ";
        result += "studentCurricularPlan = " + this.getStudentCurricularPlan()
                + "; ";
        result += "enrollmentState = " + this.getEnrollmentState() + "; ";
        result += "execution Period = " + this.getExecutionPeriod() + "; ";
        result += "curricularCourse = " + this.getCurricularCourse() + "]\n";
        return result;
    }

    /**
     * @return Returns the condition.
     */
    public EnrollmentCondition getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            The condition to set.
     */
    public void setCondition(EnrollmentCondition condition) {
        this.condition = condition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterDelete(PersistenceBroker arg0)
            throws PersistenceBrokerException {

        createNewEnrolmentLog(EnrolmentAction.UNENROL, arg0);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterInsert(PersistenceBroker arg0)
            throws PersistenceBrokerException {

        createNewEnrolmentLog(EnrolmentAction.ENROL, arg0);

    }

    private void createNewEnrolmentLog(EnrolmentAction action, PersistenceBroker arg0)
            throws PersistenceBrokerException {
        IEnrolmentLog enrolmentLog = new EnrolmentLog();
        //persistentEnrolmentLog.simpleLockWrite(enrolmentLog);
        enrolmentLog.setDate(new Date());
        enrolmentLog.setAction(action);
        enrolmentLog.setCurricularCourse(this.getCurricularCourse());
        enrolmentLog.setStudent(this.getStudentCurricularPlan().getStudent());
        arg0.store(enrolmentLog);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterLookup(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterLookup(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterUpdate(PersistenceBroker arg0)
            throws PersistenceBrokerException {
       
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeDelete(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeInsert(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeUpdate(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        
    }
}