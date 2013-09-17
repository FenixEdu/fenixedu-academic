package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class MarkSheetRectifyBean extends MarkSheetManagementBaseBean {

    private MarkSheet markSheet;
    private EnrolmentEvaluation enrolmentEvaluation;
    private MarkSheetType markSheetType;

    private Integer studentNumber;
    private String newGrade;
    private Date evaluationDate;
    private String reason;

    public MarkSheet getMarkSheet() {
        return this.markSheet;
    }

    public void setMarkSheet(MarkSheet markSheet) {
        this.markSheet = markSheet;
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
        return this.enrolmentEvaluation;
    }

    public void setEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation) {
        this.enrolmentEvaluation = enrolmentEvaluation;
    }

    public void setMarkSheetType(MarkSheetType markSheetType) {
        this.markSheetType = markSheetType;
    }

    public MarkSheetType getMarkSheetType() {
        return markSheetType;
    }

    @Atomic
    public MarkSheet createRectificationOldMarkSheet(Person person) throws InvalidArgumentsServiceException {
        if (getEnrolmentEvaluation() == null) {
            throw new InvalidArgumentsServiceException();
        }
        return getEnrolmentEvaluation()
                .getEnrolment()
                .getCurricularCourse()
                .rectifyOldEnrolmentEvaluation(getEnrolmentEvaluation(), getMarkSheetType(), getEvaluationDate(),
                        getRectifiedGrade(), getReason(), person);
    }
}
