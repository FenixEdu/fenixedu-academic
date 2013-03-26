package net.sourceforge.fenixedu.domain.mobility.outbound;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class OutboundMobilityCandidacyPeriodConfirmationOption extends OutboundMobilityCandidacyPeriodConfirmationOption_Base
        implements Comparable<OutboundMobilityCandidacyPeriodConfirmationOption> {

    public OutboundMobilityCandidacyPeriodConfirmationOption(final OutboundMobilityCandidacyPeriod period,
            final String optionValue) {
        setRootDomainObject(RootDomainObject.getInstance());
        setOptionValue(optionValue);
        setOutboundMobilityCandidacyPeriod(period);
    }

    @Service
    public void delete() {
        removeOutboundMobilityCandidacyPeriod();
        removeRootDomainObject();
        deleteDomainObject();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyPeriodConfirmationOption o) {
        return getExternalId().compareTo(o.getExternalId());
    }

}
