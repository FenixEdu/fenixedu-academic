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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.finalDegreeWork.CandidacyAttributionType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ProposalsFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum AttributionFilter {
        ATTRIBUTED, NOT_ATTRIBUTED, ALL;
    }

    public static enum WithCandidatesFilter {
        WITH_CANDIDATES, WITHOUT_CANDIDATES, ALL;
    }

    public static class StatusCountPair implements Serializable {

        private static final long serialVersionUID = 1L;

        private ProposalStatusType status;
        private int count;

        public StatusCountPair(ProposalStatusType status, int count) {
            this.status = status;
            this.count = count;
        }

        public ProposalStatusType getStatus() {
            return status;
        }

        public int getCount() {
            return count;
        }

        public void setStatus(ProposalStatusType status) {
            this.status = status;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getPresentationString() {
            return String.format("%s (%s)", RenderUtils.getEnumString(getStatus()), getCount());
        }

    }

    private final List<StatusCountPair> statusCount = new ArrayList<StatusCountPair>();

    private StatusCountPair status;
    private AttributionFilter attribution;
    private WithCandidatesFilter withCandidates;

    public ProposalsFilterBean(ProposalsSummaryBean summary) {
        final StatusCountPair startValue = new StatusCountPair(ProposalStatusType.TOTAL, summary.getTotalProposalsCount());
        statusCount.add(startValue);
        statusCount.add(new StatusCountPair(ProposalStatusType.FOR_APPROVAL, summary.getForApprovalProposalsCount()));
        statusCount.add(new StatusCountPair(ProposalStatusType.APPROVED, summary.getApprovedProposalsCount()));
        statusCount.add(new StatusCountPair(ProposalStatusType.PUBLISHED, summary.getPublishedProposalsCount()));
        setStatus(startValue);
        setAttribution(AttributionFilter.ALL);
        setWithCandidates(WithCandidatesFilter.ALL);
    }

    public StatusCountPair getStatusCount(ProposalStatusType status) {
        for (StatusCountPair statusCount : this.statusCount) {
            if (statusCount.getStatus().equals(status)) {
                return statusCount;
            }
        }
        return null;
    }

    public List<StatusCountPair> getStatusCount() {
        return statusCount;
    }

    public StatusCountPair getStatus() {
        return status;
    }

    public void setStatus(StatusCountPair status) {
        this.status = status;
    }

    public AttributionFilter getAttribution() {
        return attribution;
    }

    public void setAttribution(AttributionFilter attribution) {
        this.attribution = attribution;
    }

    public WithCandidatesFilter getWithCandidates() {
        return withCandidates;
    }

    public void setWithCandidates(WithCandidatesFilter withCandidates) {
        this.withCandidates = withCandidates;
    }

    public Set<Predicate> getPredicates() {
        final Set<Predicate> predicates = new HashSet<Predicate>();
        final Set<CandidacyAttributionType> attributionTypes = new HashSet<CandidacyAttributionType>();
        final ProposalStatusType status = getStatus().getStatus();

        if (status != ProposalStatusType.TOTAL) {
            predicates.add(new Proposal.StatusPredicate(status));
        }

        switch (getAttribution()) {
        case ATTRIBUTED:
            attributionTypes.add(CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR);
            attributionTypes.add(CandidacyAttributionType.ATTRIBUTED);
            break;
        case NOT_ATTRIBUTED:
            attributionTypes.add(CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED);
            attributionTypes.add(CandidacyAttributionType.NOT_ATTRIBUTED);
            break;
        }
        if (!attributionTypes.isEmpty()) {
            predicates.add(new Proposal.CandidacyAttributionPredicate(attributionTypes));
        }
        if (getWithCandidates() != WithCandidatesFilter.ALL) {
            predicates.add(new Proposal.HasCandidatesPredicate(getWithCandidates()));
        }
        return predicates;
    }

    @Override
    public String toString() {
        StringBuffer label = new StringBuffer();
        List<String> filters = new ArrayList<String>();
        final String string = BundleUtil.getString(Bundle.ENUMERATION, getStatus().getStatus().name());
        filters.add(string);
        if (getAttribution() != AttributionFilter.ALL) {
            filters.add(RenderUtils.getEnumString(getAttribution()));
        }
        if (getWithCandidates() != WithCandidatesFilter.ALL) {
            filters.add(RenderUtils.getEnumString(getWithCandidates()));
        }
        Iterator<String> iterator = filters.iterator();
        while (iterator.hasNext()) {
            label.append(iterator.next());
            if (iterator.hasNext()) {
                label.append(" & ");
            }
        }
        return label.toString();
    }

    public void setFromRequest(HttpServletRequest request) {
        String filter = request.getParameter("filter");
        if (filter != null) {
            ProposalStatusType status = ProposalStatusType.valueOf(filter);
            setStatus(getStatusCount(status));
        }
    }

    public String getProposalsFilterAsParameters() {
        String parameters = "";
        parameters += "&amp;proposalStatusType=" + getStatus().getStatus();
        parameters += "&amp;attributionFilter=" + getAttribution();
        parameters += "&amp;withCandidatesFilter=" + getWithCandidates();
        return parameters;
    }
}
