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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.finalDegreeWork.CandidacyAttributionType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;

import org.apache.commons.collections.Predicate;

public class CandidacyFilterBean implements Serializable {

    private CandidacyAttributionType attributionStatus;

    public CandidacyFilterBean(CandidacyAttributionType status) {
        setStatus(status);
    }

    private void setStatus(CandidacyAttributionType status) {
        attributionStatus = status;
    }

    public CandidacyAttributionType getStatus() {
        return attributionStatus;
    }

    public Set<Predicate> getPredicates() {
        final Set<Predicate> predicates = new HashSet<Predicate>();
        predicates.add(new FinalDegreeWorkGroup.IsValidGroupPredicate());
        if (attributionStatus != null) {
            predicates.add(new FinalDegreeWorkGroup.AttributionStatusPredicate(attributionStatus));
        }
        return predicates;
    }

    public void setFromRequest(HttpServletRequest request) {
        String filter = request.getParameter("filter");
        if (filter != null) {
            try {
                CandidacyAttributionType status = CandidacyAttributionType.valueOf(filter);
                setStatus(status);
            } catch (IllegalArgumentException iae) {
            }
        }
    }

    @Override
    public String toString() {
        return attributionStatus.getDescription();
    }
}
