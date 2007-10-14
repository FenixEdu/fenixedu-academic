package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;

public class MarkSheetEnrolmentEvaluationBean implements Serializable {
    
    private String gradeValue;
    private Date evaluationDate;    
    private DomainReference<Enrolment> enrolment;

    // used to edit
    private DomainReference<EnrolmentEvaluation> enrolmentEvaluation;
    
    public MarkSheetEnrolmentEvaluationBean() {        
    }
     
    public MarkSheetEnrolmentEvaluationBean(Enrolment enrolment, Date evaluationDate, Grade grade) {
        setEnrolment(enrolment);
        setEvaluationDate(evaluationDate);
        setGradeValue(grade.getValue());
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Enrolment getEnrolment() {
        return (this.enrolment == null) ? null : this.enrolment.getObject();
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = (enrolment != null) ? new DomainReference<Enrolment>(enrolment) : null;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String grade) {
        this.gradeValue = grade;
    }
    
    public EnrolmentEvaluation getEnrolmentEvaluation() {
        return (this.enrolmentEvaluation == null) ? null : this.enrolmentEvaluation.getObject();
    }

    public void setEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation) {
        this.enrolmentEvaluation = (enrolmentEvaluation != null) ? new DomainReference<EnrolmentEvaluation>(enrolmentEvaluation) : null;
        //TODO: ????????????
        if (this.enrolmentEvaluation != null) {
            setEnrolment(this.enrolmentEvaluation.getObject().getEnrolment());
        }
    }
}
