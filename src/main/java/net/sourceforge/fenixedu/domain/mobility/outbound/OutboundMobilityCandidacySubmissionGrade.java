package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class OutboundMobilityCandidacySubmissionGrade extends OutboundMobilityCandidacySubmissionGrade_Base implements
        Comparable<OutboundMobilityCandidacySubmissionGrade> {

    public OutboundMobilityCandidacySubmissionGrade(final OutboundMobilityCandidacySubmission submission,
            final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
        removeOutboundMobilityCandidacyContestGroup();
        removeOutboundMobilityCandidacySubmission();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
