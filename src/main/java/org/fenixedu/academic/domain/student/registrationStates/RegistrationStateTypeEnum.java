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
/**
 * 
 */
package org.fenixedu.academic.domain.student.registrationStates;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum RegistrationStateTypeEnum {

    REGISTERED(true),

    MOBILITY(true),

    CANCELED(false),

    CONCLUDED(false),

    FLUNKED(false),

    INTERRUPTED(false),

    SCHOOLPARTCONCLUDED(false),

    INTERNAL_ABANDON(false),

    EXTERNAL_ABANDON(false),

    TRANSITION(false),

    TRANSITED(false),

    STUDYPLANCONCLUDED(false),

    INACTIVE(false); //Closed state for the registrations regarding the AFA & MA protocols

    private RegistrationStateTypeEnum(final boolean active) {
        this.active = active;
    }

    private boolean active;

    public String getName() {
        return name();
    }

    public boolean isActive() {
        return active;
    }

    public boolean isInactive() {
        return !active;
    }

    public String getQualifiedName() {
        return "RegistrationStateType." + name();
    }

    public String getFullyQualifiedName() {
        return "org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType." + name();
    }

    public String getDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

}
