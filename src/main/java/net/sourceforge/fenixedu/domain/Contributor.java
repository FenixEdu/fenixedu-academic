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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * This class only exists to allow database access in scripts. A Contributor is
 * now a Party: either a Person or an ExternalUnit
 * 
 */

@Deprecated
public class Contributor extends Contributor_Base {

    private Contributor() {
        throw new DomainException("Contributor.class.is.deprecated");
    }

    public void edit(Integer contributorNumber, String contributorName, String contributorAddress) {
        Contributor contributor = Contributor.readByContributorNumber(contributorNumber);
        if (contributor != null && !contributor.equals(this)) {
            throw new DomainException("duplicate.contributor.number");
        }

        this.setContributorNumber(contributorNumber);
        this.setContributorName(contributorName);
        this.setContributorAddress(contributorAddress);
    }

    public void delete() {
        if (hasAnyGuides()) {
            throw new DomainException("contributor.cannot.be.deleted");
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static Contributor readByContributorNumber(final Integer contributorNumber) {
        for (final Contributor contributor : Bennu.getInstance().getContributorsSet()) {
            if (contributor.getContributorNumber().equals(contributorNumber)) {
                return contributor;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Guide> getGuides() {
        return getGuidesSet();
    }

    @Deprecated
    public boolean hasAnyGuides() {
        return !getGuidesSet().isEmpty();
    }

    @Deprecated
    public boolean hasContributorAddress() {
        return getContributorAddress() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasContributorName() {
        return getContributorName() != null;
    }

    @Deprecated
    public boolean hasContributorNumber() {
        return getContributorNumber() != null;
    }

}
