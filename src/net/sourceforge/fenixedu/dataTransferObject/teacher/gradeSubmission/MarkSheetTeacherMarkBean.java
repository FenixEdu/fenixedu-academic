/*
 * Created on May 19, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DomainReference;

public class MarkSheetTeacherMarkBean extends DataTranferObject {

    private DomainReference<Attends> attends;
    private String enrolmentEvaluationType;
    private Date evaluationDate;
    private String grade;
    private boolean toSubmitMark;
    
    public MarkSheetTeacherMarkBean() {}
    
    public MarkSheetTeacherMarkBean(Attends attends, Date evaluationDate, String grade, String enrolmentEvaluationType, boolean sendMark) {
        setAttends(attends);
        setEvaluationDate(evaluationDate);
        setGrade(grade);
        setEnrolmentEvaluationType(enrolmentEvaluationType);
        setToSubmitMark(sendMark);
    }

    public Attends getAttends() {
        return (this.attends != null) ? this.attends.getObject() : null;
    }
    
    public void setAttends(Attends attends) {
        this.attends = (attends != null) ? new DomainReference<Attends>(attends) : null;
    }
    
    public boolean isToSubmitMark() {
        return toSubmitMark;
    }
    
    public void setToSubmitMark(boolean markToSubmit) {
        this.toSubmitMark = markToSubmit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public void setEnrolmentEvaluationType(String enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
    }
}
