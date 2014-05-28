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
package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

import org.joda.time.DateTime;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

    protected ApprovementCertificateRequest() {
        super();
    }

    public ApprovementCertificateRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setMobilityProgram(bean.getMobilityProgram());
        super.setIgnoreExternalEntries(bean.isIgnoreExternalEntries());
        super.setIgnoreCurriculumInAdvance(bean.isIgnoreCurriculumInAdvance());

        // TODO: remove this after DEA diplomas and certificates
        if (!isDEARegistration()) {

            if (getRegistration().isConcluded()) {
                throw new DomainException("ApprovementCertificateRequest.registration.is.concluded");
            }

            if (getRegistration().isRegistrationConclusionProcessed()) {
                throw new DomainException("ApprovementCertificateRequest.registration.has.conclusion.processed");
            }
        }

        if (getEntriesToReport(isDEARegistration()).isEmpty()) {
            throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
        }
    }

    // TODO: remove this after DEA diplomas and certificates
    private boolean isDEARegistration() {
        return getRegistration().getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
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

            // TODO: remove this after DEA diplomas and certificate
            if (!isDEARegistration()) {

                if (getRegistration().isConcluded()) {
                    throw new DomainException("ApprovementCertificateRequest.registration.is.concluded");
                }

                if (getRegistration().isRegistrationConclusionProcessed()) {
                    throw new DomainException("ApprovementCertificateRequest.registration.has.conclusion.processed");
                }
            }

            if (getEntriesToReport(isDEARegistration()).isEmpty()) {
                throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
            }

            // FIXME For now, the following conditions are only valid for 5year
            // degrees
            if (getRegistration().getDegreeType().getYears() == 5 && getDocumentPurposeType() == DocumentPurposeType.PROFESSIONAL) {

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
        return DocumentRequestType.APPROVEMENT_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
        return RegistrationAgreement.EXEMPTED_AGREEMENTS.contains(getRegistration().getRegistrationAgreement()) ? null : EventType.APPROVEMENT_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
        final Integer res = super.getNumberOfUnits();
        return res == null ? calculateNumberOfUnits() : res;
    }

    private int calculateNumberOfUnits() {
        return getEntriesToReport(isDEARegistration()).size() + getExtraCurricularEntriesToReport().size()
                + getPropaedeuticEntriesToReport().size();
    }

    @Override
    final public void setNumberOfUnits(final Integer numberOfUnits) {
        throw new DomainException("error.ApprovementCertificateRequest.cannot.modify.numberOfUnits");
    }

    @Override
    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        throw new DomainException("error.ApprovementCertificateRequest.cannot.modify");
    }

    @Override
    public void setIgnoreExternalEntries(Boolean ignoreExternalEntries) {
        throw new DomainException("error.ApprovementCertificateRequest.cannot.modify");
    }

    @Override
    public boolean isToPrint() {
        final Integer units = super.getNumberOfUnits();
        return !hasConcluded() || units != null && units.intValue() == calculateNumberOfUnits();
    }

    final private Collection<ICurriculumEntry> getEntriesToReport(final boolean useConcluded) {
        final HashSet<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        final Registration registration = getRegistration();
        ICurriculum curriculum;
        if (registration.isBolonha()) {
            for (final CycleCurriculumGroup cycle : registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops()) {
                if (cycle.hasAnyApprovedCurriculumLines() && (useConcluded || !cycle.isConclusionProcessed())) {
                    curriculum = cycle.getCurriculum(getFilteringDate());
                    filterEntries(result, this, curriculum);
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
            final ApprovementCertificateRequest request, final ICurriculum curriculum) {
        for (final ICurriculumEntry entry : curriculum.getCurriculumEntries()) {
            if (entry instanceof Dismissal) {
                final Dismissal dismissal = (Dismissal) entry;
                if (dismissal.getCredits().isEquivalence() || dismissal.isCreditsDismissal()
                        && !dismissal.getCredits().isSubstitution()) {
                    continue;
                }
            } else if (entry instanceof ExternalEnrolment && request.getIgnoreExternalEntries()) {
                continue;
            }

            result.add(entry);
        }
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
        if (getIgnoreCurriculumInAdvance() != null && !getIgnoreCurriculumInAdvance()) {
            for (final ExternalCurriculumGroup group : getRegistration().getLastStudentCurricularPlan()
                    .getExternalCurriculumGroups()) {
                filterEntries(result, this, group.getCurriculumInAdvance(getFilteringDate()));
            }
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

    @Deprecated
    public boolean hasNumberOfUnits() {
        return getNumberOfUnits() != null;
    }

    @Deprecated
    public boolean hasIgnoreExternalEntries() {
        return getIgnoreExternalEntries() != null;
    }

    @Deprecated
    public boolean hasIgnoreCurriculumInAdvance() {
        return getIgnoreCurriculumInAdvance() != null;
    }

    @Deprecated
    public boolean hasMobilityProgram() {
        return getMobilityProgram() != null;
    }

}
