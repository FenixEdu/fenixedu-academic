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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ExamDateCertificateExamSelectionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<ExamDateCertificateExamSelectionEntryBean> entries;

    public ExamDateCertificateExamSelectionBean() {
        this.entries = new ArrayList<ExamDateCertificateExamSelectionEntryBean>();
    }

    public List<ExamDateCertificateExamSelectionEntryBean> getEntries() {
        return entries;
    }

    public void addEntry(final ExamDateCertificateExamSelectionEntryBean entry) {
        this.entries.add(entry);
    }

    public void addEntries(final List<ExamDateCertificateExamSelectionEntryBean> entries) {
        this.entries.addAll(entries);
    }

    public static ExamDateCertificateExamSelectionBean buildFor(final Collection<Enrolment> enrolments,
            final ExecutionSemester executionSemester) {
        final ExamDateCertificateExamSelectionBean result = new ExamDateCertificateExamSelectionBean();

        for (final Enrolment enrolment : enrolments) {
            for (final Exam exam : enrolment.getAttendsFor(executionSemester).getExecutionCourse()
                    .getPublishedExamsFor(enrolment.getCurricularCourse())) {
                result.addEntry(new ExamDateCertificateExamSelectionEntryBean(enrolment, exam));
            }
        }

        return result;
    }

    public Set<Enrolment> getEnrolmentsWithoutExam(final Collection<Enrolment> enrolments) {
        final Set<Enrolment> result = new HashSet<Enrolment>();

        for (final Enrolment enrolment : enrolments) {
            if (!containsEnrolment(enrolment)) {
                result.add(enrolment);
            }
        }

        return result;
    }

    private boolean containsEnrolment(Enrolment enrolment) {
        for (final ExamDateCertificateExamSelectionEntryBean each : getEntries()) {
            if (each.getEnrolment() == enrolment) {
                return true;
            }
        }

        return false;
    }
}
