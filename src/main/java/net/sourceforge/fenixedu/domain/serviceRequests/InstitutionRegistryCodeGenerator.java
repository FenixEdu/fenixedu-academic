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
package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class InstitutionRegistryCodeGenerator extends InstitutionRegistryCodeGenerator_Base {
    public InstitutionRegistryCodeGenerator() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected Integer getNextNumber(CycleType cycle) {
        switch (cycle) {
        case FIRST_CYCLE:
            super.setCurrentFirstCycle((super.getCurrentFirstCycle() != null ? super.getCurrentFirstCycle() : 0) + 1);
            return super.getCurrentFirstCycle();
        case SECOND_CYCLE:
            super.setCurrentSecondCycle((super.getCurrentSecondCycle() != null ? super.getCurrentSecondCycle() : 0) + 1);
            return super.getCurrentSecondCycle();
        case THIRD_CYCLE:
            super.setCurrentThirdCycle((super.getCurrentThirdCycle() != null ? super.getCurrentThirdCycle() : 0) + 1);
            return super.getCurrentThirdCycle();
        default:
            throw new DomainException("error.InstitutionRegistryCodeGenerator.unsupportedCycle");
        }
    }

    public RegistryCode createRegistryFor(IRegistryDiplomaRequest request) {
        return new RegistryCode(this, request);
    }

    public RegistryCode createRegistryFor(IDiplomaSupplementRequest request) {
        return new RegistryCode(this, request);
    }

    public RegistryCode createRegistryFor(IDiplomaRequest request) {
        return new RegistryCode(this, request);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode> getRegistryCode() {
        return getRegistryCodeSet();
    }

    @Deprecated
    public boolean hasAnyRegistryCode() {
        return !getRegistryCodeSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCurrentSecondCycle() {
        return getCurrentSecondCycle() != null;
    }

    @Deprecated
    public boolean hasCurrentThirdCycle() {
        return getCurrentThirdCycle() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

    @Deprecated
    public boolean hasCurrentFirstCycle() {
        return getCurrentFirstCycle() != null;
    }

}
