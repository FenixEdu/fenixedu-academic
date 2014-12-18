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
package org.fenixedu.academic.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class exists for the sole purpose of keeping compatibility with the old
 * Role system. It is scheduled to be removed in FenixEdu Academic 5.
 */
@Deprecated
public class PersistentRoleGroup extends PersistentRoleGroup_Base {

    private static final Logger logger = LoggerFactory.getLogger(PersistentRoleGroup.class);

    private PersistentRoleGroup() {
        super();
    }

    @Override
    public Group toGroup() {
        try {
            logger.trace("Processing legacy group {}", getTargetGroup());
            return Group.parse(getTargetGroup());
        } catch (Exception e) {
            logger.error("Error parsing target group for " + this, e);
            return NobodyGroup.get();
        }
    }

}
