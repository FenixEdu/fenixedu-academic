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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisLibraryOperation;
import org.fenixedu.academic.dto.thesis.ThesisSearchBean;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(module = "library", path = "/theses/validate", functionality = SearchThesesDA.class)
@Forwards(@Forward(name = "view", path = "/library/theses/validate.jsp"))
public class ValidateThesisDA extends ThesisLibraryDA {

    protected Thesis getThesis(HttpServletRequest request) {
        return getDomainObject(request, "thesisID");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String text = null;
        String state = null;
        String year = null;
        if (request.getParameter("text") != null) {
            text = URLEncoder.encode(request.getParameter("text"), "UTF-8");
        }
        if (request.getParameter("state") != null) {
            state = request.getParameter("state");
        }
        if (request.getParameter("year") != null) {
            year = request.getParameter("year");
        }
        request.setAttribute("searchArgs", buildSearchArgs(new ThesisSearchBean(text, state, year)));
        request.setAttribute("thesis", getThesis(request));
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("view", true);
        return mapping.findForward("view");
    }

    public ActionForward prepareValidate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("validate", true);
        return mapping.findForward("view");
    }

    public ActionForward preparePending(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("pending", true);
        return mapping.findForward("view");
    }

    public ActionForward prepareEditPending(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        request.setAttribute("editPending", true);
        return mapping.findForward("view");
    }

    public ActionForward history(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<ThesisLibraryOperation> operations = new ArrayList<ThesisLibraryOperation>();
        Thesis thesis = getThesis(request);
        if (thesis.getLastLibraryOperation() != null) {
            ThesisLibraryOperation last = thesis.getLastLibraryOperation();
            do {
                operations.add(last);
            } while ((last = last.getPrevious()) != null);
        }
        request.setAttribute("history", operations);
        return view(mapping, actionForm, request, response);
    }
}
