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
package net.sourceforge.fenixedu.domain.internship;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;

public class InternshipCandidacySession extends InternshipCandidacySession_Base {
    public InternshipCandidacySession(Interval interval) {
        super();
        setCandidacyInterval(interval);
        setRootDomainObject(Bennu.getInstance());
    }

    /**
     * This will deep erase the candidacy session, all candidacies created in
     * this session will be wiped out.
     */
    @Atomic
    public void delete() {
        for (; hasAnyInternshipCandidacy(); getInternshipCandidacy().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public String getPresentationName() {
        return getCandidacyInterval().getStart().toLocalDate() + "/" + getCandidacyInterval().getEnd().toLocalDate();
    }

    public static InternshipCandidacySession getMostRecentCandidacySession() {
        InternshipCandidacySession current = null;
        for (InternshipCandidacySession session : Bennu.getInstance().getInternshipCandidacySessionSet()) {
            if (current == null || session.getCandidacyInterval().isAfter(current.getCandidacyInterval())) {
                current = session;
            }
        }
        return current;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getInternshipCandidacy() {
        return getInternshipCandidacySet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacy() {
        return !getInternshipCandidacySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit> getUniversity() {
        return getUniversitySet();
    }

    @Deprecated
    public boolean hasAnyUniversity() {
        return !getUniversitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Country> getDestination() {
        return getDestinationSet();
    }

    @Deprecated
    public boolean hasAnyDestination() {
        return !getDestinationSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCandidacyInterval() {
        return getCandidacyInterval() != null;
    }

}
