package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacySubmission extends OutboundMobilityCandidacySubmission_Base implements
        Comparable<OutboundMobilityCandidacySubmission> {

    public OutboundMobilityCandidacySubmission(final OutboundMobilityCandidacyPeriod candidacyPeriod,
            final Registration registration) {
        setRootDomainObject(RootDomainObject.getInstance());
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

    @Atomic
    public void setGrade(final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade) {
        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            if (submissionGrade.getOutboundMobilityCandidacyContestGroup() == mobilityGroup) {
                submissionGrade.edit(grade);
                return;
            }
        }
        new OutboundMobilityCandidacySubmissionGrade(this, mobilityGroup, grade);
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
    public boolean hasRootDomainObject() {
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

    @Deprecated
    public boolean hasConfirmedPlacement() {
        return getConfirmedPlacement() != null;
    }

}
