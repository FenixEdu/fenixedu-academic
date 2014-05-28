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
package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class Over23IndividualCandidacyDegreeEntry extends Over23IndividualCandidacyDegreeEntry_Base {

    static Comparator<Over23IndividualCandidacyDegreeEntry> COMPARATOR_BY_ORDER =
            new Comparator<Over23IndividualCandidacyDegreeEntry>() {
                @Override
                public int compare(Over23IndividualCandidacyDegreeEntry o1, Over23IndividualCandidacyDegreeEntry o2) {
                    return o1.getDegreeOrder().compareTo(o2.getDegreeOrder());
                }
            };

    private Over23IndividualCandidacyDegreeEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    Over23IndividualCandidacyDegreeEntry(final Over23IndividualCandidacy candidacy, final Degree degree, final int order) {
        this();
        checkParameters(candidacy, degree);
        checkOrder(candidacy, degree, order);
        super.setOver23IndividualCandidacy(candidacy);
        super.setDegree(degree);
        super.setDegreeOrder(order);
    }

    private void checkParameters(final Over23IndividualCandidacy candidacy, final Degree degree) {
        if (candidacy == null) {
            throw new DomainException("error.Over23IndividualCandidacyDegreeEntry.invalid.candidacy");
        }
        if (degree == null) {
            throw new DomainException("error.Over23IndividualCandidacyDegreeEntry.invalid.degree");
        }
    }

    private void checkOrder(final Over23IndividualCandidacy candidacy, final Degree degree, int order) {
        for (final Over23IndividualCandidacyDegreeEntry entry : candidacy.getOver23IndividualCandidacyDegreeEntriesSet()) {
            if (entry.isFor(degree) && entry.hasDegreeOrder(order)) {
                throw new DomainException("error.Over23IndividualCandidacyDegreeEntry.found.duplicated.order");
            }
        }
    }

    public boolean isFor(Degree degree) {
        return getDegree().equals(degree);
    }

    public boolean hasDegreeOrder(int order) {
        return getDegreeOrder().intValue() == order;
    }

    void delete() {
        setDegree(null);
        setOver23IndividualCandidacy(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
