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

import java.util.ArrayList;
import java.util.List;

public enum GrantOwnerType {

    STUDENT_WITHOUT_SCHOLARSHIP(true, true),

    HIGHER_EDUCATION_SAS_GRANT_OWNER_CANDIDATE(false, true),

    HIGHER_EDUCATION_SAS_GRANT_OWNER(false, true),

    HIGHER_EDUCATION_NOT_SAS_GRANT_OWNER(true, true),

    FCT_GRANT_OWNER(true, false),

    ORIGIN_COUNTRY_GRANT_OWNER(true, true),

    OTHER_INSTITUTION_GRANT_OWNER(true, true);

    private boolean forPhDStudent;
    private boolean forFirstOrSecondCycle;

    private GrantOwnerType(boolean forPhDStudent, boolean forFirstOrSecondCycle) {
        this.forPhDStudent = forPhDStudent;
        this.forFirstOrSecondCycle = forFirstOrSecondCycle;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return GrantOwnerType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return GrantOwnerType.class.getName() + "." + name();
    }

    public boolean isForPhDStudent() {
        return this.forPhDStudent;
    }

    public boolean isForFirstOrSecondCycle() {
        return this.forFirstOrSecondCycle;
    }

    public static List<GrantOwnerType> getTypesForPhDStudent() {
        final List<GrantOwnerType> result = new ArrayList<GrantOwnerType>();

        for (final GrantOwnerType each : values()) {
            if (each.isForPhDStudent()) {
                result.add(each);
            }
        }

        return result;
    }

    public static List<GrantOwnerType> getTypesForFirstOrSecondCycle() {
        final List<GrantOwnerType> result = new ArrayList<GrantOwnerType>();

        for (final GrantOwnerType each : values()) {
            if (each.isForFirstOrSecondCycle()) {
                result.add(each);
            }
        }

        return result;
    }
}
