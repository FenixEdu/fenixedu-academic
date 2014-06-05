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

import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.joda.time.Interval;

public enum CandidacyGroupContestState {

    GATHER_CANDIDATES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || i.isAfterNow() ? CandidacyGroupContestStateStage.NOT_STARTED : i.isBeforeNow() ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    GRADE_CANDIDATES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() ? CandidacyGroupContestStateStage.NOT_STARTED : group
                    .areAllStudentsGraded(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    SELECT_CANDIDACIES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() || !group.areAllStudentsGraded(period) ? CandidacyGroupContestStateStage.NOT_STARTED : group
                    .isCandidacySelectionConcluded(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    NOTIFY_CANDIDATES() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() || !group.isCandidacySelectionConcluded(period) ? CandidacyGroupContestStateStage.NOT_STARTED : group
                    .areCandidatesNotofiedOfSelectionResults(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    },
    CONCLUDED_CANDIDATE_CONFIRMATION() {
        @Override
        public CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
                final OutboundMobilityCandidacyPeriod period) {
            final Interval i = period.getInterval();
            return i == null || !i.isBeforeNow() || !group.areCandidatesNotofiedOfSelectionResults(period) ? CandidacyGroupContestStateStage.NOT_STARTED : group
                    .haveAllCandidatesConfirmed(period) ? CandidacyGroupContestStateStage.COMPLETED : CandidacyGroupContestStateStage.UNDER_WAY;
        }
    };

    public enum CandidacyGroupContestStateStage {
        NOT_STARTED, UNDER_WAY, COMPLETED;

        public String getLocalizedName() {
            return BundleUtil.getString(Bundle.ACADEMIC, "label." + name());
        }
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label." + name());
    }

    public abstract CandidacyGroupContestStateStage getStage(final OutboundMobilityCandidacyContestGroup group,
            final OutboundMobilityCandidacyPeriod period);

}
