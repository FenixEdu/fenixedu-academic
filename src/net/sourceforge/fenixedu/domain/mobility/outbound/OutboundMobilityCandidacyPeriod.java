package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;

public class OutboundMobilityCandidacyPeriod extends OutboundMobilityCandidacyPeriod_Base implements Comparable<CandidacyPeriod> {

    public OutboundMobilityCandidacyPeriod(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        super();
        init(executionInterval, start, end);
    }

    @Service
    public static OutboundMobilityCandidacyPeriod create(final ExecutionInterval executionInterval, final DateTime start,
            final DateTime end) {
        return new OutboundMobilityCandidacyPeriod(executionInterval, start, end);
    }

    @Service
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
        mobilityGroup.getOutboundMobilityCandidacyContestCount();
        return contest;
    }

    @Service
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

    @Service
    public void setOptionIntroductoryDestriptionService(final String optionIntroductoryDestription) {
        setOptionIntroductoryDestription(optionIntroductoryDestription);        
    }

    @Service
    public void addOption(final String optionValue) {
        new OutboundMobilityCandidacyPeriodConfirmationOption(this, optionValue);        
    }

    public SortedSet<OutboundMobilityCandidacyPeriodConfirmationOption> getSortedOptions() {
        return new TreeSet<OutboundMobilityCandidacyPeriodConfirmationOption>(getOutboundMobilityCandidacyPeriodConfirmationOptionSet());
    }

    public SortedSet<OutboundMobilityCandidacySubmission> getSortedSubmissionSet(final OutboundMobilityCandidacyContestGroup mobilityGroup) {
        final SortedSet<OutboundMobilityCandidacySubmission> result = new TreeSet<OutboundMobilityCandidacySubmission>(new Comparator<OutboundMobilityCandidacySubmission>() {
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

}
