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
package net.sourceforge.fenixedu.domain.candidacy;

public enum CandidacySituationType {
    PRE_CANDIDACY(true),

    STAND_BY(true),

    STAND_BY_FILLED_DATA(true),

    STAND_BY_CONFIRMED_DATA(true),

    ADMITTED(true),

    CANCELLED(false),

    SUBSTITUTE(false),

    NOT_ADMITTED(false),

    REGISTERED(true);

    private boolean active;

    private CandidacySituationType(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return CandidacySituationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return CandidacySituationType.class.getName() + "." + name();
    }

    public boolean isActive() {
        return this.active;
    }
}
