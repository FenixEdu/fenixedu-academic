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

import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityProgram;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import org.fenixedu.academic.ui.struts.action.mobility.outbound.OutboundMobilityContextBean;

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
