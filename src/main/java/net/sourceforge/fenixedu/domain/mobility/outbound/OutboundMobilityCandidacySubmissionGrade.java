/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.Bennu;

public class OutboundMobilityCandidacySubmissionGrade extends OutboundMobilityCandidacySubmissionGrade_Base implements
        Comparable<OutboundMobilityCandidacySubmissionGrade> {

    public OutboundMobilityCandidacySubmissionGrade(final OutboundMobilityCandidacySubmission submission,
            final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade, final BigDecimal gradeForSerialization) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setOutboundMobilityCandidacySubmission(submission);
        setOutboundMobilityCandidacyContestGroup(mobilityGroup);
        edit(grade, gradeForSerialization);
    }

    public void edit(final BigDecimal grade, final BigDecimal gradeForSerialization) {
        setGrade(grade);
        setGradeForSerialization(gradeForSerialization);
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacySubmissionGrade o) {
        final int g = o.getGradeForSerialization().compareTo(getGradeForSerialization());
        return g == 0 ? getExternalId().compareTo(o.getExternalId()) : g;
    }

    public void delete() {
        setOutboundMobilityCandidacyContestGroup(null);
        setOutboundMobilityCandidacySubmission(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacySubmission() {
        return getOutboundMobilityCandidacySubmission() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGrade() {
        return getGrade() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacyContestGroup() {
        return getOutboundMobilityCandidacyContestGroup() != null;
    }

}
