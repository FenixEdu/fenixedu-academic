/*
 * Created on 21/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import java.util.Date;

import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;

/**
 * @author dcs-rjao
 * 
 * 21/Mai/2003
 */
public interface IEnrolmentEvaluation extends IDomainObject {

    public abstract IEnrollment getEnrolment();

    public abstract EnrolmentEvaluationType getEnrolmentEvaluationType();

    public abstract Date getExamDate();

    public abstract String getGrade();

    public abstract Date getGradeAvailableDate();

    public abstract EnrolmentEvaluationState getEnrolmentEvaluationState();

    public abstract IPessoa getPersonResponsibleForGrade();

    public abstract IEmployee getEmployee();

    public abstract Date getWhen();

    public abstract String getCheckSum();

    public abstract String getObservation();

    public abstract void setPersonResponsibleForGrade(IPessoa teacher);

    public abstract void setEnrolment(IEnrollment enrolment);

    public abstract void setEnrolmentEvaluationType(EnrolmentEvaluationType type);

    public abstract void setExamDate(Date date);

    public abstract void setGrade(String string);

    public abstract void setGradeAvailableDate(Date date);

    public abstract void setEnrolmentEvaluationState(EnrolmentEvaluationState state);

    public abstract void setEmployee(IEmployee employee);

    public abstract void setWhen(Date date);

    public abstract void setCheckSum(String checkSum);

    public abstract void setObservation(String observation);

    public abstract boolean isNormal();

    public abstract boolean isImprovment();
}