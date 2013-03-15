package net.sourceforge.fenixedu.domain.mobility.outbound;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.Interval;

public enum CandidacyGroupContestState {

    GATHER_CANDIDATES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || i.isAfterNow() ? CandidacyGroupContestStateStage.NOT_STARTED :
                i.isBeforeNow() ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    GRADE_CANDIDATES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() ? CandidacyGroupContestStateStage.NOT_STARTED :
                group.areAllStudentsGraded(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    SELECT_CANDIDACIES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() || !group.areAllStudentsGraded(period) ? CandidacyGroupContestStateStage.NOT_STARTED : 
                group.isCandidacySelectionConcluded(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    NOTIFY_CANDIDATES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() || !group.isCandidacySelectionConcluded(period) ? CandidacyGroupContestStateStage.NOT_STARTED : 
                group.areCandidatesNotofiedOfSelectionResults(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    CONCLUDED_CANDIDATE_CONFIRMATION() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() || !group.areCandidatesNotofiedOfSelectionResults(period) ? CandidacyGroupContestStateStage.NOT_STARTED : 
                group.haveAllCandidatesConfirmed(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    };

    public enum CandidacyGroupContestStateStage {
        NOT_STARTED, UNDER_WAY, COMPLETED;

        public String getLocalizedName() {
            return BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", "label." + name());
        }
    }

    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", "label." + name());
    }

    public abstract CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group, final OutboundMobilityCandidacyPeriod period );

}
