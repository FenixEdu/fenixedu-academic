package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class OutboundMobilityCandidacyPeriod extends OutboundMobilityCandidacyPeriod_Base implements Comparable<CandidacyPeriod> {

    public OutboundMobilityCandidacyPeriod(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        super();
        init(executionInterval, start, end);
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
        return new TreeSet<OutboundMobilityCandidacyContest>(getOutboundMobilityCandidacyContest());
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
        for (final Registration registration : person.getStudent().getRegistrations()) {
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
                        final BigDecimal grade1 = o1.getGrade(mobilityGroup);
                        final BigDecimal grade2 = o2.getGrade(mobilityGroup);
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
                BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
                        "label.mobility.outbound.period.export.selected.candiadates.filename");

        final Spreadsheet spreadsheetCandidates = new Spreadsheet(filename);
        final Spreadsheet spreadsheetOtherCurricularInfo =
                spreadsheetCandidates.addSpreadsheet(BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
                        "label.other.curricular.info"));
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
                final RegistrationAgreement registrationAgreement = mobilityProgram.getRegistrationAgreement();
                final UniversityUnit universityUnit = mobilityAgreement.getUniversityUnit();
                final Country country = universityUnit.getCountry();

                final Row candidacyRow = spreadsheetCandidates.addRow();
                candidacyRow.setCell(getString("label.mobility.program"), registrationAgreement.getDescription());
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
                candidacyRow.setCell(getString("label.average.degree"), curriculum.getAverage().toString());
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
                        rowOCI.setCell(getString("label.average.degree"), curriculumOther.getAverage().toString());
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
        return BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", key, args);
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest> getOutboundMobilityCandidacyContest() {
        return getOutboundMobilityCandidacyContestSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyContest() {
        return !getOutboundMobilityCandidacyContestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission> getOutboundMobilityCandidacySubmission() {
        return getOutboundMobilityCandidacySubmissionSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacySubmission() {
        return !getOutboundMobilityCandidacySubmissionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup> getCandidatesNotifiedOfSelectionResultsForGroups() {
        return getCandidatesNotifiedOfSelectionResultsForGroupsSet();
    }

    @Deprecated
    public boolean hasAnyCandidatesNotifiedOfSelectionResultsForGroups() {
        return !getCandidatesNotifiedOfSelectionResultsForGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption> getOutboundMobilityCandidacyPeriodConfirmationOption() {
        return getOutboundMobilityCandidacyPeriodConfirmationOptionSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyPeriodConfirmationOption() {
        return !getOutboundMobilityCandidacyPeriodConfirmationOptionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup> getConcludedCandidateSelectionGroups() {
        return getConcludedCandidateSelectionGroupsSet();
    }

    @Deprecated
    public boolean hasAnyConcludedCandidateSelectionGroups() {
        return !getConcludedCandidateSelectionGroupsSet().isEmpty();
    }

    @Deprecated
    public boolean hasOptionIntroductoryDestription() {
        return getOptionIntroductoryDestription() != null;
    }

}
