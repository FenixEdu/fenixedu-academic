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
package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission.Calculator;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class OutboundMobilityCandidacyContestGroup extends OutboundMobilityCandidacyContestGroup_Base implements
        Comparable<OutboundMobilityCandidacyContestGroup> {

    private static final Logger logger = LoggerFactory.getLogger(OutboundMobilityCandidacyContestGroup.class);

    public OutboundMobilityCandidacyContestGroup(final OutboundMobilityCandidacyContest contest,
            final Set<ExecutionDegree> executionDegrees) {
        if (contest == null) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.contest");
        }
        if (executionDegrees.isEmpty()) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.execution.degree");
        }
        setRootDomainObject(Bennu.getInstance());
        addOutboundMobilityCandidacyContest(contest);
        getExecutionDegreeSet().addAll(executionDegrees);

        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        getOutboundMobilityCandidacyContestSet().size();
        getExecutionDegreeSet().size();
    }

    public OutboundMobilityCandidacyContestGroup(final Set<ExecutionDegree> executionDegrees) {
        if (executionDegrees.isEmpty()) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.execution.degree");
        }
        setRootDomainObject(Bennu.getInstance());
        getExecutionDegreeSet().addAll(executionDegrees);

        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        getOutboundMobilityCandidacyContestSet().size();
        getExecutionDegreeSet().size();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyContestGroup o) {
        final int dc = getDescription().compareTo(o.getDescription());
        return dc == 0 ? getExternalId().compareTo(o.getExternalId()) : dc;
    }

    public String getDescription() {
        final StringBuilder builder = new StringBuilder();
        for (final ExecutionDegree executionDegree : getSortedExecutionDegrees()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(executionDegree.getDegree().getSigla());
        }
        return builder.toString();
    }

    public SortedSet<ExecutionDegree> getSortedExecutionDegrees() {
        final SortedSet<ExecutionDegree> result = new TreeSet<ExecutionDegree>(ExecutionDegree.COMPARATOR_BY_DEGREE_CODE);
        result.addAll(getExecutionDegreeSet());
        return result;
    }

    @Atomic
    public void addExecutionDegreeService(final ExecutionDegree executionDegree) {
        addExecutionDegree(executionDegree);
    }

    @Atomic
    public void removeExecutionDegreeService(final ExecutionDegree executionDegree) {
        removeExecutionDegree(executionDegree);
    }

    @Atomic
    public void addMobilityCoordinatorService(final Person person) {
        addMobilityCoordinator(person);
    }

    @Atomic
    public void removeMobilityCoordinatorService(final Person person) {
        removeMobilityCoordinator(person);
    }

    public static OutboundMobilityCandidacyContestGroup findOrCreateGroup(final ExecutionDegree executionDegree) {
        for (final OutboundMobilityCandidacyContestGroup mobilityGroup : executionDegree
                .getOutboundMobilityCandidacyContestGroupSet()) {
            if (mobilityGroup.getExecutionDegreeSet().size() == 1) {
                return mobilityGroup;
            }
        }
        return new OutboundMobilityCandidacyContestGroup(Collections.singleton(executionDegree));
    }

    public void delete() {
        getExecutionDegreeSet().clear();
        getMobilityCoordinatorSet().clear();
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            contest.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();

    }

    public boolean handles(final Degree degree) {
        for (final ExecutionDegree executionDegree : getExecutionDegreeSet()) {
            if (executionDegree.getDegree() == degree) {
                return true;
            }
        }
        return false;
    }

    public Spreadsheet getCandidatesInformationSpreadSheet(final OutboundMobilityCandidacyPeriod period) {
        final String filename =
                BundleUtil.getString(Bundle.ACADEMIC,
                        "label.mobility.candidates.information.filename");

        final Spreadsheet spreadsheetCurricularInfo = new Spreadsheet(filename);
        final Spreadsheet spreadsheetOptions =
                spreadsheetCurricularInfo.addSpreadsheet(BundleUtil.getString(Bundle.ACADEMIC,
                        "label.mobility.candidates.options"));
        final Spreadsheet spreadsheetContactInformation =
                spreadsheetOptions.addSpreadsheet(BundleUtil.getString(Bundle.ACADEMIC,
                        "label.contact.information"));
        final Spreadsheet spreadsheetOtherCurricularInfo =
                spreadsheetOptions.addSpreadsheet(BundleUtil.getString(Bundle.ACADEMIC,
                        "label.other.curricular.info"));

        final Set<Registration> processed = new HashSet<Registration>();
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                final OutboundMobilityCandidacySubmission submission = candidacy.getOutboundMobilityCandidacySubmission();
                final Registration registration = submission.getRegistration();
                final Calculator calculator = new Calculator(registration.getStudent());

                if (!processed.contains(registration)) {
                    final Person person = registration.getPerson();

                    final Row row = spreadsheetCurricularInfo.addRow();
                    final BigDecimal candidacyGrade = submission.getGrade(this);
//                    final ICurriculum curriculum = registration.getCurriculum();
                    row.setCell(getString("label.username"), person.getUsername());
                    row.setCell(getString("label.name"), person.getName());
                    row.setCell(getString("label.degree"), registration.getDegree().getSigla());
                    row.setCell(getString("label.candidate.classification"),
                            candidacyGrade == null ? "" : candidacyGrade.toString());

                    row.setCell(getString("label.ects.first.cycle"), calculator.completedECTSCycle1.toString());
                    row.setCell(getString("label.ects.average"), calculator.getEctsAverage().toString());
                    row.setCell(getString("label.ects.average.first.and.second.cycle"), calculator.getEctsEverateFirstAndSecondCycle().toString());
                    row.setCell(getString("label.ects.completed"), calculator.completedECTS.toString());
                    //row.setCell(getString("label.ects.pending"), calculator.getPendingEcts().toString());
                    row.setCell(getString("label.ects.enrolled"), calculator.enrolledECTS.toString());

                    for (final Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
                        final Row rowOCI = spreadsheetOtherCurricularInfo.addRow();
                        final ICurriculum curriculumOther = otherRegistration.getCurriculum();
                        rowOCI.setCell(getString("label.username"), person.getUsername());
                        rowOCI.setCell(getString("label.name"), person.getName());
                        rowOCI.setCell(getString("label.degree"), otherRegistration.getDegree().getSigla());
                        rowOCI.setCell(getString("label.curricular.year"), curriculumOther.getCurricularYear());
                        rowOCI.setCell(getString("label.ects.completed.degree"), curriculumOther.getSumEctsCredits().toString());
                        rowOCI.setCell(getString("label.average.degree"), curriculumOther.getAverage().toString());
                        fillCycleDetails(rowOCI, CycleType.FIRST_CYCLE, otherRegistration,
                                getString("label.ects.completed.cycle.first"), getString("label.average.cycle.first"));
                        fillCycleDetails(rowOCI, CycleType.SECOND_CYCLE, otherRegistration,
                                getString("label.ects.completed.cycle.second"), getString("label.average.cycle.second"));
                    }

                    for (final OutboundMobilityCandidacy c : submission.getSortedOutboundMobilityCandidacySet()) {
                        final OutboundMobilityCandidacyContest contestFromCandidacy = c.getOutboundMobilityCandidacyContest();
                        final MobilityAgreement mobilityAgreement = contestFromCandidacy.getMobilityAgreement();
                        final UniversityUnit unit = mobilityAgreement.getUniversityUnit();
                        final Country country = unit.getCountry();

                        final Row row2 = spreadsheetOptions.addRow();
                        row2.setCell(getString("label.username"), person.getUsername());
                        row2.setCell(getString("label.preference.order"), c.getPreferenceOrder());
                        row2.setCell(getString("label.degrees"), contestFromCandidacy.getOutboundMobilityCandidacyContestGroup()
                                .getDescription());
                        row2.setCell(getString("label.mobility.program"), mobilityAgreement.getMobilityProgram()
                                .getRegistrationAgreement().getDescription());
                        row2.setCell(getString("label.country"), country == null ? "" : country.getName());
                        row2.setCell(getString("label.university"), unit.getPresentationName());
                    }

                    final Row contactRow = spreadsheetContactInformation.addRow();
                    contactRow.setCell(getString("label.username"), person.getUsername());
                    contactRow.setCell(getString("label.name"), person.getName());
                    contactRow.setCell(getString("label.email"), person.getEmailForSendingEmails());
                    contactRow.setCell(getString("label.phone"), person.getDefaultPhoneNumber());
                    contactRow.setCell(getString("label.mobile"), person.getDefaultMobilePhoneNumber());

                    processed.add(registration);
                }
            }
        }

        return spreadsheetCurricularInfo;
    }

    public void fillCandidateOptions(Spreadsheet spreadsheet, OutboundMobilityCandidacyPeriod period) {
    }

    void fillCycleDetails(final Row row, final CycleType cycleType, final Registration registration, final String header1,
            final String header2) {
        if (isForCycle(cycleType, registration)) {
            ICurriculum curriculum = registration.getCurriculum(cycleType);
            row.setCell(header1, curriculum.getSumEctsCredits().toString());
            row.setCell(header2, curriculum.getAverage().toString());
        } else {
            row.setCell(header1, "");
            row.setCell(header2, "");
        }
    }

    private boolean isForCycle(final CycleType cycleType, final Registration registration) {
        for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getCycle(cycleType) != null) {
                return true;
            }
        }
        return false;
    }

    private String getString(final String key) {
        return BundleUtil.getString(Bundle.ACADEMIC, key);
    }

    @Atomic
    public void setGrades(final OutboundMobilityCandidacyPeriod candidacyPeriod, final String contents) {
        final StringBuilder problems = new StringBuilder();

        int l = 0;
        for (final String line : contents.split("\r?\n")) {
            l++;
            final String[] parts = line.split("\t");
            final Person person = Person.findByUsername(parts[0]);
            if (person == null) {
                // truncate to avoid long lines while printing code for username from an invalid file (xls)
                problems.append(getMessage("error.mobility.outbound.username.not.valid.on.line", truncateDots(parts[0], 12),
                        Integer.toString(l)));
                problems.append("; ");
                continue;
            }
            try {
                final BigDecimal grade = new BigDecimal(parts[1]);
                final OutboundMobilityCandidacySubmission submission = candidacyPeriod.findSubmissionFor(person);
                submission.setGrade(this, grade);
            } catch (final NumberFormatException ex) {
                // truncate to avoid long lines while printing code for grade from an invalid file (xls)
                problems.append(getMessage("error.mobility.outbound.invalid.grade.on.one", truncateDots(parts[1], 4),
                        Integer.toString(l)));
                problems.append("; ");
            } catch (final ArrayIndexOutOfBoundsException ex) {
                problems.append(getMessage("error.mobility.outbound.invalid.format.on.line", Integer.toString(l)));
                problems.append(".");
                break;
            }
        }

        if (problems.length() > 0) {
            throw new DomainException("error.mobility.outbound.unable.to.set.grades", problems.toString());
        }
    }

    private String getMessage(final String key, final String... args) {
        return BundleUtil.getString(Bundle.ACADEMIC, key, args);
    }

    // truncate string and add (...) if it is longer than length
    private String truncateDots(String stringToTruncate, int length) {
        return (stringToTruncate.length() > length) ? (stringToTruncate.substring(0, length) + "(...)") : stringToTruncate;
    }

    public boolean areAllStudentsGraded(final OutboundMobilityCandidacyPeriod period) {
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                final OutboundMobilityCandidacySubmission submission = candidacy.getOutboundMobilityCandidacySubmission();
                if (submission.getOutboundMobilityCandidacyPeriod() == period && !hasGrade(submission)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasGrade(final OutboundMobilityCandidacySubmission submission) {
        for (final OutboundMobilityCandidacySubmissionGrade grade : submission.getOutboundMobilityCandidacySubmissionGradeSet()) {
            if (grade.getOutboundMobilityCandidacyContestGroup() == this) {
                return true;
            }
        }
        return false;
    }

    public boolean isCandidacySelectionConcluded(final OutboundMobilityCandidacyPeriod period) {
        return getConcludedCandidateSelectionForPeriodSet().contains(period);
    }

    public boolean isCandidateNotificationConcluded(final OutboundMobilityCandidacyPeriod period) {
        return getCandidatesNotifiedOfSelectionResultsForPeriodSet().contains(period);
    }

    public boolean areCandidatesNotofiedOfSelectionResults(final OutboundMobilityCandidacyPeriod period) {
        return getCandidatesNotifiedOfSelectionResultsForPeriodSet().contains(period);
    }

    public boolean haveAllCandidatesConfirmed(final OutboundMobilityCandidacyPeriod period) {
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                final OutboundMobilityCandidacySubmission submission = candidacy.getSubmissionFromSelectedCandidacy();
                if (submission != null) {
                    if (!submission.hasConfirmedPlacement()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Atomic
    public void selectCandidates(final OutboundMobilityCandidacyPeriod period) {
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            if (contest.getOutboundMobilityCandidacyPeriod() == period) {
                for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                    final OutboundMobilityCandidacySubmission submission = candidacy.getSubmissionFromSelectedCandidacy();
                    if (submission != null) {
                        candidacy.unselect();
                    }
                }
            }
        }

        final SortedSet<OutboundMobilityCandidacySubmissionGrade> grades =
                new TreeSet<OutboundMobilityCandidacySubmissionGrade>();
        collectGradesForGroup(grades, period);

        for (final OutboundMobilityCandidacyContestGroup otherGroup : getRootDomainObject()
                .getOutboundMobilityCandidacyContestGroupSet()) {
            if (otherGroup != this && intersect(otherGroup)) {
                otherGroup.collectGradesForGroup(grades, period);
            }
        }

        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : grades) {
            logger.info("Selecting for: "
                    + submissionGrade.getOutboundMobilityCandidacySubmission().getRegistration().getPerson().getUsername());
            final BigDecimal grade = submissionGrade.getGrade();
            if (grade.signum() == 1) {
                submissionGrade.getOutboundMobilityCandidacySubmission().select();
            }
        }
    }

    private void unselectCandidates(final OutboundMobilityCandidacyPeriod period) {
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            if (contest.getOutboundMobilityCandidacyPeriod() == period) {
                for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                    final OutboundMobilityCandidacySubmission submission = candidacy.getSubmissionFromSelectedCandidacy();
                    if (submission != null) {
                        candidacy.unselect();
                    }
                }
            }
        }
    }

    private boolean intersect(final OutboundMobilityCandidacyContestGroup otherGroup) {
        return CollectionUtils.containsAny(getExecutionDegreeSet(), otherGroup.getExecutionDegreeSet());
    }

    private void collectGradesForGroup(final SortedSet<OutboundMobilityCandidacySubmissionGrade> grades,
            final OutboundMobilityCandidacyPeriod period) {
        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            final OutboundMobilityCandidacySubmission submission = submissionGrade.getOutboundMobilityCandidacySubmission();
            if (submission.getOutboundMobilityCandidacyPeriod() == period) {
                grades.add(submissionGrade);
            }
        }
    }

    @Atomic
    public void concludeCandidateSelection(final OutboundMobilityCandidacyPeriod period) {
        addConcludedCandidateSelectionForPeriod(period);
    }

    @Atomic
    public void revertConcludeCandidateSelection(final OutboundMobilityCandidacyPeriod period) {
        removeConcludedCandidateSelectionForPeriod(period);
    }

    @Atomic
    public void concludeCandidateNotification(final OutboundMobilityCandidacyPeriod period) {
        addCandidatesNotifiedOfSelectionResultsForPeriod(period);
    }

    @Atomic
    public void revertConcludeCandidateNotification(final OutboundMobilityCandidacyPeriod period) {
        removeCandidatesNotifiedOfSelectionResultsForPeriod(period);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionDegree> getExecutionDegree() {
        return getExecutionDegreeSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegree() {
        return !getExecutionDegreeSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getMobilityCoordinator() {
        return getMobilityCoordinatorSet();
    }

    @Deprecated
    public boolean hasAnyMobilityCoordinator() {
        return !getMobilityCoordinatorSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmissionGrade> getOutboundMobilityCandidacySubmissionGrade() {
        return getOutboundMobilityCandidacySubmissionGradeSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacySubmissionGrade() {
        return !getOutboundMobilityCandidacySubmissionGradeSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod> getCandidatesNotifiedOfSelectionResultsForPeriod() {
        return getCandidatesNotifiedOfSelectionResultsForPeriodSet();
    }

    @Deprecated
    public boolean hasAnyCandidatesNotifiedOfSelectionResultsForPeriod() {
        return !getCandidatesNotifiedOfSelectionResultsForPeriodSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod> getConcludedCandidateSelectionForPeriod() {
        return getConcludedCandidateSelectionForPeriodSet();
    }

    @Deprecated
    public boolean hasAnyConcludedCandidateSelectionForPeriod() {
        return !getConcludedCandidateSelectionForPeriodSet().isEmpty();
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
