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

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum ProfessionType {

    UNKNOWN(false),

    PUBLIC_ADMINISTRATION_BOARD_OR_DIRECTOR_AND_BOARD_OF_COMPANIES(true),

    CIENTIFIC_AND_INTELECTUAL_PROFESSION_SPECIALIST(true),

    INTERMEDIATE_LEVEL_TECHNICALS_AND_PROFESSIONALS(true),

    ADMINISTRATIVE_STAFF_AND_SIMMILAR(true),

    SALES_AND_SERVICE_STAFF(true),

    FARMERS_AND_AGRICULTURE_AND_FISHING_QUALIFIED_WORKERS(true),

    WORKERS_CRAFTSMEN_AND_SIMMILAR(true),

    INSTALLATION_AND_MACHINE_WORKERS_AND_LINE_ASSEMBLY_WORKERS(true),

    NON_QUALIFIED_WORKERS(true),

    MILITARY_MEMBER(true),

    OTHER(true);

    private boolean active;

    private ProfessionType(boolean active) {
        setActive(active);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ProfessionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return ProfessionType.class.getName() + "." + name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

}
