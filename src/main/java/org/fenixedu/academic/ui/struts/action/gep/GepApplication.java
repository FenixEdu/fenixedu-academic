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
package org.fenixedu.academic.ui.struts.action.gep;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "GEPResources", path = "gep", titleKey = "label.gep.fullName", hint = "Gep",
        accessGroup = "role(GEP)")
@Mapping(path = "/index", module = "gep", parameter = "/gep/index.jsp")
public class GepApplication extends ForwardAction {

    static final String HINT = "Gep";
    static final String ACCESS_GROUP = "role(GEP)";
    static final String BUNDLE = "GEPResources";

    @StrutsApplication(bundle = BUNDLE, path = "gep", titleKey = "label.gep.portal.tilte", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class GepPortalApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "alumni", titleKey = "label.alumni", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class GepAlumniApp {
    }

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "registered-degree-candidacies",
            titleKey = "label.registeredDegreeCandidacies.first.time.student.registration", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class GepRegisteredDegreeCandidaciesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "raides", titleKey = "title.personal.ingression.data.viewer", hint = HINT,
            accessGroup = "#managers")
    public static class GepRAIDESApp {
    }

}
