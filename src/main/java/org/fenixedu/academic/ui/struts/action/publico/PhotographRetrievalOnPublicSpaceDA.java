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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.person.RetrievePersonalPhotoAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/retrievePersonalPhoto", module = "publico")
public class PhotographRetrievalOnPublicSpaceDA extends RetrievePersonalPhotoAction {
    /**
     * @deprecated use /user/photo/{username} instead
     */
    @Deprecated
    public ActionForward retrievePhotographOnPublicSpace(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final Person person = FenixFramework.getDomainObject(request.getParameter("personId"));
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location",
                CoreConfiguration.getConfiguration().applicationUrl() + "/user/photo/" + person.getUsername());
        return null;
    }

    /**
     * @deprecated use /user/photo/{username} instead
     */
    @Deprecated
    public ActionForward retrieveByIstId(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String uuid = request.getParameter("istId");

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", CoreConfiguration.getConfiguration().applicationUrl() + "/user/photo/" + uuid);
        return null;
    }
}
