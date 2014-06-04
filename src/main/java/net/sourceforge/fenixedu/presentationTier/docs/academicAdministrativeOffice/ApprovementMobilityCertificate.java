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
package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.documents.ApprovementMobilityCertificateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.NoEctsComparabilityTableFound;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementMobilityCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
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
    protected ApprovementMobilityCertificateRequest getDocumentRequest() {
        return (ApprovementMobilityCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        return getDocumentRequest().getRegistration().getDegreeDescription(getDocumentRequest().getExecutionYear(), null,
                getLocale());
    }

    /* ###################### */

    private boolean isMobility() {
        RegistrationAgreement agreement = getDocumentRequest().getRegistration().getRegistrationAgreement();

        if (RegistrationAgreement.MOBILITY_AGREEMENTS.contains(agreement)) {
            return true;
        }
        return false;
    }

    private String getMobilityProgramDescription() {

        if (isMobility()) {
            return getDocumentRequest().getRegistration().getRegistrationAgreement().getDescription(getLocale());
        }

        return "";
    }

    final private void mapCycles(final SortedSet<ICurriculumEntry> entries) {
        final Collection<CycleCurriculumGroup> cycles =
                new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
        cycles.addAll(getDocumentRequest().getRegistration().getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());

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

        if (RegistrationAgreement.MOBILITY_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
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
            unit.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, "documents.external.curricular.courses.one"));
            unit.append(SINGLE_SPACE).append(getMLSTextContent(academicUnitId.getKey().getPartyName()).toUpperCase());

            if (description.length() > 0) {
                unit.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, "documents.external.curricular.courses.two"));
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
        return getDocumentRequest().getRegistration().getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    private String getEctsGradeDescription() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.ects.grade").concat(":");
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
        result.append(StringUtils.rightPad("(" + BundleUtil.getString(Bundle.ENUMERATION, entry.getGradeValue()) + ")", SUFFIX_LENGTH,
                ' '));

        result.append(", ");

        result.append(getEctsGradeDescription());
        result.append(SINGLE_SPACE);
        result.append(getEctsGrade(entry));
        result.append(SINGLE_SPACE);
        result.append(", ");

        final String in = BundleUtil.getString(Bundle.ACADEMIC, "label.in");
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
        builder1.append(BundleUtil.getString(Bundle.ACADEMIC, "label.with"));
        builder1.append(SINGLE_SPACE).append(person.getIdDocumentType().getLocalizedName(getLocale()));
        builder1.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, "label.number.short"));
        builder1.append(SINGLE_SPACE).append(person.getDocumentIdNumber());

        addParameter("name", person.getName().toUpperCase());
        addParameter("documentIdNumber", builder1.toString());

        setNationality(person);
    }

}
