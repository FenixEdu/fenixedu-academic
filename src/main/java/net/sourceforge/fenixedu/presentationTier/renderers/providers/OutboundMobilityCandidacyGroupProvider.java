package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OutboundMobilityCandidacyGroupProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final OutboundMobilityContextBean contextBean = (OutboundMobilityContextBean) source;
        if (contextBean.getCandidacyPeriods().size() == 1) {
            return getCandidacyContestGroupSet(contextBean.getCandidacyPeriods().iterator().next());
        }
        final SortedSet<OutboundMobilityCandidacyContestGroup> result = new TreeSet<OutboundMobilityCandidacyContestGroup>();
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : contextBean.getCandidacyPeriods()) {
            result.addAll(getCandidacyContestGroupSet(candidacyPeriod));
        }
        return result;
    }

    public SortedSet<OutboundMobilityCandidacyContestGroup> getCandidacyContestGroupSet(
            final OutboundMobilityCandidacyPeriod period) {
        final Person person = AccessControl.getPerson();
        if (new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_MOBILITY_OUTBOUND).isMember(person)) {
            return period.getOutboundMobilityCandidacyContestGroupSet();
        }
        final SortedSet<OutboundMobilityCandidacyContestGroup> result = new TreeSet<OutboundMobilityCandidacyContestGroup>();
        if (person != null) {
            for (final OutboundMobilityCandidacyContestGroup group : person.getOutboundMobilityCandidacyContestGroupSet()) {
                if (hasContestForPeriod(period, group)) {
                    result.add(group);
                }
            }
        }
        return result;
    }

    private boolean hasContestForPeriod(final OutboundMobilityCandidacyPeriod period,
            final OutboundMobilityCandidacyContestGroup group) {
        for (final OutboundMobilityCandidacyContest contest : group.getOutboundMobilityCandidacyContestSet()) {
            if (contest.getOutboundMobilityCandidacyPeriod() == period) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
