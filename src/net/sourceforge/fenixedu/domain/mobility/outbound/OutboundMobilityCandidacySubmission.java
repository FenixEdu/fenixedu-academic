package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

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

    @Service
    public void apply(final OutboundMobilityCandidacyContest contest) {
        for (final OutboundMobilityCandidacy candidacy : getOutboundMobilityCandidacySet()) {
            if (candidacy.getOutboundMobilityCandidacyContest() == contest) {
                return;
            }
        }
        new OutboundMobilityCandidacy(contest, this);
    }

    @Service
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
        removeOutboundMobilityCandidacyPeriod();
        removeRegistration();
        removeRootDomainObject();
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

    @Service
    public void setGrade(final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade) {
        for (final OutboundMobilityCandidacySubmissionGrade submissionGrade : getOutboundMobilityCandidacySubmissionGradeSet()) {
            if (submissionGrade.getOutboundMobilityCandidacyContestGroup() == mobilityGroup) {
                submissionGrade.edit(grade);
            }
        }
        new OutboundMobilityCandidacySubmissionGrade(this, mobilityGroup, grade);
    }

}
