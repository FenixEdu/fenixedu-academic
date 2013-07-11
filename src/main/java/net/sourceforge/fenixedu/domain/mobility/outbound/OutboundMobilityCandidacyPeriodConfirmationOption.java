package net.sourceforge.fenixedu.domain.mobility.outbound;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacyPeriodConfirmationOption extends OutboundMobilityCandidacyPeriodConfirmationOption_Base
        implements Comparable<OutboundMobilityCandidacyPeriodConfirmationOption> {

    public OutboundMobilityCandidacyPeriodConfirmationOption(final OutboundMobilityCandidacyPeriod period,
            final String optionValue, final Boolean availableForCandidates) {
        setRootDomainObject(RootDomainObject.getInstance());
        setOptionValue(optionValue);
        setAvailableForCandidates(availableForCandidates);
        setOutboundMobilityCandidacyPeriod(period);
    }

    @Atomic
    public void delete() {
        setOutboundMobilityCandidacyPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyPeriodConfirmationOption o) {
        return getExternalId().compareTo(o.getExternalId());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission> getSubmissionsThatSelectedOption() {
        return getSubmissionsThatSelectedOptionSet();
    }

    @Deprecated
    public boolean hasAnySubmissionsThatSelectedOption() {
        return !getSubmissionsThatSelectedOptionSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasOptionValue() {
        return getOptionValue() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacyPeriod() {
        return getOutboundMobilityCandidacyPeriod() != null;
    }

    @Deprecated
    public boolean hasAvailableForCandidates() {
        return getAvailableForCandidates() != null;
    }

}
