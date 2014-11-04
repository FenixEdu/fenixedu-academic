/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on May 19, 2006
 */
package org.fenixedu.academic.dto.teacher.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.dto.DataTranferObject;

public class MarkSheetTeacherGradeSubmissionBean extends DataTranferObject {

    private ExecutionCourse executionCourse;
    private CurricularCourse selectedCurricularCourse;

    private Date evaluationDate;
    private Collection<MarkSheetTeacherMarkBean> marksToSubmit;

    private transient Teacher responsibleTeacher;

    public CurricularCourse getSelectedCurricularCourse() {
        return this.selectedCurricularCourse;
    }

    public void setSelectedCurricularCourse(CurricularCourse selectedCurricularCourse) {
        this.selectedCurricularCourse = selectedCurricularCourse;
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
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

    public Collection<CurricularCourse> getAllCurricularCourses() {
        return (getSelectedCurricularCourse() != null) ? Collections.singletonList(getSelectedCurricularCourse()) : getExecutionCourse()
                .getAssociatedCurricularCoursesSet();
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
