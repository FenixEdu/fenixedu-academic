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
package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

public class TutorshipLog extends TutorshipLog_Base {

    public TutorshipLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setTutorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasTutorship() {
        return getTutorship() != null;
    }

    @Deprecated
    public boolean hasCountsWithSupport() {
        return getCountsWithSupport() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMotivation() {
        return getMotivation() != null;
    }

    @Deprecated
    public boolean hasRelativesSupport() {
        return getRelativesSupport() != null;
    }

    @Deprecated
    public boolean hasWishesTutor() {
        return getWishesTutor() != null;
    }

    @Deprecated
    public boolean hasAnnotations() {
        return getAnnotations() != null;
    }

    @Deprecated
    public boolean hasDifficultiesOrSpecialLimitations() {
        return getDifficultiesOrSpecialLimitations() != null;
    }

    @Deprecated
    public boolean hasSpaceToValidateStudentsRegistration() {
        return getSpaceToValidateStudentsRegistration() != null;
    }

    @Deprecated
    public boolean hasOptionNumberDegree() {
        return getOptionNumberDegree() != null;
    }

    @Deprecated
    public boolean hasHowManyReunions() {
        return getHowManyReunions() != null;
    }

}
