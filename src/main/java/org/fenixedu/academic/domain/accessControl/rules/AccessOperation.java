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
package org.fenixedu.academic.domain.accessControl.rules;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public interface AccessOperation<R extends AccessRule, T extends AccessTarget> extends Serializable {

    public String exportAsString();

    public static AccessOperation<?, ?> importFromString(String serialized) {
        try {
            String[] parts = serialized.split(":");
            Class<? extends Enum> type = (Class<? extends Enum>) Class.forName(parts[0]);
            return (AccessOperation<?, ?>) Enum.valueOf(type, parts[1]);
        } catch (ClassNotFoundException e) {
            throw new Error("Could not parse AcademicOperation:" + serialized);
        }
    }

    public String getLocalizedName();

    public Optional<R> grant(Group whoCanAccess, Set<T> whatCanAffect);

    public Optional<R> grant(User user);

    public Optional<R> revoke(User user);
}
