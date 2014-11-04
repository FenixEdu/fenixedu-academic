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
package org.fenixedu.academic.ui.struts.action.research;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.CareerType;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.teacher.Career;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.research.ResearcherApplication.CurriculumApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = CurriculumApp.class, path = "career", titleKey = "label.career")
@Mapping(path = "/career/careerManagement", module = "researcher")
@Forwards(@Forward(name = "showCareer", path = "/researcher/career/showCareer.jsp"))
public class CareerManagementDispatchAction extends FenixDispatchAction {
    @EntryPoint
    public ActionForward showCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = getLoggedPerson(request);
        SortedSet<Career> sortedCareer = new TreeSet<Career>(Career.CAREER_DATE_COMPARATOR);
        sortedCareer.addAll(Career.getCareersByType(person, CareerType.PROFESSIONAL));
        request.setAttribute("career", sortedCareer);
        return mapping.findForward("showCareer");
    }

    public ActionForward prepareCreateCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("creating", true);
        return showCareer(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Career career = getDomainObject(request, "id");
        request.setAttribute("editCareer", career);
        return showCareer(mapping, actionForm, request, response);
    }

    public ActionForward prepareDeleteCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Career career = getDomainObject(request, "id");
        request.setAttribute("deleteCareer", career);
        return showCareer(mapping, actionForm, request, response);
    }

    public ActionForward deleteCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Career career = getRenderedObject("deleteCareer");
        career.delete();
        return showCareer(mapping, actionForm, request, response);
    }
}
