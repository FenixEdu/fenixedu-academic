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

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class AccessRuleSystem extends AccessRuleSystem_Base {
    private AccessRuleSystem() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static AccessRuleSystem getInstance() {
        if (Bennu.getInstance().getAccessRuleSystem() == null) {
            return initialize();
        }
        return Bennu.getInstance().getAccessRuleSystem();
    }

    @Atomic(mode = TxMode.WRITE)
    private static AccessRuleSystem initialize() {
        if (Bennu.getInstance().getAccessRuleSystem() == null) {
            return new AccessRuleSystem();
        }
        return Bennu.getInstance().getAccessRuleSystem();
    }

    public static Stream<AccessRule> accessRules() {
        return getInstance().getAccessRuleSet().stream();
    }

    public static Stream<AccessRule> accessRules(DateTime when) {
        Stream<AccessRule> deleted =
                getInstance().getDeletedAccessRuleSet().stream()
                        .filter(r -> r.getCreated().isBefore(when) && r.getRevoked().isAfter(when));
        Stream<AccessRule> current = getInstance().getAccessRuleSet().stream().filter(r -> r.getCreated().isBefore(when));
        return Stream.concat(deleted, current);
    }
}
