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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED(true, true),

    MOBILITY(true, true),

    CANCELED(false, false),

    CONCLUDED(false, true),

    FLUNKED(false, false),

    INTERRUPTED(false, false),

    SCHOOLPARTCONCLUDED(true, true),

    INTERNAL_ABANDON(false, false),

    EXTERNAL_ABANDON(false, false),

    TRANSITION(false, true),

    TRANSITED(false, true),

    STUDYPLANCONCLUDED(false, true),

    INACTIVE(false, false); //Closed state for the registrations regarding the AFA & MA protocols

    private RegistrationStateType(final boolean active, final boolean canHaveCurriculumLinesOnCreation) {
        this.active = active;
        this.canHaveCurriculumLinesOnCreation = canHaveCurriculumLinesOnCreation;
    }

    private boolean active;

    private boolean canHaveCurriculumLinesOnCreation;

    public String getName() {
        return name();
    }

    public boolean isActive() {
        return active;
    }

    public boolean isInactive() {
        return !active;
    }

    public boolean canHaveCurriculumLinesOnCreation() {
        return canHaveCurriculumLinesOnCreation;
    }

    public String getQualifiedName() {
        return RegistrationStateType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return RegistrationStateType.class.getName() + "." + name();
    }

    public String getDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    public boolean canReingress() {
        return this == FLUNKED || this == INTERRUPTED || this == INTERNAL_ABANDON || this == EXTERNAL_ABANDON || this == CANCELED
                || this == INACTIVE;
    }

}
