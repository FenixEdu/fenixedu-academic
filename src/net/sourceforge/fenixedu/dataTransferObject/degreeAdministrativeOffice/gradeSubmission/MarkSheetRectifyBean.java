package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;

public class MarkSheetRectifyBean extends MarkSheetManagementBaseBean {

    private DomainReference<MarkSheet> markSheet;
    private DomainReference<EnrolmentEvaluation> enrolmentEvaluation;
    private MarkSheetType markSheetType;

    private Integer studentNumber;
    private String newGrade;
    private Date evaluationDate;
    private String reason;

    public MarkSheet getMarkSheet() {
	return (this.markSheet == null) ? null : this.markSheet.getObject();
    }

    public void setMarkSheet(MarkSheet markSheet) {
	this.markSheet = (markSheet != null) ? new DomainReference<MarkSheet>(markSheet) : null;
    }

    public Date getEvaluationDate() {
	return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
	this.evaluationDate = evaluationDate;
    }

    public Grade getRectifiedGrade() {
	return Grade.createGrade(getNewGrade(), getEnrolmentEvaluation().getGradeScale());
    }

    public String getNewGrade() {
	return newGrade;
    }

    public void setNewGrade(String newGrade) {
	this.newGrade = newGrade;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public EnrolmentEvaluation getEnrolmentEvaluation() {
	return (this.enrolmentEvaluation == null) ? null : this.enrolmentEvaluation.getObject();
    }

    public void setEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation) {
	this.enrolmentEvaluation = (enrolmentEvaluation != null) ? new DomainReference<EnrolmentEvaluation>(enrolmentEvaluation)
		: null;
    }

    public void setMarkSheetType(MarkSheetType markSheetType) {
	this.markSheetType = markSheetType;
    }

    public MarkSheetType getMarkSheetType() {
	return markSheetType;
    }

}
