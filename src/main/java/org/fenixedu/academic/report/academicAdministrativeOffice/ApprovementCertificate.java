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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.util.FenixStringTools;
import net.sourceforge.fenixedu.util.Money;

public class ApprovementCertificate extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected ApprovementCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        addParameter("approvementsInfo", getApprovementsInfo());
    }

    @Override
    protected ApprovementCertificateRequest getDocumentRequest() {
        return (ApprovementCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        return getDocumentRequest().getRegistration().getDegreeDescription(getDocumentRequest().getExecutionYear(), null,
                getLocale());
    }

    final private String getApprovementsInfo() {
        final ApprovementCertificateRequest request = getDocumentRequest();

        final StringBuilder res = new StringBuilder();

        final SortedSet<ICurriculumEntry> entries =
                new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

        final Registration registration = getDocumentRequest().getRegistration();
        final Map<Unit, String> ids = new HashMap<Unit, String>();
        if (registration.isBolonha()) {
            reportCycles(res, entries, ids);
        } else {
            final ICurriculum curriculum = registration.getCurriculum(request.getFilteringDate());
            ApprovementCertificateRequest.filterEntries(entries, request, curriculum);
            reportEntries(res, entries, ids);
        }

        entries.clear();
        entries.addAll(request.getExtraCurricularEntriesToReport());
        if (!entries.isEmpty()) {
            reportRemainingEntries(res, entries, ids, registration.getLastStudentCurricularPlan().getExtraCurriculumGroup());
        }

        entries.clear();
        entries.addAll(request.getPropaedeuticEntriesToReport());
        if (!entries.isEmpty()) {
            reportRemainingEntries(res, entries, ids, registration.getLastStudentCurricularPlan()
                    .getPropaedeuticCurriculumGroup());
        }

        if (getDocumentRequest().isToShowCredits()) {
            res.append(getRemainingCreditsInfo(request.getRegistration().getCurriculum()));
        }

        res.append(generateEndLine());

        if (!ids.isEmpty()) {
            res.append(LINE_BREAK).append(getAcademicUnitInfo(ids, request.getMobilityProgram()));
        }

        return res.toString();
    }

    final private void reportEntries(final StringBuilder result, final Collection<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers) {
        ExecutionYear lastReportedExecutionYear = null;
        for (final ICurriculumEntry entry : entries) {
            final ExecutionYear executionYear = entry.getExecutionYear();
            if (lastReportedExecutionYear == null) {
                lastReportedExecutionYear = executionYear;
            }

            if (executionYear != lastReportedExecutionYear) {
                lastReportedExecutionYear = executionYear;
                // result.append(LINE_BREAK);
            }

            reportEntry(result, entry, academicUnitIdentifiers, executionYear);
        }
    }

    final private void reportCycles(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers) {
        final Collection<CycleCurriculumGroup> cycles =
                new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
        cycles.addAll(getDocumentRequest().getRegistration().getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());

        CycleCurriculumGroup lastReported = null;
        for (final CycleCurriculumGroup cycle : cycles) {
            if (!cycle.isConclusionProcessed() || isDEARegistration()) {
                final ApprovementCertificateRequest request = (getDocumentRequest());
                final Curriculum curriculum = cycle.getCurriculum(request.getFilteringDate());
                ApprovementCertificateRequest.filterEntries(entries, request, curriculum);

                if (!entries.isEmpty()) {
                    if (lastReported == null) {
                        lastReported = cycle;
                    } else {
                        result.append(generateEndLine()).append(LINE_BREAK);
                    }

                    result.append(getMLSTextContent(cycle.getName())).append(":").append(LINE_BREAK);
                    reportEntries(result, entries, academicUnitIdentifiers);
                }

                entries.clear();
            }
        }
    }

    // TODO: remove this after DEA diplomas and certificates
    private boolean isDEARegistration() {
        return getDocumentRequest().getRegistration().getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    final private void reportRemainingEntries(final StringBuilder result, final Collection<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers, final NoCourseGroupCurriculumGroup group) {
        result.append(generateEndLine()).append(LINE_BREAK).append(getMLSTextContent(group.getName())).append(":")
                .append(LINE_BREAK);

        for (final ICurriculumEntry entry : entries) {
            reportEntry(result, entry, academicUnitIdentifiers, entry.getExecutionYear());
        }
    }

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry,
            final Map<Unit, String> academicUnitIdentifiers, final ExecutionYear executionYear) {
        result.append(
                FenixStringTools.multipleLineRightPadWithSuffix(getCurriculumEntryName(academicUnitIdentifiers, entry),
                        LINE_LENGTH, END_CHAR, getCreditsAndGradeInfo(entry, executionYear))).append(LINE_BREAK);
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

}
