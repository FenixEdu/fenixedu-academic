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
import java.math.RoundingMode;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacySubmission extends OutboundMobilityCandidacySubmission_Base implements
        Comparable<OutboundMobilityCandidacySubmission> {

    public OutboundMobilityCandidacySubmission(final OutboundMobilityCandidacyPeriod candidacyPeriod,
            final Registration registration) {
        setRootDomainObject(Bennu.getInstance());
        setOutboundMobilityCandidacyPeriod(candidacyPeriod);
        setRegistration(registration);
    }

    public String getStatusString() {
        return "TODO";
    }

    public static void apply(final OutboundMobilityCandidacyContest contest, final Registration registration) {
        final OutboundMobilityCandidacySubmission submission = getOutboundMobilityCandidacySubmission(contest, registration);
        submission.apply(contest);
    }

    @Atomic
    public void apply(final OutboundMobilityCandidacyContest contest) {
        for (final OutboundMobilityCandidacy candidacy : getOutboundMobilityCandidacySet()) {
            if (candidacy.getOutboundMobilityCandidacyContest() == contest) {
                return;
            }
        }
        new OutboundMobilityCandidacy(contest, this);
        final OutboundMobilityCandidacyContestGroup mobilityGroup = contest.getOutboundMobilityCandidacyContestGroup();
        setGrade(mobilityGroup, null);
    }

    @Atomic
    private static OutboundMobilityCandidacySubmission getOutboundMobilityCandidacySubmission(
            final OutboundMobilityCandidacyContest contest, final Registration registration) {
        final OutboundMobilityCandidacyPeriod candidacyPeriod = contest.getOutboundMobilityCandidacyPeriod();
        for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
            if (submission.getOutboundMobilityCandidacyPeriod() == candidacyPeriod) {
                return submission;
            }
        }
        return new OutboundMobilityCandidacySubmission(candidacyPeriod, registration);
    }

    public void delete() {
        for (final OutboundMobilityCandidacySubmissionGrade grade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            grade.delete();
        }
        setOutboundMobilityCandidacyPeriod(null);
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public SortedSet<OutboundMobilityCandidacy> getSortedOutboundMobilityCandidacySet() {
        return new TreeSet<OutboundMobilityCandidacy>(getOutboundMobilityCandidacySet());
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacySubmission o) {
        final int r = Registration.COMPARATOR_BY_NUMBER_AND_ID.compare(getRegistration(), o.getRegistration());
        return r == 0 ? getExternalId().compareTo(o.getExternalId()) : r;
    }

    public SortedSet<OutboundMobilityCandidacyContestGroup> getOutboundMobilityCandidacyContestGroupSet() {
        final SortedSet<OutboundMobilityCandidacyContestGroup> result = new TreeSet<OutboundMobilityCandidacyContestGroup>();
        for (final OutboundMobilityCandidacy candidacy : getOutboundMobilityCandidacySet()) {
            final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
            result.add(contest.getOutboundMobilityCandidacyContestGroup());
        }
        return result;
    }

    public BigDecimal getGrade(final OutboundMobilityCandidacyContestGroup mobilityGroup) {
        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            if (submissionGrade.getOutboundMobilityCandidacyContestGroup() == mobilityGroup) {
                return submissionGrade.getGrade();
            }
        }
        return null;
    }

    public BigDecimal getGradeForSerialization(final OutboundMobilityCandidacyContestGroup mobilityGroup) {
        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            if (submissionGrade.getOutboundMobilityCandidacyContestGroup() == mobilityGroup) {
                return submissionGrade.getGradeForSerialization();
            }
        }
        return null;
    }

    @Atomic
    public void setGrade(final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade) {
        final Calculator calculator = new Calculator(getRegistration().getStudent(), grade);
        setGrade(mobilityGroup, calculator.grade, calculator.gradeForSerialization);
    }

    private void setGrade(final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade,
            final BigDecimal gradeForSerialization) {
        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            if (submissionGrade.getOutboundMobilityCandidacyContestGroup() == mobilityGroup) {
                submissionGrade.edit(grade, gradeForSerialization);
                return;
            }
        }
        new OutboundMobilityCandidacySubmissionGrade(this, mobilityGroup, grade, gradeForSerialization);
    }

    public boolean hasConfirmedPlacement() {
        final Boolean cp = getConfirmedPlacement();
        return cp != null && cp.booleanValue();
    }

    public void select() {
        if (!hasSelectedCandidacy()) {
            for (final OutboundMobilityCandidacy candidacy : getSortedOutboundMobilityCandidacySet()) {
                if (candidacy.getOutboundMobilityCandidacyContest().hasVacancy()) {
                    candidacy.select();
                    return;
                }
            }
        }
    }

    public boolean hasContestInGroup(final OutboundMobilityCandidacyContestGroup mobilityGroup) {
        for (final OutboundMobilityCandidacy candidacy : getOutboundMobilityCandidacySet()) {
            final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
            if (contest.getOutboundMobilityCandidacyContestGroup() == mobilityGroup) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public void selectOption(final OutboundMobilityCandidacyPeriodConfirmationOption option) {
        setConfirmationOption(option);
        setConfirmedPlacement(Boolean.TRUE);
    }

    @Atomic
    public void removeOption(final OutboundMobilityCandidacyPeriodConfirmationOption option) {
        if (getConfirmationOption() == option) {
            setConfirmationOption(null);
            setConfirmedPlacement(Boolean.FALSE);
        }
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
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy> getOutboundMobilityCandidacy() {
        return getOutboundMobilityCandidacySet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacy() {
        return !getOutboundMobilityCandidacySet().isEmpty();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasConfirmationOption() {
        return getConfirmationOption() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSelectedCandidacy() {
        return getSelectedCandidacy() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacyPeriod() {
        return getOutboundMobilityCandidacyPeriod() != null;
    }

    private final static BigDecimal AVG_FACTOR = new BigDecimal(1000000000000l);
    private final static BigDecimal ECTS_FACTOR = new BigDecimal(100000000l);
    private final static BigDecimal PENDING_ECTS_FACTOR = new BigDecimal(10000l);

    public static class Calculator {

        public BigDecimal possibleECTS = BigDecimal.ZERO;
        public BigDecimal enrolledECTS = BigDecimal.ZERO;

        public BigDecimal completedECTS = BigDecimal.ZERO;
        public BigDecimal completedECTSCycle1 = BigDecimal.ZERO;
        public BigDecimal completedECTSCycle2 = BigDecimal.ZERO;

        public BigDecimal factoredGradeSum = BigDecimal.ZERO;
        public BigDecimal factoredGradeSumCycle1 = BigDecimal.ZERO;
        public BigDecimal factoredGradeSumCycle2 = BigDecimal.ZERO;

        public BigDecimal factoredECTS = BigDecimal.ZERO;
        public BigDecimal factoredECTSCycle1 = BigDecimal.ZERO;
        public BigDecimal factoredECTSCycle2 = BigDecimal.ZERO;

        public BigDecimal grade;
        public BigDecimal gradeForSerialization;

        public Calculator(final Student student) {
            this(student, null);
        }

        private Calculator(final Student student, final BigDecimal grade) {
            for (final Registration registration : student.getRegistrationsSet()) {
                if (registration.getDegree() != null && registration.getDegree().getEctsCredits() != null) {
                    possibleECTS = possibleECTS.add(new BigDecimal(registration.getDegree().getEctsCredits()));
                }
                for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    calculateGrade(null, studentCurricularPlan.getRoot());
                }
            }

            if (completedECTSCycle1.doubleValue() >= 120) {
                final BigDecimal gradeForElimination =
                        factoredECTS.doubleValue() > 0 ? factoredGradeSum.divide(factoredECTS, 2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
                if (gradeForElimination.doubleValue() >= 12.50) {
                    final BigDecimal d = factoredECTSCycle1.add(factoredECTSCycle2);
                    if (d.doubleValue() > 0) {
                        this.grade =
                                grade != null ? grade : factoredGradeSumCycle1.add(factoredGradeSumCycle2)
                                        .divide(d, 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(5));
                        gradeForSerialization =
                                this.grade
                                        .multiply(AVG_FACTOR)
                                        .add(completedECTS.multiply(ECTS_FACTOR))
                                        .add(new BigDecimal(9999).subtract(possibleECTS).add(completedECTS)
                                                .multiply(PENDING_ECTS_FACTOR))
                                        .add(completedECTS.divide(enrolledECTS, 4, RoundingMode.HALF_EVEN).multiply(
                                                new BigDecimal(10000)));
                        return;
                    }
                }
            }
            if (grade != null) {
                this.grade = grade;
                gradeForSerialization =
                        this.grade
                                .multiply(AVG_FACTOR)
                                .add(completedECTS.multiply(ECTS_FACTOR))
                                .add(new BigDecimal(9999).subtract(possibleECTS).add(completedECTS).multiply(PENDING_ECTS_FACTOR));
            } else {
                this.grade = BigDecimal.ZERO;
                gradeForSerialization = BigDecimal.ZERO;
            }
        }

        private void calculateGrade(final CycleType cycleType, CurriculumModule module) {
            if (module.isLeaf()) {
                if (module.isEnrolment()) {
                    final Enrolment enrolment = (Enrolment) module;
                    final BigDecimal weigth = enrolment.getWeigthForCurriculum();
                    enrolledECTS = enrolledECTS.add(weigth);
                    if (enrolment.isApproved()) {
                        completedECTS = completedECTS.add(weigth);
                        if (cycleType == CycleType.FIRST_CYCLE) {
                            completedECTSCycle1 = completedECTSCycle1.add(weigth);
                        } else if (cycleType == CycleType.SECOND_CYCLE) {
                            completedECTSCycle2 = completedECTSCycle2.add(weigth);
                        }

                        final Grade grade = enrolment.getGrade();
                        if (grade.isNumeric()) {
                            final BigDecimal value = grade.getNumericValue();
                            factoredECTS = factoredECTS.add(weigth);
                            factoredGradeSum = factoredGradeSum.add(value.multiply(weigth));
                            if (cycleType == CycleType.FIRST_CYCLE) {
                                factoredECTSCycle1 = factoredECTSCycle1.add(weigth);
                                factoredGradeSumCycle1 = factoredGradeSumCycle1.add(value.multiply(weigth));
                            } else if (cycleType == CycleType.SECOND_CYCLE) {
                                factoredECTSCycle2 = factoredECTSCycle2.add(weigth);
                                factoredGradeSumCycle2 = factoredGradeSumCycle2.add(value.multiply(weigth));
                            }
                        }
                    }
                }
            } else {
                final CurriculumGroup group = (CurriculumGroup) module;
                final CycleType cycleForChild =
                        group.isCycleCurriculumGroup() ? ((CycleCurriculumGroup) group).getCycleType() : cycleType;
                for (final CurriculumModule child : group.getCurriculumModulesSet()) {
                    calculateGrade(cycleForChild, child);
                }
            }
        }

        public BigDecimal getCompletedEctsFirstAndSecondCycle() {
            return completedECTSCycle1.add(completedECTSCycle2);
        }

        public BigDecimal getEctsAverage() {
            final BigDecimal d = factoredECTS;
            return d.doubleValue() > 0 ? factoredGradeSum.divide(d, 2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
        }

        public BigDecimal getPendingEcts() {
            BigDecimal result = possibleECTS.subtract(completedECTS);
            return result.signum() == 1 ? result : BigDecimal.ZERO;
        }

        public BigDecimal getEctsEverateFirstAndSecondCycle() {
            final BigDecimal d = factoredECTSCycle1.add(factoredECTSCycle2);
            return d.doubleValue() > 0 ? factoredGradeSumCycle1.add(factoredGradeSumCycle2).divide(d, 2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
        }

    }

}
