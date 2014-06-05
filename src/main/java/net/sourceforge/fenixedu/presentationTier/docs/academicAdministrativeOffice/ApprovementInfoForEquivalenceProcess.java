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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ApprovementInfoForEquivalenceProcess {

    static final protected String[] identifiers = { "*", "#", "+", "**", "***" };

    static final protected String LINE_BREAK = "\n";

    static final protected String SINGLE_SPACE = " ";

    public static String getApprovementsInfo(final Registration registration) {

        final StringBuilder res = new StringBuilder();

        final SortedSet<ICurriculumEntry> entries =
                new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

        final Map<Unit, String> ids = new HashMap<Unit, String>();
        if (registration.isBolonha()) {
            reportCycles(res, entries, ids, registration);
        } else {
            final ICurriculum curriculum = registration.getCurriculum();
            filterEntries(entries, curriculum);
            reportEntries(res, entries, ids, registration);
        }

        entries.clear();
        entries.addAll(getExtraCurricularEntriesToReport(registration));
        if (!entries.isEmpty()) {
            reportRemainingEntries(res, entries, ids, registration.getLastStudentCurricularPlan().getExtraCurriculumGroup(),
                    registration);
        }

        entries.clear();
        entries.addAll(getPropaedeuticEntriesToReport(registration));
        if (!entries.isEmpty()) {
            reportRemainingEntries(res, entries, ids, registration.getLastStudentCurricularPlan()
                    .getPropaedeuticCurriculumGroup(), registration);
        }

        res.append(getRemainingCreditsInfo(registration.getCurriculum()));

        res.append(LINE_BREAK);

        if (!ids.isEmpty()) {
            res.append(LINE_BREAK).append(getAcademicUnitInfo(ids));
        }

        return res.toString();
    }

    static protected String getAcademicUnitInfo(final Map<Unit, String> unitIDs) {
        final StringBuilder result = new StringBuilder();

        for (final Entry<Unit, String> academicUnitId : unitIDs.entrySet()) {
            final StringBuilder unit = new StringBuilder();

            unit.append(academicUnitId.getValue());
            unit.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, "documents.external.curricular.courses.one"));
            unit.append(SINGLE_SPACE).append(getMLSTextContent(academicUnitId.getKey().getPartyName()));
            result.append(unit.toString());
            result.append(LINE_BREAK);
        }

        return result.toString();
    }

    private static String getRemainingCreditsInfo(final ICurriculum curriculum) {
        final BigDecimal remainingCredits = curriculum.getRemainingCredits();

        final StringBuilder result = new StringBuilder();
        if (remainingCredits != BigDecimal.ZERO) {
            result.append(LINE_BREAK);

            final String remainingCreditsInfo = BundleUtil.getString(Bundle.ACADEMIC, "documents.remainingCreditsInfo");
            result.append(remainingCreditsInfo + ":" + remainingCredits);

            result.append(LINE_BREAK);
        }

        return result.toString();
    }

    static private void reportEntries(final StringBuilder result, final Collection<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers, final Registration registration) {
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

            reportEntry(result, entry, academicUnitIdentifiers, executionYear, registration);
        }
    }

    static private void reportCycles(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers, final Registration registration) {
        final Collection<CycleCurriculumGroup> cycles =
                new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
        cycles.addAll(registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());

        CycleCurriculumGroup lastReported = null;
        for (final CycleCurriculumGroup cycle : cycles) {
            if (!cycle.isConclusionProcessed() || isDEARegistration(registration)) {
                // final ApprovementCertificateRequest request =
                // ((ApprovementCertificateRequest) getDocumentRequest());
                final Curriculum curriculum = cycle.getCurriculum();
                filterEntries(entries, curriculum);

                if (!entries.isEmpty()) {
                    if (lastReported == null) {
                        lastReported = cycle;
                    } else {
                        result.append(LINE_BREAK);
                    }

                    result.append(getMLSTextContent(cycle.getName())).append(":").append(LINE_BREAK);
                    reportEntries(result, entries, academicUnitIdentifiers, registration);
                }

                entries.clear();
            }
        }
    }

    static final public void filterEntries(final Collection<ICurriculumEntry> result, final ICurriculum curriculum) {
        for (final ICurriculumEntry entry : curriculum.getCurriculumEntries()) {
            if (entry instanceof Dismissal) {
                final Dismissal dismissal = (Dismissal) entry;
                if (dismissal.getCredits().isEquivalence()
                        || (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
                    continue;
                }
            }

            result.add(entry);
        }
    }

    private static boolean isDEARegistration(Registration registration) {
        return registration.getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    static private void reportRemainingEntries(final StringBuilder result, final Collection<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers, final NoCourseGroupCurriculumGroup group,
            final Registration registration) {
        result.append(LINE_BREAK).append(getMLSTextContent(group.getName())).append(":").append(LINE_BREAK);

        for (final ICurriculumEntry entry : entries) {
            reportEntry(result, entry, academicUnitIdentifiers, entry.getExecutionYear(), registration);
        }
    }

    static private void reportEntry(final StringBuilder result, final ICurriculumEntry entry,
            final Map<Unit, String> academicUnitIdentifiers, final ExecutionYear executionYear, final Registration registration) {
        result.append(getCurriculumEntryName(academicUnitIdentifiers, entry))
                .append(getCreditsAndGradeInfo(entry, executionYear, registration)).append(LINE_BREAK);
    }

    static protected String getCurriculumEntryName(final Map<Unit, String> academicUnitIdentifiers, final ICurriculumEntry entry) {
        final StringBuilder result = new StringBuilder();

        if (entry instanceof ExternalEnrolment) {
            result.append(getAcademicUnitIdentifier(academicUnitIdentifiers, ((ExternalEnrolment) entry).getAcademicUnit()));
        }
        result.append(getPresentationNameFor(entry));

        return result.toString();
    }

    static protected String getAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers, final Unit academicUnit) {
        if (!academicUnitIdentifiers.containsKey(academicUnit)) {
            academicUnitIdentifiers.put(academicUnit, identifiers[academicUnitIdentifiers.size()]);
        }

        return academicUnitIdentifiers.get(academicUnit);
    }

    static protected String getPresentationNameFor(final ICurriculumEntry entry) {
        final MultiLanguageString result;

        if (entry instanceof OptionalEnrolment) {
            final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) entry;
            result = optionalEnrolment.getCurricularCourse().getNameI18N();
        } else {
            result = entry.getName();
        }

        return getMLSTextContent(result);
    }

    final static String getCreditsAndGradeInfo(final ICurriculumEntry entry, final ExecutionYear executionYear,
            final Registration registration) {
        final StringBuilder result = new StringBuilder();

        result.append(SINGLE_SPACE);
        getCreditsInfo(result, entry, registration);
        result.append(entry.getGradeValue());
        result.append("(" + BundleUtil.getString(Bundle.ENUMERATION, entry.getGradeValue()) + ")");

        result.append(SINGLE_SPACE);
        final String in = BundleUtil.getString(Bundle.ACADEMIC, "label.in");
        if (executionYear != null) {
            result.append(in);
            result.append(SINGLE_SPACE).append(executionYear.getYear());
        }

        return result.toString();
    }

    static protected void getCreditsInfo(final StringBuilder result, final ICurriculumEntry entry, final Registration registration) {
        result.append(entry.getEctsCreditsForCurriculum()).append(getCreditsDescription(registration)).append(", ");
    }

    static private String getCreditsDescription(final Registration registration) {
        return registration.getDegreeType().getCreditsDescription();
    }

    static private Collection<ICurriculumEntry> getExtraCurricularEntriesToReport(final Registration registration) {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        reportApprovedCurriculumLines(result, calculateExtraCurriculumLines(registration));
        reportExternalGroups(result, registration);

        return result;
    }

    static private Collection<CurriculumLine> calculateExtraCurriculumLines(final Registration registration) {
        final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

        for (final CurriculumLine line : registration.getExtraCurricularCurriculumLines()) {
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

    static private void reportApprovedCurriculumLines(final Collection<ICurriculumEntry> result,
            final Collection<CurriculumLine> lines) {
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

    static private void reportExternalGroups(final Collection<ICurriculumEntry> result, final Registration registration) {
        for (final ExternalCurriculumGroup group : registration.getLastStudentCurricularPlan().getExternalCurriculumGroups()) {
            filterEntries(result, group.getCurriculumInAdvance());
        }
    }

    static private Collection<ICurriculumEntry> getPropaedeuticEntriesToReport(final Registration registration) {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        reportApprovedCurriculumLines(result, registration.getPropaedeuticCurriculumLines());

        return result;
    }

    protected static String getMLSTextContent(final MultiLanguageString mls) {
        if (mls == null) {
            return StringUtils.EMPTY;
        }
        final String content = mls.hasContent() && !StringUtils.isEmpty(mls.getContent()) ? mls.getContent() : mls.getContent();
        return content == null ? StringUtils.EMPTY : convert(content);
    }

    static private String convert(final String content) {
        return HtmlToTextConverterUtil.convertToText(content).replace("\n\n", "\t").replace(LINE_BREAK, StringUtils.EMPTY)
                .replace("\t", "\n\n").trim();
    }

}
