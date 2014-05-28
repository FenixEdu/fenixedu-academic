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
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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

    @StrutsApplication(bundle = "InquiriesResources", path = "inquiries", titleKey = "label.inquiries", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class GepInquiriesApp {
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
