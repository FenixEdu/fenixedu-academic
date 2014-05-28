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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AllSummaryBean extends SummaryBean {

    private final ThesisSummaryBean thesisSummaryBean;
    private final CandidaciesSummaryBean candidaciesSummaryBean;
    private final ProposalsSummaryBean proposalsSummaryBean;
    private final static String NULL_LABEL = "-";

    public AllSummaryBean(ExecutionDegree executionDegree, DegreeCurricularPlan degreeCurricularPlan) {
        super(executionDegree, degreeCurricularPlan);
        thesisSummaryBean = new ThesisSummaryBean(executionDegree, degreeCurricularPlan);
        candidaciesSummaryBean = new CandidaciesSummaryBean(executionDegree, degreeCurricularPlan);
        proposalsSummaryBean = new ProposalsSummaryBean(executionDegree, degreeCurricularPlan);
    }

    public ThesisSummaryBean getThesisSummary() {
        return thesisSummaryBean;
    }

    public CandidaciesSummaryBean getCandidaciesSummary() {
        return candidaciesSummaryBean;
    }

    public ProposalsSummaryBean getProposalsSummary() {
        return proposalsSummaryBean;
    }

    // public Integer getAttributedByCoordinatorCandidaciesCount() {
    // return
    // candidaciesSummaryBean.getAttributedByCoordinatorCandidaciesCount();
    // }
    //
    // public Integer getAttributedCandidaciesCount() {
    // return candidaciesSummaryBean.getAttributedCandidaciesCount();
    // }
    //
    // public Integer getAttributedNotConfirmedCandidaciesCount() {
    // return
    // candidaciesSummaryBean.getAttributedNotConfirmedCandidaciesCount();
    // }
    //
    // public Integer getNotAttributedCandidaciesCount() {
    // return candidaciesSummaryBean.getNotAttributedCandidaciesCount();
    // }
    //
    // public Integer getTotalProposalsCount() {
    // return proposalsSummaryBean.getTotalProposalsCount();
    //
    // }
    //
    // public Integer getApprovedProposalsCount() {
    // return proposalsSummaryBean.getApprovedProposalsCount();
    // }
    //
    // public Integer getPublishedProposalsCount() {
    // return proposalsSummaryBean.getPublishedProposalsCount();
    // }
    //
    // public Integer getForApprovalProposalsCount() {
    // return proposalsSummaryBean.getForApprovalProposalsCount();
    // }
    //
    // public ProposalStatusType getTotalProposalsKey() {
    // return proposalsSummaryBean.getTotalProposalsKey();
    // }
    //
    // public ProposalStatusType getApprovedProposalsKey() {
    // return proposalsSummaryBean.getApprovedProposalsKey();
    // }
    //
    // public ProposalStatusType getPublishedProposalsKey() {
    // return proposalsSummaryBean.getPublishedProposalsKey();
    // }
    //
    // public ProposalStatusType getForApprovalProposalsKey() {
    // return proposalsSummaryBean.getForApprovalProposalsKey();
    // }
    //
    // public Map<ThesisPresentationState, Integer> getThesisCount() {
    // return thesisSummaryBean.getThesisCount();
    // }

    public String getCandidacyPeriod() {
        Scheduleing scheduling = getExecutionDegree().getScheduling();

        final YearMonthDay startOfCandidacyPeriodDateYearMonthDay = scheduling.getStartOfCandidacyPeriodDateYearMonthDay();
        final String startDate =
                startOfCandidacyPeriodDateYearMonthDay == null ? NULL_LABEL : startOfCandidacyPeriodDateYearMonthDay.toString();

        final HourMinuteSecond startOfCandidacyPeriodTimeHourMinuteSecond =
                scheduling.getStartOfCandidacyPeriodTimeHourMinuteSecond();
        final String startTime =
                startOfCandidacyPeriodTimeHourMinuteSecond == null ? NULL_LABEL : startOfCandidacyPeriodTimeHourMinuteSecond
                        .toString();

        final YearMonthDay endOfCandidacyPeriodDateYearMonthDay = scheduling.getEndOfCandidacyPeriodDateYearMonthDay();
        final String endDate =
                endOfCandidacyPeriodDateYearMonthDay == null ? NULL_LABEL : endOfCandidacyPeriodDateYearMonthDay.toString();

        final HourMinuteSecond endOfCandidacyPeriodTimeHourMinuteSecond =
                scheduling.getEndOfCandidacyPeriodTimeHourMinuteSecond();
        final String endTime =
                endOfCandidacyPeriodTimeHourMinuteSecond == null ? NULL_LABEL : endOfCandidacyPeriodTimeHourMinuteSecond
                        .toString();

        return RenderUtils.getResourceString("APPLICATION_RESOURCES", "label.thesis.period", new Object[] { startDate, startTime,
                endDate, endTime });
    }

    public String getProposalsPeriod() {
        Scheduleing scheduling = getExecutionDegree().getScheduling();

        final YearMonthDay startOfProposalPeriodDateYearMonthDay = scheduling.getStartOfProposalPeriodDateYearMonthDay();
        final String startDate =
                startOfProposalPeriodDateYearMonthDay == null ? NULL_LABEL : startOfProposalPeriodDateYearMonthDay.toString();

        final HourMinuteSecond startOfProposalPeriodTimeHourMinuteSecond =
                scheduling.getStartOfProposalPeriodTimeHourMinuteSecond();
        final String startTime =
                startOfProposalPeriodTimeHourMinuteSecond == null ? NULL_LABEL : startOfProposalPeriodTimeHourMinuteSecond
                        .toString();

        final YearMonthDay endOfProposalPeriodDateYearMonthDay = scheduling.getEndOfProposalPeriodDateYearMonthDay();
        final String endDate =
                endOfProposalPeriodDateYearMonthDay == null ? NULL_LABEL : endOfProposalPeriodDateYearMonthDay.toString();

        final HourMinuteSecond endOfProposalPeriodTimeHourMinuteSecond = scheduling.getEndOfProposalPeriodTimeHourMinuteSecond();
        final String endTime =
                endOfProposalPeriodTimeHourMinuteSecond == null ? NULL_LABEL : endOfProposalPeriodTimeHourMinuteSecond.toString();

        return RenderUtils.getResourceString("APPLICATION_RESOURCES", "label.thesis.period", new Object[] { startDate, startTime,
                endDate, endTime });
    }
}
