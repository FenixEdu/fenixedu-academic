package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdCandidacyPeriodsProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return PhdCandidacyPeriod.readPhdCandidacyPeriods();
    }

}
