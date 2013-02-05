package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Date;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Teacher;

public class MarkSheetManagementEditBean extends MarkSheetManagementBaseBean {

    private String teacherId;
    private Date evaluationDate;

    private MarkSheet markSheet;

    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToEdit;
    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppend;

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
        if (this.teacherId != null) {
            setTeacher(Teacher.readByIstId(this.teacherId));
        }
    }

    public MarkSheet getMarkSheet() {
        return this.markSheet;
    }

    public void setMarkSheet(MarkSheet markSheet) {
        this.markSheet = markSheet;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationBeansToAppend() {
        return enrolmentEvaluationBeansToAppend;
    }

    public void setEnrolmentEvaluationBeansToAppend(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppend) {
        this.enrolmentEvaluationBeansToAppend = enrolmentEvaluationBeansToAppend;
    }

    public Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationBeansToEdit() {
        return enrolmentEvaluationBeansToEdit;
    }

    public void setEnrolmentEvaluationBeansToEdit(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToEdit) {
        this.enrolmentEvaluationBeansToEdit = enrolmentEvaluationBeansToEdit;
    }
}
