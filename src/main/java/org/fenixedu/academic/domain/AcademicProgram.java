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
package org.fenixedu.academic.domain;

import java.util.Collection;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public abstract class AcademicProgram extends AcademicProgram_Base {
    public AcademicProgram() {
        super();
    }

    public abstract DegreeType getDegreeType();

    public abstract Collection<CycleType> getCycleTypes();

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getAcademicAuthorizationGroupSet().isEmpty() || !getAccessRuleSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION,
                    "error.academicProgram.cannotDeleteBacauseUsedInAccessControl"));
        }
    }

    @Atomic
    public final void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        disconnect();
    }

    protected void disconnect() {
        setAdministrativeOffice(null);
        super.deleteDomainObject();
    }
}
