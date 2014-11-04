/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on May 19, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

public class MarkSheetTeacherMarkBean extends DataTranferObject {

    private Attends attends;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private Date evaluationDate;

    private String gradeValue;

    private boolean toSubmitMark;

    public MarkSheetTeacherMarkBean() {
    }

    public MarkSheetTeacherMarkBean(Attends attends, Date evaluationDate, String grade,
            EnrolmentEvaluationType enrolmentEvaluationType, boolean sendMark) {
        setAttends(attends);
        setEvaluationDate(evaluationDate);
        setGradeValue(grade);
        setEnrolmentEvaluationType(enrolmentEvaluationType);
        setToSubmitMark(sendMark);
    }

    public Attends getAttends() {
        return this.attends;
    }

    public void setAttends(Attends attends) {
        this.attends = attends;
    }

    public boolean isToSubmitMark() {
        return toSubmitMark;
    }

    public void setToSubmitMark(boolean markToSubmit) {
        this.toSubmitMark = markToSubmit;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String grade) {
        this.gradeValue = grade;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
    }
}
