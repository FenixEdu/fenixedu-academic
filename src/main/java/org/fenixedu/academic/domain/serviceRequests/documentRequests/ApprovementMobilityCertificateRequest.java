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

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.StandaloneCurriculumGroup;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.joda.time.DateTime;

public class ApprovementMobilityCertificateRequest extends ApprovementMobilityCertificateRequest_Base {

    protected ApprovementMobilityCertificateRequest() {
        super();
    }

    public ApprovementMobilityCertificateRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);

        if (getEntriesToReport().isEmpty()) {
            throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
        }
    }

    @Override
    public boolean isFreeProcessed() {
        return getDocumentRequestType() == DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE
                && (getRegistration().getRegistrationProtocol().isMobilityAgreement() || getRegistration()
                        .getRegistrationProtocol().isExempted());
    }

    @Override
    final protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getMobilityProgram() != null && bean.isIgnoreExternalEntries()) {
            throw new DomainException("ApprovementCertificateRequest.cannot.ignore.external.entries.within.a.mobility.program");
        }
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {

            if (getEntriesToReport().isEmpty()) {
                throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
            }

            // FIXME For now, the following conditions are only valid for 5year
            // degrees
            if (getRegistration().getLastStudentCurricularPlan().getDegreeCurricularPlan().getDurationInYears() == 5
                    && getDocumentPurposeType() == DocumentPurposeType.PROFESSIONAL) {

                int curricularYear = getRegistration().getCurricularYear();

                if (curricularYear <= 3) {
                    throw new DomainException("ApprovementCertificateRequest.registration.hasnt.finished.third.year");
                }
            }
        }

        if (academicServiceRequestBean.isToConclude()) {
            super.setNumberOfUnits(calculateNumberOfUnits());
        }
    }

    @Override
    protected boolean isPayed() {
        return super.isPayed() || getEvent().isCancelled();
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
        final RegistrationProtocol protocol = getRegistration().getRegistrationProtocol();
        return protocol.isExempted() || protocol.isMobilityAgreement() ? null : EventType.APPROVEMENT_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
        final Integer res = super.getNumberOfUnits();
        return res == null ? calculateNumberOfUnits() : res;
    }

    private int calculateNumberOfUnits() {
        return getEntriesToReport().size() + getExtraCurricularEntriesToReport().size()
                + getPropaedeuticEntriesToReport().size();
    }

    @Override
    final public void setNumberOfUnits(final Integer numberOfUnits) {
        throw new DomainException("error.ApprovementCertificateRequest.cannot.modify.numberOfUnits");
    }

    @Override
    public boolean isToPrint() {
        final Integer units = super.getNumberOfUnits();
        return !hasConcluded() || units != null && units.intValue() == calculateNumberOfUnits();
    }

    final private Collection<ICurriculumEntry> getEntriesToReport() {
        final HashSet<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        final Registration registration = getRegistration();
        ICurriculum curriculum;
        if (registration.isBolonha()) {
            for (CycleType cycleType : registration.getDegree().getCycleTypes()) {
                CycleCurriculumGroup cycle = registration.getStudentCurricularPlan(cycleType).getCycle(cycleType);
                if (cycle != null && cycle.hasAnyApprovedCurriculumLines()) {
                    curriculum = cycle.getCurriculum(getFilteringDate());
                    filterEntries(result, this, curriculum);
                }
            }
            if (isMobilityStudent()) {
                final StandaloneCurriculumGroup standalone =
                        registration.getLastStudentCurricularPlan().getStandaloneCurriculumGroup();
                if (standalone != null && standalone.hasAnyApprovedCurriculumLines()) {
                    result.addAll(getStandaloneEntriesToReport());
                }
            }
        } else {
            curriculum = getRegistration().getCurriculum(getFilteringDate());
            filterEntries(result, this, curriculum);
        }

        return result;
    }

    public DateTime getFilteringDate() {
        return hasConcluded() ? getRequestConclusionDate() : new DateTime();
    }

    static final public void filterEntries(final Collection<ICurriculumEntry> result,
            final ApprovementMobilityCertificateRequest request, final ICurriculum curriculum) {
        for (final ICurriculumEntry entry : curriculum.getCurriculumEntries()) {
            if (entry instanceof Dismissal) {
                final Dismissal dismissal = (Dismissal) entry;
                if (dismissal.getCredits().isEquivalence() || dismissal.isCreditsDismissal()
                        && !dismissal.getCredits().isSubstitution()) {
                    continue;
                }
            }
            result.add(entry);
        }
    }

    final public Collection<ICurriculumEntry> getStandaloneEntriesToReport() {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        reportApprovedCurriculumLines(result, calculateStandaloneCurriculumLines());
        // reportExternalGroups(result);

        return result;
    }

    final public Collection<ICurriculumEntry> getExtraCurricularEntriesToReport() {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        reportApprovedCurriculumLines(result, calculateExtraCurriculumLines());
        reportExternalGroups(result);

        return result;
    }

    private Collection<CurriculumLine> calculateExtraCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

        for (final CurriculumLine line : getRegistration().getExtraCurricularCurriculumLines()) {
            if (line.isEnrolment()) {
                if (!((Enrolment) line).isSourceOfAnyCreditsInCurriculum()) {
                    result.add(line);
                }
            } else {
                result.add(line);
            }
        }

        return result;
    }

    private Collection<CurriculumLine> calculateStandaloneCurriculumLines() {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

        for (final CurriculumLine line : getRegistration().getStandaloneCurriculumLines()) {
            if (line.isEnrolment()) {
                if (!((Enrolment) line).isSourceOfAnyCreditsInCurriculum()) {
                    result.add(line);
                }
            } else {
                result.add(line);
            }
        }

        return result;
    }

    private void reportApprovedCurriculumLines(final Collection<ICurriculumEntry> result, final Collection<CurriculumLine> lines) {
        for (final CurriculumLine line : lines) {
            if (line.isApproved()) {
                if (line.isEnrolment()) {
                    result.add((IEnrolment) line);
                } else if (line.isDismissal() && ((Dismissal) line).getCredits().isSubstitution()) {
                    result.addAll(((Dismissal) line).getSourceIEnrolments());
                }
            }
        }
    }

    private void reportExternalGroups(final Collection<ICurriculumEntry> result) {
        for (final ExternalCurriculumGroup group : getRegistration().getLastStudentCurricularPlan().getExternalCurriculumGroups()) {
            filterEntries(result, this, group.getCurriculumInAdvance(getFilteringDate()));
        }
    }

    final public Collection<ICurriculumEntry> getPropaedeuticEntriesToReport() {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        reportApprovedCurriculumLines(result, getRegistration().getPropaedeuticCurriculumLines());

        return result;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    protected boolean hasMissingPersonalInfo() {
        // no need to test for parishOfBirth or districtOfBirth
        // see super for more details
        return false;
    }

    private boolean isMobilityStudent() {
        return getRegistration().getRegistrationProtocol().isMobilityAgreement();
    }

}
