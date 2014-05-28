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
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "PortalResources", path = "website-manager", titleKey = "portal.webSiteManager.name",
        hint = "Website Manager", accessGroup = "role(WEBSITE_MANAGER)")
@Mapping(module = "webSiteManager", path = "/index")
@Forwards(@Forward(name = "list", path = "/webSiteManager/firstPage.jsp"))
public class ListSitesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);

        SortedSet<UnitSite> sites = new TreeSet<UnitSite>(new Comparator<UnitSite>() {
            @Override
            public int compare(UnitSite o1, UnitSite o2) {
                return o1.getUnit().getName().compareTo(o2.getUnit().getName());
            }
        });
        sites.addAll(person.getUnitSitesSet());
        request.setAttribute("sites", sites);

        return mapping.findForward("list");
    }

}
