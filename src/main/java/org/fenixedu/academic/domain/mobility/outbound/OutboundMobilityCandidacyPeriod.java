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
package org.fenixedu.academic.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityProgram;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.period.CandidacyPeriod;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacyPeriod extends OutboundMobilityCandidacyPeriod_Base implements Comparable<CandidacyPeriod> {

    public OutboundMobilityCandidacyPeriod(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        super();
        init(executionInterval, start, end);

        final OutboundMobilityCandidacyPeriod previousPeriod = findPreviousPeriod();
        for (final OutboundMobilityCandidacyContestGroup group : previousPeriod.getOutboundMobilityCandidacyContestGroupSet()) {
            if (!group.getExecutionDegreeSet().isEmpty()) {
                final OutboundMobilityCandidacyContestGroup newGroup =
                        new OutboundMobilityCandidacyContestGroup(group.getExecutionDegreeSet());
                for (final OutboundMobilityCandidacyContest contest : group.getOutboundMobilityCandidacyContestSet()) {
                    final OutboundMobilityCandidacyContest newContest =
                            new OutboundMobilityCandidacyContest(this, newGroup, contest.getMobilityAgreement(),
                                    contest.getVacancies());
                    newGroup.getOutboundMobilityCandidacyContestSet().add(newContest);
                }
                newGroup.getMobilityCoordinatorSet().addAll(group.getMobilityCoordinatorSet());
            }
        }
        for (final OutboundMobilityCandidacyContest contest : previousPeriod.getOutboundMobilityCandidacyContestSet()) {
            contest.getExecutionDegreeSet();
            contest.getMobilityAgreement();
            contest.getOutboundMobilityCandidacyContestGroup();
            contest.getVacancies();
        }
    }

    @Atomic
    public static OutboundMobilityCandidacyPeriod create(final ExecutionInterval executionInterval, final DateTime start,
            final DateTime end) {
        return new OutboundMobilityCandidacyPeriod(executionInterval, start, end);
    }

    @Atomic
    public OutboundMobilityCandidacyContest createOutboundMobilityCandidacyContest(final ExecutionDegree executionDegree,
            final MobilityProgram mobilityProgram, final UniversityUnit unit, final Integer vacancies) {
        final MobilityAgreement mobilityAgreement = findOrCreateMobilityAgreement(mobilityProgram, unit);
        final OutboundMobilityCandidacyContestGroup mobilityGroup =
                OutboundMobilityCandidacyContestGroup.findOrCreateGroup(executionDegree);
        final OutboundMobilityCandidacyContest contest =
                new OutboundMobilityCandidacyContest(this, mobilityGroup, mobilityAgreement, vacancies);

        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        mobilityGroup.getOutboundMobilityCandidacyContestSet().size();
        return contest;
    }

    @Atomic
    public OutboundMobilityCandidacyContest createOutboundMobilityCandidacyContest(
            final OutboundMobilityCandidacyContestGroup mobilityGroup, final MobilityProgram mobilityProgram,
            final UniversityUnit unit, final Integer vacancies) {
        final MobilityAgreement mobilityAgreement = findOrCreateMobilityAgreement(mobilityProgram, unit);
        return new OutboundMobilityCandidacyContest(this, mobilityGroup, mobilityAgreement, vacancies);
    }

    private MobilityAgreement findOrCreateMobilityAgreement(final MobilityProgram mobilityProgram, final UniversityUnit unit) {
        for (final MobilityAgreement mobilityAgreement : mobilityProgram.getMobilityAgreementsSet()) {
            if (mobilityAgreement.getUniversityUnit() == unit) {
                return mobilityAgreement;
            }
        }
        return new MobilityAgreement(mobilityProgram, unit);
    }

    @Override
    public int compareTo(final CandidacyPeriod cp) {
        int i = getStart().compareTo(cp.getStart());
        return i == 0 ? getExternalId().compareTo(cp.getExternalId()) : i;
    }

    public Interval getInterval() {
        return new Interval(getStart(), getEnd());
    }

    public String getIntervalAsString() {
        return getStart().toString("yyyy/MM/dd HH:mm") + " - " + getEnd().toString("yyyy/MM/dd HH:mm");
    }

    public SortedSet<OutboundMobilityCandidacyContest> getSortedOutboundMobilityCandidacyContest() {
        return new TreeSet<OutboundMobilityCandidacyContest>(getOutboundMobilityCandidacyContestSet());
    }

    public boolean isAcceptingCandidacies() {
        return isOpen();
    }

    public SortedSet<OutboundMobilityCandidacyContestGroup> getOutboundMobilityCandidacyContestGroupSet() {
        final SortedSet<OutboundMobilityCandidacyContestGroup> result = new TreeSet<OutboundMobilityCandidacyContestGroup>();
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            result.add(contest.getOutboundMobilityCandidacyContestGroup());
        }
        return result;
    }

    public OutboundMobilityCandidacySubmission findSubmissionFor(final Person person) {
        for (final Registration registration : person.getStudent().getRegistrationsSet()) {
            for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
                if (submission.getOutboundMobilityCandidacyPeriod() == this) {
                    return submission;
                }
            }
        }
        return null;
    }

    @Atomic
    public void setOptionIntroductoryDestriptionService(final String optionIntroductoryDestription) {
        setOptionIntroductoryDestription(optionIntroductoryDestription);
    }

    @Atomic
    public void addOption(final String optionValue, final Boolean availableForCandidates) {
        new OutboundMobilityCandidacyPeriodConfirmationOption(this, optionValue, availableForCandidates);
    }

    public SortedSet<OutboundMobilityCandidacyPeriodConfirmationOption> getSortedOptions() {
        return new TreeSet<OutboundMobilityCandidacyPeriodConfirmationOption>(
                getOutboundMobilityCandidacyPeriodConfirmationOptionSet());
    }

    public SortedSet<OutboundMobilityCandidacySubmission> getSortedSubmissionSet(
            final OutboundMobilityCandidacyContestGroup mobilityGroup) {
        final SortedSet<OutboundMobilityCandidacySubmission> result =
                new TreeSet<OutboundMobilityCandidacySubmission>(new Comparator<OutboundMobilityCandidacySubmission>() {
                    @Override
                    public int compare(OutboundMobilityCandidacySubmission o1, OutboundMobilityCandidacySubmission o2) {
                        final BigDecimal grade1 = o1.getGradeForSerialization(mobilityGroup);
                        final BigDecimal grade2 = o2.getGradeForSerialization(mobilityGroup);
                        if ((grade1 == null && grade2 == null) || (grade1 != null && grade1.equals(grade2))) {
                            return o1.compareTo(o2);
                        }
                        return grade1 == null ? 1 : grade2 == null ? -1 : grade2.compareTo(grade1);
                    }
                });
        for (final OutboundMobilityCandidacySubmission submission : getOutboundMobilityCandidacySubmissionSet()) {
            if (submission.hasContestInGroup(mobilityGroup)) {
                result.add(submission);
            }
        }
        return result;
    }

    public Spreadsheet getSelectedCandidateSpreadSheet(final OutboundMobilityCandidacyPeriod period) {
        final String filename =
                BundleUtil.getString(Bundle.ACADEMIC, "label.mobility.outbound.period.export.selected.candiadates.filename");

        final Spreadsheet spreadsheetCandidates = new Spreadsheet(filename);
        final Spreadsheet spreadsheetOtherCurricularInfo =
                spreadsheetCandidates.addSpreadsheet(BundleUtil.getString(Bundle.ACADEMIC, "label.other.curricular.info"));
        for (final OutboundMobilityCandidacySubmission submission : getOutboundMobilityCandidacySubmissionSet()) {
            final OutboundMobilityCandidacy candidacy = submission.getSelectedCandidacy();
            if (candidacy != null) {
                final Registration registration = submission.getRegistration();
                final Person person = registration.getPerson();
                final ICurriculum curriculum = registration.getCurriculum();
                final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
                final OutboundMobilityCandidacyContestGroup group = contest.getOutboundMobilityCandidacyContestGroup();
                final MobilityAgreement mobilityAgreement = contest.getMobilityAgreement();
                final MobilityProgram mobilityProgram = mobilityAgreement.getMobilityProgram();
                final RegistrationProtocol registrationProtocol = mobilityProgram.getRegistrationProtocol();
                final UniversityUnit universityUnit = mobilityAgreement.getUniversityUnit();
                final Country country = universityUnit.getCountry();

                final Row candidacyRow = spreadsheetCandidates.addRow();
                candidacyRow.setCell(getString("label.mobility.program"), registrationProtocol.getDescription().getContent());
                candidacyRow.setCell(getString("label.country"), country == null ? "" : country.getLocalizedName().toString());
                candidacyRow.setCell(getString("label.university"), universityUnit.getPresentationName());
                candidacyRow.setCell(getString("label.degrees"), group.getDescription());
                candidacyRow.setCell(getString("label.vacancies"), contest.getVacancies());
                candidacyRow.setCell(getString("label.username"), person.getUsername());
                candidacyRow.setCell(getString("label.name"), person.getName());
                candidacyRow.setCell(getString("label.degree"), registration.getDegree().getSigla());
                candidacyRow.setCell(getString("label.candidate.classification"), getGrades(submission));
                candidacyRow.setCell(getString("label.preference.order"), candidacy.getPreferenceOrder());
                candidacyRow.setCell(getString("label.curricular.year"), curriculum.getCurricularYear());
                candidacyRow.setCell(getString("label.ects.completed.degree"), curriculum.getSumEctsCredits().toString());
                candidacyRow.setCell(getString("label.average.degree"), curriculum.getRawGrade().getValue());
                group.fillCycleDetails(candidacyRow, CycleType.FIRST_CYCLE, registration,
                        getString("label.ects.completed.cycle.first"), getString("label.average.cycle.first"));
                group.fillCycleDetails(candidacyRow, CycleType.SECOND_CYCLE, registration,
                        getString("label.ects.completed.cycle.second"), getString("label.average.cycle.second"));
                candidacyRow.setCell(getString("label.email"), person.getEmailForSendingEmails());
                candidacyRow.setCell(getString("label.phone"), person.getDefaultPhoneNumber());
                candidacyRow.setCell(getString("label.mobile"), person.getDefaultMobilePhoneNumber());

                for (final Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
                    if (otherRegistration != registration) {
                        final Row rowOCI = spreadsheetOtherCurricularInfo.addRow();
                        final ICurriculum curriculumOther = otherRegistration.getCurriculum();
                        rowOCI.setCell(getString("label.username"), person.getUsername());
                        rowOCI.setCell(getString("label.name"), person.getName());
                        rowOCI.setCell(getString("label.degree"), otherRegistration.getDegree().getSigla());
                        rowOCI.setCell(getString("label.curricular.year"), curriculumOther.getCurricularYear());
                        rowOCI.setCell(getString("label.ects.completed.degree"), curriculumOther.getSumEctsCredits().toString());
                        rowOCI.setCell(getString("label.average.degree"), curriculumOther.getRawGrade().getValue());
                        group.fillCycleDetails(rowOCI, CycleType.FIRST_CYCLE, otherRegistration,
                                getString("label.ects.completed.cycle.first"), getString("label.average.cycle.first"));
                        group.fillCycleDetails(rowOCI, CycleType.SECOND_CYCLE, otherRegistration,
                                getString("label.ects.completed.cycle.second"), getString("label.average.cycle.second"));
                    }
                }
            }
        }

        return spreadsheetCandidates;
    }

    private String getGrades(final OutboundMobilityCandidacySubmission submission) {
        final Set<BigDecimal> grades = new HashSet<BigDecimal>();
        final StringBuilder builder = new StringBuilder();
        for (final OutboundMobilityCandidacySubmissionGrade grade : submission.getOutboundMobilityCandidacySubmissionGradeSet()) {
            final BigDecimal value = grade.getGrade();
            if (!grades.contains(value)) {
                grades.add(value);
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(value.toString());
            }
        }
        return builder.toString();
    }

    private String getString(final String key, final String... args) {
        return BundleUtil.getString(Bundle.ACADEMIC, key, args);
    }

    @Atomic
    public String selectCandidatesForAllGroups() {
        boolean hasSomePlacement = false;

        final StringBuilder error = new StringBuilder();

        final Map<OutboundMobilityCandidacySubmission, OutboundMobilityCandidacy> selections =
                new HashMap<OutboundMobilityCandidacySubmission, OutboundMobilityCandidacy>();
        for (final OutboundMobilityCandidacySubmission submission : getOutboundMobilityCandidacySubmissionSet()) {
            final OutboundMobilityCandidacy selectedCandidacy = submission.getSelectedCandidacy();
            selections.put(submission, selectedCandidacy);

            if (selectedCandidacy != null) {
                hasSomePlacement = true;
            }

            BigDecimal gv = null;
            for (final OutboundMobilityCandidacySubmissionGrade grade : submission
                    .getOutboundMobilityCandidacySubmissionGradeSet()) {
                if (gv == null) {
                    gv = grade.getGrade();
                } else {
                    if (!gv.equals(grade.getGrade())) {
                        final Registration registration = submission.getRegistration();
                        error.append(getString("label.error.student.has.different.grades.in.different.groups", registration
                                .getPerson().getUsername(), registration.getDegree().getSigla()));
                        error.append("\n");
                    }
                }
            }
        }
        if (error.length() > 0) {
            throw new DomainException(error.toString());
        }

        for (final OutboundMobilityCandidacyContestGroup group : getOutboundMobilityCandidacyContestGroupSet()) {
            for (final OutboundMobilityCandidacyContest contest : group.getOutboundMobilityCandidacyContestSet()) {
                for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                    candidacy.unselect();
                }
            }
        }

        for (final OutboundMobilityCandidacyContestGroup group : getOutboundMobilityCandidacyContestGroupSet()) {
            group.selectCandidates(this);
        }

        final StringBuilder result = new StringBuilder();
        final SortedSet<OutboundMobilityCandidacySubmission> submissions =
                new TreeSet<OutboundMobilityCandidacySubmission>(new Comparator<OutboundMobilityCandidacySubmission>() {
                    @Override
                    public int compare(OutboundMobilityCandidacySubmission o1, OutboundMobilityCandidacySubmission o2) {
                        final int cd = o1.getRegistration().getDegree().compareTo(o2.getRegistration().getDegree());
                        return cd == 0 ? o1.compareTo(o2) : cd;
                    }
                });
        submissions.addAll(getOutboundMobilityCandidacySubmissionSet());
        int selectedCandidateCount = 0;
        for (final OutboundMobilityCandidacySubmission submission : submissions) {
            final OutboundMobilityCandidacy selectedCandidacy = submission.getSelectedCandidacy();
            if (selectedCandidacy != null) {
                selectedCandidateCount++;
            }
            if (selectedCandidacy != selections.get(submission)) {
                final Registration registration = submission.getRegistration();
                result.append(getString("label.changed.candidate.selection", registration.getPerson().getUsername(), registration
                        .getDegree().getSigla(), printPlacement(selections.get(submission)), printPlacement(selectedCandidacy)));
                result.append("\n");
            }
        }
        return hasSomePlacement ? result.toString() : getString("label.count.placed.candidates", "" + selectedCandidateCount);
    }

    private String printPlacement(final OutboundMobilityCandidacy candidacy) {
        return candidacy == null ? "-----" : candidacy.getOutboundMobilityCandidacyContest().getMobilityAgreement()
                .getUniversityUnit().getPresentationName();
    }

    private OutboundMobilityCandidacyPeriod findPreviousPeriod() {
        OutboundMobilityCandidacyPeriod result = null;
        for (final ExecutionInterval interval : Bennu.getInstance().getExecutionIntervalsSet()) {
            for (final CandidacyPeriod candidacyPeriod : interval.getCandidacyPeriodsSet()) {
                if (candidacyPeriod instanceof OutboundMobilityCandidacyPeriod) {
                    final OutboundMobilityCandidacyPeriod period = (OutboundMobilityCandidacyPeriod) candidacyPeriod;
                    final YearMonthDay ymd = period.getExecutionInterval().getBeginDateYearMonthDay();
                    if (ymd.isBefore(getExecutionInterval().getBeginDateYearMonthDay())
                            && (result == null || result.getExecutionInterval().getBeginDateYearMonthDay().isBefore(ymd))) {
                        result = period;
                    }
                }
            }
        }
        return result;
    }

    @Atomic
    public void delete() {
        if (!getOutboundMobilityCandidacyContestSet().isEmpty()) {
            throw new DomainException("cannot.delete.because.still.connected.to.contests");
        }
        for (final OutboundMobilityCandidacyPeriodConfirmationOption option : getOutboundMobilityCandidacyPeriodConfirmationOptionSet()) {
            option.delete();
        }
        setExecutionInterval(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

}
