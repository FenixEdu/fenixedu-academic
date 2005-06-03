package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.log.IEnrolmentLog;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Enrolment extends Enrolment_Base {

    public Enrolment() {
        this.setOjbConcreteClass(this.getClass().getName());
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "idInternal = " + super.getIdInternal() + "; ";
        result += "studentCurricularPlan = " + this.getStudentCurricularPlan() + "; ";
        result += "enrollmentState = " + this.getEnrollmentState() + "; ";
        result += "execution Period = " + this.getExecutionPeriod() + "; ";
        result += "curricularCourse = " + this.getCurricularCourse() + "]\n";
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IEnrolment) {
            IEnrolment enrolment = (IEnrolment) obj;

            result = this.getStudentCurricularPlan().equals(enrolment.getStudentCurricularPlan())
                    && this.getCurricularCourse().equals(enrolment.getCurricularCourse())
                    && this.getExecutionPeriod().equals(enrolment.getExecutionPeriod());
        }
        return result;
    }

    private void createNewEnrolmentLog(EnrolmentAction action, PersistenceBroker arg0)
            throws PersistenceBrokerException {
        IEnrolmentLog enrolmentLog = new EnrolmentLog();
        enrolmentLog.setDate(new Date());
        enrolmentLog.setAction(action);
        enrolmentLog.setCurricularCourse(this.getCurricularCourse());
        enrolmentLog.setStudent(this.getStudentCurricularPlan().getStudent());
        arg0.store(enrolmentLog);
    }

    public void afterDelete(PersistenceBroker arg0) throws PersistenceBrokerException {
        createNewEnrolmentLog(EnrolmentAction.UNENROL, arg0);

    }

    public void afterInsert(PersistenceBroker arg0) throws PersistenceBrokerException {
        createNewEnrolmentLog(EnrolmentAction.ENROL, arg0);
    }

    public void afterLookup(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void afterUpdate(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void beforeDelete(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void beforeInsert(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void beforeUpdate(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

}
