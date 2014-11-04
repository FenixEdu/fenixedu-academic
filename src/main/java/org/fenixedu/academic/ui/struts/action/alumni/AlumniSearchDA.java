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
package org.fenixedu.academic.ui.struts.action.alumni;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Alumni;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.alumni.AlumniSearchBean;
import org.fenixedu.academic.ui.struts.action.alumni.AlumniApplication.AlumniAcademicPathApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AlumniAcademicPathApp.class, path = "search-alumni", titleKey = "link.search.alumni")
@Mapping(module = "alumni", path = "/searchAlumni")
@Forwards({ @Forward(name = "viewAlumniDetails", path = "/alumni/viewAlumniDetails.jsp"),
        @Forward(name = "showAlumniList", path = "/alumni/showAlumniList.jsp") })
public class AlumniSearchDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showAlumniList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniSearchBean searchBean = reconstructBeanFromRequest(request);

        if (!StringUtils.isEmpty(searchBean.getName()) || searchBean.getDegreeType() != null) {

            List<Registration> resultRegistrations = readAlumniRegistrations(searchBean);

            if (request.getParameter("sort") != null && request.getParameter("sort").length() > 0) {
                resultRegistrations = RenderUtils.sortCollectionWithCriteria(resultRegistrations, request.getParameter("sort"));
            } else {
                resultRegistrations = RenderUtils.sortCollectionWithCriteria(resultRegistrations, "student.person.name");
            }

            searchBean.setAlumni(new ArrayList<Registration>(resultRegistrations));
            searchBean.setTotalItems(resultRegistrations.size());
        }

        request.setAttribute("searchAlumniBean", searchBean);
        return mapping.findForward("showAlumniList");
    }

    public ActionForward degreeTypePostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniSearchBean searchBean = reconstructBeanFromRequest(request);
        RenderUtils.invalidateViewState();
        request.setAttribute("searchAlumniBean", searchBean);
        return mapping.findForward("showAlumniList");
    }

    protected AlumniSearchBean reconstructBeanFromRequest(HttpServletRequest request) {

        final IViewState viewState = RenderUtils.getViewState("searchAlumniBean");
        if (viewState != null) {

            return (AlumniSearchBean) viewState.getMetaObject().getObject();

        } else if (request.getParameter("beansearch") != null && request.getParameter("beansearch").length() > 0) {
            return AlumniSearchBean.getBeanFromParameters(request.getParameter("beansearch"));
        }

        return new AlumniSearchBean();
    }

    public ActionForward visualizeAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniData", getDomainObject(request, "studentId"));
        return mapping.findForward("viewAlumniDetails");
    }

    protected List<Registration> readAlumniRegistrations(AlumniSearchBean searchBean) {
        return Alumni.readAlumniRegistrations(searchBean);
    }
}
