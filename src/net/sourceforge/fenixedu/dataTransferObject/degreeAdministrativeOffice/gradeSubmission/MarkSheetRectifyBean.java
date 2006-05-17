package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class MarkSheetRectifyBean implements Serializable{
	private DomainReference<MarkSheet> markSheet;
	private Integer studentNumber;
	private String newGrade;
	private Date evaluationDate;
	private String reason;
	private DomainReference<EnrolmentEvaluation> enrolmentEvaluation;
	
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
        this.enrolmentEvaluation = (enrolmentEvaluation != null) ? new DomainReference<EnrolmentEvaluation>(enrolmentEvaluation) : null; 
    }


}
