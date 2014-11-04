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
package org.fenixedu.academic.dto.degreeAdministrativeOffice.serviceRequest.documentRequest.certificates;

import java.io.Serializable;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Exam;

public class ExamDateCertificateExamSelectionEntryBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Enrolment enrolment;

    private Exam exam;

    public ExamDateCertificateExamSelectionEntryBean(final Enrolment enrolment, final Exam exam) {
        setEnrolment(enrolment);
        setExam(exam);
    }

    public Enrolment getEnrolment() {
        return this.enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Set<DegreeModuleScope> getDegreeModuleScopesForEnrolment() {
        return getExam().getDegreeModuleScopesFor(getEnrolment().getCurricularCourse());
    }

}
