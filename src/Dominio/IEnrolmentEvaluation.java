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
public interface IEnrolmentEvaluation {

	public abstract IEnrolment getEnrolment();
	public abstract EnrolmentEvaluationType getEvaluationType();
	public abstract Date getExamDate();
	public abstract String getGrade();
	public abstract Date getGradeAvailableDate();
	public abstract EnrolmentEvaluationState getState();
	public abstract ITeacher getResponsibleTeacher();

	public abstract void setResponsibleTeacher(ITeacher teacher);
	public abstract void setEnrolment(IEnrolment enrolment);
	public abstract void setEvaluationType(EnrolmentEvaluationType type);
	public abstract void setExamDate(Date date);
	public abstract void setGrade(String string);
	public abstract void setGradeAvailableDate(Date date);
	public abstract void setState(EnrolmentEvaluationState state);
}