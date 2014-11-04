/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import org.fenixedu.academic.ui.struts.action.mobility.outbound.OutboundMobilityContextBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

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
        final User user = Authenticate.getUser();
        if (AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_MOBILITY_OUTBOUND).isMember(user)) {
            return period.getOutboundMobilityCandidacyContestGroupSet();
        }
        final SortedSet<OutboundMobilityCandidacyContestGroup> result = new TreeSet<OutboundMobilityCandidacyContestGroup>();
        if (user != null && user.getPerson() != null) {
            for (final OutboundMobilityCandidacyContestGroup group : user.getPerson()
                    .getOutboundMobilityCandidacyContestGroupSet()) {
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
