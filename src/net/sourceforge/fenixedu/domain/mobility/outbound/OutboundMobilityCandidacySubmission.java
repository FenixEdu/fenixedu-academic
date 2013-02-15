package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class OutboundMobilityCandidacySubmission extends OutboundMobilityCandidacySubmission_Base {

    public OutboundMobilityCandidacySubmission(final OutboundMobilityCandidacyPeriod candidacyPeriod, final Registration registration) {
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

}
