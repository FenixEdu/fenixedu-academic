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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.certificates;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;

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
