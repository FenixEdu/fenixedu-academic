package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OutboundMobilityProgramProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final OutboundMobilityContextBean contextBean = (OutboundMobilityContextBean) source;
        final SortedSet<MobilityProgram> result = new TreeSet<MobilityProgram>();
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : contextBean.getCandidacyPeriods()) {
            for (final OutboundMobilityCandidacyContest contest : candidacyPeriod.getOutboundMobilityCandidacyContestSet()) {
                final MobilityAgreement mobilityAgreement = contest.getMobilityAgreement();
                result.add(mobilityAgreement.getMobilityProgram());
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
