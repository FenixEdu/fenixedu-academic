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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import org.fenixedu.bennu.portal.StrutsApplication;

public class CoordinatorApplication {

    private static final String HINT = "Coordinator";
    private static final String ACCESS_GROUP = "role(COORDINATOR)";

    @StrutsApplication(bundle = "ApplicationResources", path = "manage", titleKey = "label.coordinator.management", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class CoordinatorManagementApp {
    }

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.phds", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class CoordinatorPhdApp {
    }

}
