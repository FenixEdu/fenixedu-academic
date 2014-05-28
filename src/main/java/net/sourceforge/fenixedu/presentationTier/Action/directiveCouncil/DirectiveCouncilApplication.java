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
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ApplicationResources", path = "directive-council", titleKey = "role.directiveCouncil",
        hint = DirectiveCouncilApplication.HINT, accessGroup = DirectiveCouncilApplication.ACCESS_GROUP)
@Mapping(path = "/index", module = "directiveCouncil", parameter = "/directiveCouncil/index.jsp")
public class DirectiveCouncilApplication extends ForwardAction {

    protected static final String HINT = "Directive Council";
    protected static final String ACCESS_GROUP = "role(DIRECTIVE_COUNCIL)";

    @StrutsApplication(bundle = "ApplicationResources", path = "control", titleKey = "link.control", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DirectiveCouncilControlApp {

    }

    @StrutsApplication(bundle = "ApplicationResources", path = "external-supervision",
            titleKey = "link.directiveCouncil.externalSupervision", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DirectiveCouncilExternalSupervision {

    }

    @StrutsApplication(bundle = "DirectiveCouncilResources", path = "career-workshops",
            titleKey = "label.title.careerWorkshop.simple", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DirectiveCouncilCareerWorkshops {

    }

}
