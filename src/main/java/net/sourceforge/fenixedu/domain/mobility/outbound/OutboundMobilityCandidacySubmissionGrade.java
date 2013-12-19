package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.Bennu;

public class OutboundMobilityCandidacySubmissionGrade extends OutboundMobilityCandidacySubmissionGrade_Base implements
        Comparable<OutboundMobilityCandidacySubmissionGrade> {

    public OutboundMobilityCandidacySubmissionGrade(final OutboundMobilityCandidacySubmission submission,
            final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setOutboundMobilityCandidacySubmission(submission);
        setOutboundMobilityCandidacyContestGroup(mobilityGroup);
        edit(grade);
    }

    public void edit(final BigDecimal grade) {
        setGrade(grade);
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacySubmissionGrade o) {
        final int g = o.getGrade().compareTo(getGrade());
        return g == 0 ? getExternalId().compareTo(o.getExternalId()) : g;
    }

    public void delete() {
        setOutboundMobilityCandidacyContestGroup(null);
        setOutboundMobilityCandidacySubmission(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacySubmission() {
        return getOutboundMobilityCandidacySubmission() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGrade() {
        return getGrade() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacyContestGroup() {
        return getOutboundMobilityCandidacyContestGroup() != null;
    }

}
