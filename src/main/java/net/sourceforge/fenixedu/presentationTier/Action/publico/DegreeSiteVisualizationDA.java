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

import java.net.MalformedURLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/showDegreeSiteContent", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "site-section-adviseLogin", path = "degree-section-adviseLogin"),
        @Forward(name = "degree-description", path = "showDescription"),
        @Forward(name = "site-section", path = "degree-section"), @Forward(name = "site-item", path = "degree-item"),
        @Forward(name = "site-item-adviseLogin", path = "degree-item-adviseLogin"),
        @Forward(name = "site-item-deny", path = "degree-item-deny"),
        @Forward(name = "site-section-deny", path = "degree-section-deny") })
public class DegreeSiteVisualizationDA extends SiteVisualizationDA {

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("degree-description");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Degree degree = getDegree(request);
        if (degree != null) {
            request.setAttribute("degree", degree);
        }

        setPageLanguage(request);
        return super.execute(mapping, actionForm, request, response);
    }

    private void setPageLanguage(HttpServletRequest request) {
        Boolean inEnglish;

        String inEnglishParameter = request.getParameter("inEnglish");
        if (inEnglishParameter == null) {
            inEnglish = (Boolean) request.getAttribute("inEnglish");
        } else {
            inEnglish = new Boolean(inEnglishParameter);
        }

        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }

        request.setAttribute("inEnglish", inEnglish);
    }

    public Degree getDegree(HttpServletRequest request) {
        String parameter = request.getParameter("degreeID");

        if (parameter == null) {
            return null;
        }

        try {
            return FenixFramework.getDomainObject(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Degree degree = getDegree(request);

        if (degree == null) {
            return null;
        }

        if (degree.getSigla() == null) {
            return null;
        }

        try {
            return RequestUtils.absoluteURL(request, "/" + degree.getSigla().toLowerCase()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
