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
package org.fenixedu.academic.ui.struts.action.publico;

import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "ApplicationResources", path = "public", titleKey = "title.public", hint = "Public")
public class PublicApplication {

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.php.program", hint = "Public")
    public static class PublicPhdApp {

    }

    @StrutsApplication(bundle = "CandidateResources", path = "candidacies", titleKey = "title.applications", hint = "Public")
    public static class PublicCandidaciesApp {

    }

}
