package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OutboundMobilityCandidacyGroupProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final OutboundMobilityContextBean contextBean = (OutboundMobilityContextBean) source;
        if (contextBean.getCandidacyPeriods().size() == 1) {
            return contextBean.getCandidacyPeriods().iterator().next().getOutboundMobilityCandidacyContestGroupSet();
        }
        final SortedSet<OutboundMobilityCandidacyContestGroup> result = new TreeSet<OutboundMobilityCandidacyContestGroup>();
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : contextBean.getCandidacyPeriods()) {
            result.addAll(candidacyPeriod.getOutboundMobilityCandidacyContestGroupSet());
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
