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
package net.sourceforge.fenixedu.presentationTier.Action.library;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = LibraryApplication.class, path = "operator", titleKey = "label.library.operator")
@Mapping(path = "/libraryOperator", module = "library")
@Forwards(@Forward(name = "libraryOperator", path = "/library/operator/libraryOperator.jsp"))
public class LibraryOperatorDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("attendance", new LibraryAttendance());
        return mapping.findForward("libraryOperator");
    }

    public ActionForward selectLibrary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryAttendance attendance = getAttendanceFromRequest(request, "attendance");
        RenderUtils.invalidateViewState();
        request.setAttribute("attendance", attendance);
        return mapping.findForward("libraryOperator");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryAttendance attendance = getAttendanceFromRequest(request, "search.person");
        RenderUtils.invalidateViewState();
        attendance.search();
        request.setAttribute("attendance", attendance);
        return mapping.findForward("libraryOperator");
    }

    public ActionForward advancedSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryAttendance attendance = getAttendanceFromRequest(request, "advanced.search");
        Integer pageNumber = getIntegerFromRequest(request, "pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
        }
        RenderUtils.invalidateViewState();
        attendance.advancedSearch(pageNumber);
        request.setAttribute("attendance", attendance);
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", attendance.getNumberOfPages());
        return mapping.findForward("libraryOperator");
    }

    public ActionForward exitPlace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryAttendance attendance = getRenderedObject("person.selectPlace");
        if (attendance == null) {
            SpaceAttendances spaceAttendance = FenixFramework.getDomainObject(request.getParameter("attendanceId"));
            Space library = FenixFramework.getDomainObject(request.getParameter("libraryId"));
            attendance = new LibraryAttendance(spaceAttendance, library);
        }
        RenderUtils.invalidateViewState();
        attendance.exitSpace();
        request.setAttribute("attendance", attendance);
        return mapping.findForward("libraryOperator");
    }

    public ActionForward selectPlace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryAttendance attendance = getAttendanceFromRequest(request, "person.selectPlace");
        RenderUtils.invalidateViewState();
        attendance.enterSpace();
        request.setAttribute("attendance", attendance);
        return mapping.findForward("libraryOperator");
    }

    private LibraryAttendance getAttendanceFromRequest(HttpServletRequest request, String renderId) {
        LibraryAttendance attendance = getRenderedObject(renderId);
        if (attendance == null) {
            Space library = FenixFramework.getDomainObject(request.getParameter("libraryId"));
            String personId = request.getParameter("personIstUsername");
            if (personId != null) {
                attendance = new LibraryAttendance(personId, library);
                attendance.search();
            } else {
                String personType = request.getParameter("personType");
                String personName = request.getParameter("personName");
                attendance = new LibraryAttendance(personType, personName, library);
            }
        }
        return attendance;
    }

}