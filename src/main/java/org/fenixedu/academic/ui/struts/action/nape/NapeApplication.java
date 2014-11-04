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
package org.fenixedu.academic.ui.struts.action.nape;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "PortalResources", path = "nape", titleKey = "portal.nape.name", hint = "NAPE",
        accessGroup = "role(NAPE)")
@Mapping(module = "nape", path = "/index", parameter = "/nape/index.jsp")
public class NapeApplication extends ForwardAction {

    static final String HINT = "NAPE";
    static final String ACCESS_GROUP = "role(NAPE)";

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "registered-candidacies",
            titleKey = "label.registeredDegreeCandidacies.first.time.student.registration", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class NapeRegisteredCandidaciesApp {
    }

    @StrutsApplication(bundle = "CandidateResources", path = "candidacies", titleKey = "title.applications", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class NapeCandidaciesApp {
    }

}
