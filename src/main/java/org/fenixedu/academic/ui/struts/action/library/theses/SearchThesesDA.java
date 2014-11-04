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
package org.fenixedu.academic.ui.struts.action.library.theses;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.dto.thesis.ThesisSearchBean;
import org.fenixedu.academic.ui.struts.action.library.LibraryApplication;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

/**
 * @author Pedro Santos (pmrsa)
 */
@StrutsFunctionality(app = LibraryApplication.class, path = "thesis-validation", titleKey = "thesis.validation.title.list",
        accessGroup = "nobody")
@Mapping(module = "library", path = "/theses/search")
@Forwards(@Forward(name = "search", path = "/library/theses/search.jsp"))
public class SearchThesesDA extends ThesisLibraryDA {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisSearchBean search = new ThesisSearchBean();
        performSearch(request, search);
        return mapping.findForward("search");
    }

    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String text = request.getParameter("text");
        String state = request.getParameter("state");
        String year = request.getParameter("year");
        ThesisSearchBean search = new ThesisSearchBean(text, state, year);
        performSearch(request, search);
        return mapping.findForward("search");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ThesisSearchBean search = getRenderedObject("search");
        performSearch(request, search);
        return mapping.findForward("search");
    }

    private void performSearch(HttpServletRequest request, ThesisSearchBean search) {
        if (request.getAttribute("sortBy") != null) {
            request.setAttribute("sortBy", request.getAttribute("sortBy"));
        }
        request.setAttribute("searchFilter", search);
        request.setAttribute("searchArgs", buildSearchArgs(search));
        List<Thesis> theses = new ArrayList<Thesis>();
        for (Thesis thesis : Thesis.getEvaluatedThesis()) {
            if (!thesis.isFinalAndApprovedThesis()) {
                continue;
            }
            if (search.isMatch(thesis)) {
                theses.add(thesis);
            }
        }
        List<Thesis> result = theses;
        request.setAttribute("thesesFound", result.size());
        request.setAttribute("theses", result);
    }
}
