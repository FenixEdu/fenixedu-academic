package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Teacher;

public class MarkSheetManagementEditBean extends MarkSheetManagementBaseBean {

    private Integer teacherNumber;
    private Date evaluationDate;

    private DomainReference<MarkSheet> markSheet;

    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToEdit;
    private Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppend;

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
	if (this.teacherNumber != null) {
	    setTeacher(Teacher.readByNumber(this.teacherNumber));
	}
    }

    public MarkSheet getMarkSheet() {
	return (this.markSheet != null) ? this.markSheet.getObject() : null;
    }

    public void setMarkSheet(MarkSheet markSheet) {
	this.markSheet = (markSheet != null) ? new DomainReference<MarkSheet>(markSheet) : null;
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
