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
package org.fenixedu.academic.ui.struts.action.alumni;

import org.fenixedu.bennu.struts.portal.StrutsApplication;

public class AlumniApplication {

    private static final String HINT = "Alumni";
    private static final String ACCESS_GROUP = "role(ALUMNI)";
    private static final String BUNDLE = "AlumniResources";

    @StrutsApplication(bundle = BUNDLE, path = "academic-path", titleKey = "academic.path", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class AlumniAcademicPathApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "professional-info", titleKey = "professional.info", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class AlumniProfessionalInfoApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "formation", titleKey = "label.formation", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class AlumniFormationApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "academic-services", titleKey = "academic.services", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class AlumniAcademicServicesApp {
    }

}
