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
package org.fenixedu.academic.domain.serviceRequests;

import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
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

}
