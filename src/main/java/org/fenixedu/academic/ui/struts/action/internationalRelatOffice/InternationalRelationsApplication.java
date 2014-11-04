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
package org.fenixedu.academic.ui.struts.action.internationalRelatOffice;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "InternRelationOfficeResources", path = "international-relations",
        titleKey = "title.internationalrelations", hint = InternationalRelationsApplication.HINT,
        accessGroup = InternationalRelationsApplication.ACCESS_GROUP)
@Mapping(path = "/index", module = "internationalRelatOffice", parameter = "/internationalRelatOffice/index.jsp")
public class InternationalRelationsApplication extends ForwardAction {

    static final String BUNDLE = "InternRelationOfficeResources";
    static final String HINT = "International Relations";
    static final String ACCESS_GROUP = "role(INTERNATIONAL_RELATION_OFFICE)";

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "consult", titleKey = "link.consult", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class InternRelationsConsultApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "mobility", titleKey = "title.mobility", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class InternRelationsMobilityApp {
    }

}
