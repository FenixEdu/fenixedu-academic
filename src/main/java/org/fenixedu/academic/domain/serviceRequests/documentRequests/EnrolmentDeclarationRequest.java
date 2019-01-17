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
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

public class EnrolmentDeclarationRequest extends EnrolmentDeclarationRequest_Base {

    private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;

    protected EnrolmentDeclarationRequest() {
        super();
    }

    public EnrolmentDeclarationRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkRulesToCreate(bean);
    }

    private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (!bean.getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
            throw new DomainException("EnrolmentDeclarationRequest.no.enrolments.for.registration.in.current.executionYear");
        }
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.ENROLMENT_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    final public Integer getNumberOfUnits() {
        return getEntriesToReport().size() + getExtraCurricularEntriesToReport().size() + getPropaedeuticEntriesToReport().size();
    }

    final public Collection<Enrolment> getEntriesToReport() {
        return getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear());
    }

    final public Collection<Enrolment> getExtraCurricularEntriesToReport() {
        final Collection<Enrolment> extraCurricular = new HashSet<Enrolment>();
        for (final Enrolment entry : getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear())) {
            if (entry.isExtraCurricular() && entry.getEnrolmentWrappersSet().isEmpty()) {
                extraCurricular.add(entry);
            }
        }
        return extraCurricular;
    }

    final public Collection<Enrolment> getPropaedeuticEntriesToReport() {
        final Collection<Enrolment> propaedeutic = new HashSet<Enrolment>();
        for (final Enrolment entry : getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear())) {
            if (!(entry.isExtraCurricular() && entry.getEnrolmentWrappersSet().isEmpty()) && entry.isPropaedeutic()) {
                propaedeutic.add(entry);
            }
        }
        return propaedeutic;
    }

    @Override
    protected boolean hasFreeDeclarationRequests() {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        final Set<DocumentRequest> schoolRegistrationDeclarations = getRegistration().getSucessfullyFinishedDocumentRequestsBy(
                currentExecutionYear, DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION, false);

        final Set<DocumentRequest> enrolmentDeclarations = getRegistration()
                .getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear, DocumentRequestType.ENROLMENT_DECLARATION, false);

        return ((schoolRegistrationDeclarations.size()
                + enrolmentDeclarations.size()) < MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR);
    }

}
