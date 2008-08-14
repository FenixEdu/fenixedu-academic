/*
 * Created on May 19, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;

public class MarkSheetTeacherGradeSubmissionBean extends DataTranferObject {

    private DomainReference<ExecutionCourse> executionCourse;
    private DomainReference<CurricularCourse> selectedCurricularCourse;

    private Date evaluationDate;
    private Collection<MarkSheetTeacherMarkBean> marksToSubmit;

    private transient Teacher responsibleTeacher;

    public CurricularCourse getSelectedCurricularCourse() {
	return (this.selectedCurricularCourse != null) ? this.selectedCurricularCourse.getObject() : null;
    }

    public void setSelectedCurricularCourse(CurricularCourse selectedCurricularCourse) {
	this.selectedCurricularCourse = (selectedCurricularCourse != null) ? new DomainReference<CurricularCourse>(
		selectedCurricularCourse) : null;
    }

    public ExecutionCourse getExecutionCourse() {
	return (this.executionCourse != null) ? this.executionCourse.getObject() : null;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = (executionCourse != null) ? new DomainReference<ExecutionCourse>(executionCourse) : null;
    }

    public Collection<MarkSheetTeacherMarkBean> getMarksToSubmit() {
	return marksToSubmit;
    }

    public void setMarksToSubmit(Collection<MarkSheetTeacherMarkBean> marksToSubmit) {
	this.marksToSubmit = marksToSubmit;
    }

    public Date getEvaluationDate() {
	return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
	this.evaluationDate = evaluationDate;
    }

    public List<CurricularCourse> getAllCurricularCourses() {
	return (getSelectedCurricularCourse() != null) ? Collections.singletonList(getSelectedCurricularCourse())
		: getExecutionCourse().getCurricularCoursesWithDegreeType();
    }

    public Collection<MarkSheetTeacherMarkBean> getSelectedMarksToSubmit() {
	Collection<MarkSheetTeacherMarkBean> result = new ArrayList<MarkSheetTeacherMarkBean>();
	for (MarkSheetTeacherMarkBean markBean : getMarksToSubmit()) {
	    if (markBean.isToSubmitMark()) {
		result.add(markBean);
	    }
	}
	return result;
    }

    public Teacher getResponsibleTeacher() {
	return responsibleTeacher;
    }

    public void setResponsibleTeacher(Teacher responsibleTeacher) {
	this.responsibleTeacher = responsibleTeacher;
    }

    public List<CurricularCourse> getCurricularCoursesAvailableToGradeSubmission() {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (CurricularCourse curricularCourse : getAllCurricularCourses()) {
	    if (curricularCourse.isGradeSubmissionAvailableFor(getExecutionCourse().getExecutionPeriod())) {
		result.add(curricularCourse);
	    }
	}
	return result;
    }
}
