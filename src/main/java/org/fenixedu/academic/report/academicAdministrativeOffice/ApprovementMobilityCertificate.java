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
package org.fenixedu.academic.report.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.NoEctsComparabilityTableFound;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ApprovementMobilityCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.dto.administrativeOffice.documents.ApprovementMobilityCertificateBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class ApprovementMobilityCertificate extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected ApprovementMobilityCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        // addParameter("approvementsInfo", getApprovementsInfo());
        addParameter("mobilityProgram", getMobilityProgramDescription());
        addDataSourceElements(createApprovementsBean());
        addParameter("printPriceFieldsCert", !isMobility());
    }

    @Override
    public ApprovementMobilityCertificateRequest getDocumentRequest() {
        return (ApprovementMobilityCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        return getRegistration().getDegreeDescription(getExecutionYear(), (ProgramConclusion) null, getLocale());
    }

    /* ###################### */

    private boolean isMobility() {
        final RegistrationProtocol protocol = getDocumentRequest().getRegistration().getRegistrationProtocol();
        return protocol.isMobilityAgreement();
    }

    private String getMobilityProgramDescription() {
        return isMobility() ? getDocumentRequest().getRegistration().getRegistrationProtocol().getDescription()
                .getContent(getLocale()) : "";
    }

    final private void mapCycles(final SortedSet<ICurriculumEntry> entries) {
        final Collection<CycleCurriculumGroup> cycles =
                new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
        Registration registration = getDocumentRequest().getRegistration();
        for (CycleType cycleType : registration.getDegree().getCycleTypes()) {
            CycleCurriculumGroup cycleCurriculumGroup = registration.getStudentCurricularPlan(cycleType).getCycle(cycleType);
            if (cycleCurriculumGroup != null) {
                cycles.add(cycleCurriculumGroup);
            }
        }

        for (final CycleCurriculumGroup cycle : cycles) {
            if (!cycle.isConclusionProcessed() || isDEARegistration()) {
                final ApprovementMobilityCertificateRequest request = (getDocumentRequest());
                final Curriculum curriculum = cycle.getCurriculum(request.getFilteringDate());
                ApprovementMobilityCertificateRequest.filterEntries(entries, request, curriculum);
            }
        }
    }

    final private SortedSet<ICurriculumEntry> mapEntries() {
        final ApprovementMobilityCertificateRequest request = getDocumentRequest();

        final SortedSet<ICurriculumEntry> entries =
                new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

        final Registration registration = getDocumentRequest().getRegistration();
        if (registration.isBolonha()) {
            mapCycles(entries);
        } else {
            final ICurriculum curriculum = registration.getCurriculum(request.getFilteringDate());
            ApprovementMobilityCertificateRequest.filterEntries(entries, request, curriculum);
        }

        if (registration.getRegistrationProtocol().isMobilityAgreement()) {
            entries.addAll(request.getStandaloneEntriesToReport());
        }

        entries.addAll(request.getExtraCurricularEntriesToReport());
        entries.addAll(request.getPropaedeuticEntriesToReport());
        return entries;
    }

    final private List<ApprovementMobilityCertificateBean> createApprovementsBean() {
        SortedSet<ICurriculumEntry> entries = mapEntries();
        List<ApprovementMobilityCertificateBean> beans = new ArrayList<ApprovementMobilityCertificateBean>();
        final Map<Unit, String> ids = new HashMap<Unit, String>();
        for (final ICurriculumEntry entry : entries) {
            final ExecutionYear executionYear = entry.getExecutionYear();
            beans.add(new ApprovementMobilityCertificateBean(getCurriculumEntryName(ids, entry), entry
                    .getEctsCreditsForCurriculum().toString(), entry.getGradeValue(), getEctsGrade(entry), executionYear
                    .getYear()));
        }
        StringBuilder extraInfo = new StringBuilder();
        if (!ids.isEmpty()) {
            extraInfo.append(getAcademicUnitInfo(ids));
        }

        if (extraInfo.length() > 0) {
            addParameter("mobilityExtraInfo", extraInfo.toString());
        }

        return beans;
    }

    final private String getAcademicUnitInfo(final Map<Unit, String> unitIDs) {
        final StringBuilder result = new StringBuilder();
        String description = getMobilityProgramDescription();

        for (final Entry<Unit, String> academicUnitId : unitIDs.entrySet()) {
            final StringBuilder unit = new StringBuilder();

            unit.append(academicUnitId.getValue());
            unit.append(SINGLE_SPACE).append(
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.external.curricular.courses.one"));
            unit.append(SINGLE_SPACE).append(getMLSTextContent(academicUnitId.getKey().getPartyName()).toUpperCase());

            if (description.length() > 0) {
                unit.append(SINGLE_SPACE).append(
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.external.curricular.courses.two"));
                unit.append(SINGLE_SPACE).append(description);
            }
            result.append(unit.toString());
            result.append(LINE_BREAK);
        }

        return result.toString();
    }

    /* ###################### */

    // TODO: remove this after DEA diplomas and certificates
    private boolean isDEARegistration() {
        return getDocumentRequest().getRegistration().getDegreeType().isAdvancedSpecializationDiploma();
    }

    private String getEctsGradeDescription() {
        return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.ects.grade").concat(":");
    }

    private DateTime computeProcessingDateToLockECTSTableUse() {
        DateTime date = documentRequestDomainReference.getProcessingDate();
        return date != null ? date : new DateTime();
    }

    private String getEctsGrade(final ICurriculumEntry entry) {

        DateTime processingDate = computeProcessingDateToLockECTSTableUse();

        if (entry instanceof IEnrolment) {
            IEnrolment enrolment = (IEnrolment) entry;
            try {
                Grade grade =
                        enrolment.getEctsGrade(getDocumentRequest().getRegistration().getLastStudentCurricularPlan(),
                                processingDate);
                if (grade == null) {
                    throw new DomainException(Optional.of(Bundle.ACADEMIC), "error.missing.ects.grade.for.enrolment",
                            enrolment.getDescription());
                }
                return grade.getValue();
            } catch (NoEctsComparabilityTableFound nectfe) {
                return "--";
            }
        } else if (entry instanceof Dismissal && ((Dismissal) entry).getCredits().isEquivalence()) {
            Dismissal dismissal = ((Dismissal) entry);
            try {
                Grade grade = dismissal.getEctsGrade(processingDate);
                return grade.getValue();
            } catch (NoEctsComparabilityTableFound nectfe) {
                return "--";
            }
        } else {
            throw new Error("The roof is yet again on fire!");
        }
    }

    @Override
    protected String getCreditsAndGradeInfo(final ICurriculumEntry entry, final ExecutionYear executionYear) {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {
            getCreditsInfo(result, entry);
        }
        result.append(entry.getGradeValue());
        result.append(StringUtils.rightPad("(" + BundleUtil.getString(Bundle.ENUMERATION, getLocale(), entry.getGradeValue())
                + ")", SUFFIX_LENGTH, ' '));

        result.append(", ");

        result.append(getEctsGradeDescription());
        result.append(SINGLE_SPACE);
        result.append(getEctsGrade(entry));
        result.append(SINGLE_SPACE);
        result.append(", ");

        final String in = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.in");
        if (executionYear == null) {
            result.append(StringUtils.rightPad(EMPTY_STR, in.length(), ' '));
            result.append(SINGLE_SPACE).append(StringUtils.rightPad(EMPTY_STR, 9, ' '));
        } else {
            result.append(in);
            result.append(SINGLE_SPACE).append(executionYear.getYear());
        }

        return result.toString();
    }

    @Override
    protected void addPriceFields() {
        final CertificateRequest certificateRequest = getDocumentRequest();
        final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();

        final Money amountPerPage = certificateRequestPR.getAmountPerPage();
        final Money baseAmountPlusAmountForUnits =
                certificateRequestPR.getBaseAmount().add(
                        certificateRequestPR.getAmountForUnits(certificateRequest.getNumberOfUnits()));
        final Money urgencyAmount = certificateRequest.getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;

        addParameter("amountPerPage", amountPerPage);
        addParameter("baseAmountPlusAmountForUnits", baseAmountPlusAmountForUnits);
        addParameter("urgencyAmount", urgencyAmount);
        addParameter("printPriceFields", printPriceParameters(certificateRequest));
    }

    @Override
    protected void setPersonFields() {
        final Person person = getDocumentRequest().getPerson();

        StringBuilder builder1 = new StringBuilder();
        builder1.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.with"));
        builder1.append(SINGLE_SPACE).append(person.getIdDocumentType().getLocalizedName(getLocale()));
        builder1.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.number.short"));
        builder1.append(SINGLE_SPACE).append(person.getDocumentIdNumber());

        addParameter("name", person.getName().toUpperCase());
        addParameter("documentIdNumber", builder1.toString());

        setNationality(person);
    }

}
