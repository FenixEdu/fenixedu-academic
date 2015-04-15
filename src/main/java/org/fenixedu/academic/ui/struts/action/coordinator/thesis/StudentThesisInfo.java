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
package org.fenixedu.academic.ui.struts.action.coordinator.thesis;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.util.MultiLanguageString;

public class StudentThesisInfo implements Serializable {

    private Student student;
    private Enrolment enrolment;
    private Thesis thesis;
    private ThesisPresentationState state;

    public StudentThesisInfo(Enrolment enrolment) {
        setStudent(enrolment.getStudent());
        setEnrolment(enrolment);
        setThesis(enrolment.getThesis());
    }

    public Student getStudent() {
        return this.student;
    }

    protected void setStudent(Student student) {
        this.student = student;
    }

    public Enrolment getEnrolment() {
        return this.enrolment;
    }

    protected void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public Thesis getThesis() {
        return this.thesis;
    }

    protected void setThesis(Thesis thesis) {
        this.thesis = thesis;

        setState(thesis);
    }

    public String getSemester() {
        final Enrolment enrolment = getEnrolment();
        final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
        return curricularCourse.isAnual() ? "" : enrolment.getExecutionPeriod().getSemester().toString();
    }

    public MultiLanguageString getTitle() {
        return thesis != null ? thesis.getTitle() : new MultiLanguageString();
    }

    public ThesisPresentationState getState() {
        return this.state;
    }

    private void setState(Thesis thesis) {
        this.state = ThesisPresentationState.getThesisPresentationState(thesis);
    }

    public String getThesisId() {
        final Thesis thesis = getThesis();

        return thesis == null ? null : thesis.getExternalId();
    }

    public String getEnrolmentOID() {
        final Enrolment enrolment = getEnrolment();
        return enrolment == null ? null : enrolment.getExternalId();
    }

    public boolean isUnassigned() {
        return getThesis() == null;
    }

    public boolean isDraft() {
        return getThesis() != null && getThesis().isDraft();
    }

    public boolean isSubmitted() {
        return getThesis() != null && getThesis().isSubmitted();
    }

    public boolean isWaitingConfirmation() {
        return getThesis() != null && getThesis().isWaitingConfirmation();
    }

    public boolean isConfirmed() {
        return getThesis() != null && getThesis().isConfirmed();
    }

    public boolean isEvaluated() {
        return getThesis() != null && getThesis().isEvaluated();
    }

    public boolean isPreEvaluated() {
        return isEvaluated() && !getThesis().isFinalThesis();
    }

    public boolean isSubmittedAndIsCoordinatorAndNotOrientator() {
        return getThesis() != null && getThesis().isSubmittedAndIsCoordinatorAndNotOrientator();
    }

}
