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
package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.thesis.ThesisSearchBean;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.library.LibraryApplication;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
