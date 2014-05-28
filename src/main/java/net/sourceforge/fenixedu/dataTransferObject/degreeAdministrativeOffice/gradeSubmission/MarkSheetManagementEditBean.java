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
