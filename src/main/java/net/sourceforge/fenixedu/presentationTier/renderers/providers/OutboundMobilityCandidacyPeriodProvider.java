package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OutboundMobilityCandidacyPeriodProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final OutboundMobilityContextBean contextBean = (OutboundMobilityContextBean) source;
        final SortedSet<OutboundMobilityCandidacyPeriod> candidacyPeriods = new TreeSet<OutboundMobilityCandidacyPeriod>();
        contextBean.getPossibleCandidacyPeriods(candidacyPeriods);
        return candidacyPeriods;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
