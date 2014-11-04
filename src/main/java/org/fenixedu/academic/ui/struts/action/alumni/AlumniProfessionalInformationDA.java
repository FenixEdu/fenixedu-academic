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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Job;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.alumni.AlumniJobBean;
import org.fenixedu.academic.service.services.alumni.CreateProfessionalInformation;
import org.fenixedu.academic.service.services.alumni.DeleteProfessionalInformation;
import org.fenixedu.academic.service.services.alumni.EditProfessionalInformation;
import org.fenixedu.academic.ui.struts.action.alumni.AlumniApplication.AlumniProfessionalInfoApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AlumniProfessionalInfoApp.class, path = "professional-information",
        titleKey = "link.professional.information")
@Mapping(module = "alumni", path = "/professionalInformation", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "innerProfessionalInformation", path = "/alumni/viewAlumniProfessionalInformation.jsp"),
        @Forward(name = "manageProfessionalInformation", path = "/alumni/alumniManageProfessionalInformation.jsp") })
public class AlumniProfessionalInformationDA extends AlumniEntityManagementDA {

    @EntryPoint
    public ActionForward innerProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumni", getAlumniFromLoggedPerson(request));
        return mapping.findForward("innerProfessionalInformation");
    }

    public ActionForward updateIsEmployedPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        RenderUtils.invalidateViewState("alumniEmployment");
        return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward prepareProfessionalInformationCreation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("jobCreateBean", new AlumniJobBean(getAlumniFromLoggedPerson(request)));
        return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward createBusinessAreaPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniJobBean viewStateBean = (AlumniJobBean) getObjectFromViewState("jobCreateBean");
        viewStateBean.updateSchema();
        RenderUtils.invalidateViewState("jobCreateBean");
        request.setAttribute("jobCreateBean", viewStateBean);
        return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward updateBusinessAreaPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniJobBean viewStateBean = (AlumniJobBean) getObjectFromViewState("jobUpdateBean");
        viewStateBean.updateSchema();
        RenderUtils.invalidateViewState("jobUpdateBean");
        request.setAttribute("jobUpdateBean", viewStateBean);
        return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward createProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            CreateProfessionalInformation.run((AlumniJobBean) getRenderedObject());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("jobCreateBean", getObjectFromViewState("jobCreateBean"));
            return mapping.findForward("manageProfessionalInformation");
        }

        return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward viewProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("jobView", getJob(request));
        return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward prepareUpdateProfessionalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("jobUpdateBean", new AlumniJobBean(getAlumniFromLoggedPerson(request), getJob(request)));
        return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward updateProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            EditProfessionalInformation.run((AlumniJobBean) getRenderedObject());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("jobUpdateBean", getObjectFromViewState("jobUpdateBean"));
            return mapping.findForward("manageProfessionalInformation");
        }
        return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward updateProfessionalInformationError(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("jobUpdateBean", getObjectFromViewState("jobUpdateBean"));
        return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward deleteProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (getFromRequest(request, "cancel") == null) {
            DeleteProfessionalInformation.run(getJob(request));
        }

        return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    protected Job getJob(HttpServletRequest request) {
        return getDomainObject(request, "jobId");
    }

}