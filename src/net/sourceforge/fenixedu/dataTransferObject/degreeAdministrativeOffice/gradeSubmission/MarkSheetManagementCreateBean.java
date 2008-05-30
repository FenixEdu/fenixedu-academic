package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import net.sourceforge.fenixedu.domain.MarkSheetType;

public class MarkSheetManagementCreateBean extends MarkSheetManagementBaseBean {

    private Integer teacherNumber;
    private Date evaluationDate;
    private MarkSheetType markSheetType;
    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans;
    private Collection<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans;

    public MarkSheetType getMarkSheetType() {
	return markSheetType;
    }

    public void setMarkSheetType(MarkSheetType markSheetType) {
	this.markSheetType = markSheetType;
    }

    public Date getEvaluationDate() {
	return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
	this.evaluationDate = evaluationDate;
    }

    public Integer getTeacherNumber() {
	return teacherNumber;
    }

    public void setTeacherNumber(Integer teacherNumber) {
	this.teacherNumber = teacherNumber;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationBeans() {
	return enrolmentEvaluationBeans;
    }

    public void setEnrolmentEvaluationBeans(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans) {
	this.enrolmentEvaluationBeans = enrolmentEvaluationBeans;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getImpossibleEnrolmentEvaluationBeans() {
	return impossibleEnrolmentEvaluationBeans;
    }

    public void setImpossibleEnrolmentEvaluationBeans(
	    Collection<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans) {
	this.impossibleEnrolmentEvaluationBeans = impossibleEnrolmentEvaluationBeans;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getAllEnrolmentEvalutionBeans() {
	final Collection<MarkSheetEnrolmentEvaluationBean> result = new ArrayList<MarkSheetEnrolmentEvaluationBean>();
	result.addAll(getEnrolmentEvaluationBeans());
	result.addAll(getImpossibleEnrolmentEvaluationBeans());
	return result;
    }

}
