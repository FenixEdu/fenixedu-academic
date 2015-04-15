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
package org.fenixedu.academic.ui.struts.action.publico.alumni;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Alumni;
import org.fenixedu.academic.domain.AlumniRequestType;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.alumni.AlumniIdentityCheckRequestBean;
import org.fenixedu.academic.dto.alumni.publicAccess.AlumniLinkRequestBean;
import org.fenixedu.academic.dto.alumni.publicAccess.AlumniPasswordBean;
import org.fenixedu.academic.dto.alumni.publicAccess.AlumniPublicAccessBean;
import org.fenixedu.academic.service.services.alumni.RegisterAlumniData;
import org.fenixedu.academic.service.services.commons.alumni.AlumniNotificationService;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.publico.KaptchaAction;
import org.fenixedu.academic.ui.struts.action.publico.PublicApplication;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = PublicApplication.class, path = "alumni", titleKey = "label.alumni.main.title",
        bundle = "AlumniResources")
@Mapping(module = "publico", path = "/alumni")
@Forwards({
        @Forward(name = "alumniPublicAccessInner", path = "/publico/alumni/alumniPublicAccessInner.jsp"),
        @Forward(name = "alumniCreatePasswordRequest", path = "/publico/alumni/alumniCreatePasswordRequest.jsp"),
        @Forward(name = "alumniPublicAccessInformationInquiry", path = "/publico/alumni/alumniPublicAccessInformationInquiry.jsp"),
        @Forward(name = "alumniPublicAccessIdentityCheck", path = "/publico/alumni/alumniPublicAccessIdentityCheck.jsp"),
        @Forward(name = "alumniPublicAccess", path = "/publico/alumni/alumniPublicAccess.jsp"),
        @Forward(name = "alumniErrorSendMail", path = "/publico/alumni/alumniPublicAccessErrorSendMail.jsp"),
        @Forward(name = "alumniRegistrationResult", path = "/publico/alumni/alumniRegistrationResult.jsp"),
        @Forward(name = "alumniPasswordRequired", path = "/publico/alumni/alumniPasswordRequired.jsp"),
        @Forward(name = "alumniPublicAccessMessage", path = "/publico/alumni/alumniPublicAccessMessage.jsp"),
        @Forward(name = "alumniPublicAccessRegistrationEmail", path = "/publico/alumni/alumniPublicAccessRegistrationEmail.jsp") })
public class AlumniPublicAccessDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(AlumniPublicAccessDA.class);

    @EntryPoint
    public ActionForward initFenixPublicAccess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.getSession(true);

        if (getFromRequest(request, "showForm") != null) {
            request.setAttribute("showForm", "true");
            request.setAttribute("alumniBean", getFromRequest(request, "alumniBean"));
        } else {
            request.setAttribute("showForm", "false");
            request.setAttribute("alumniBean", new AlumniLinkRequestBean());
        }
        return mapping.findForward("alumniPublicAccess");
    }

    public ActionForward requestIdentityCheck(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniBean", new AlumniIdentityCheckRequestBean(AlumniRequestType.STUDENT_NUMBER_RECOVERY));
        return mapping.findForward("alumniPublicAccessIdentityCheck");
    }

    public ActionForward processRequestIdentityCheckError(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("alumniBean", getFromRequest(request, "alumniBean"));
        return mapping.findForward("alumniPublicAccessIdentityCheck");
    }

    public ActionForward processRequestIdentityCheck(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!validateCaptcha(mapping, request)) {
            return requestIdentityCheck(mapping, actionForm, request, response);
        }

        try {
            RegisterAlumniData.run((AlumniIdentityCheckRequestBean) getObjectFromViewState("alumniBean"));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            return processRequestIdentityCheckError(mapping, actionForm, request, response);
        }

        request.setAttribute("alumniPublicAccessTitle", "identity.check.request.successful.creation.title");
        request.setAttribute("alumniPublicAccessMessage", "identity.check.request.successful.creation.message");
        return mapping.findForward("alumniPublicAccessMessage");
    }

    private boolean validateCaptcha(ActionMapping mapping, HttpServletRequest request) {
        final String captchaResponse = request.getParameter("j_captcha_response");

        try {
            if (!KaptchaAction.validateResponse(request.getSession(), captchaResponse)) {
                addActionMessage("error", request, "captcha.wrong.word");
                return false;
            }
            return true;
        } catch (Exception e) { // may be thrown if the id is not valid
            request.setAttribute("captcha.unknown.error", "captcha.unknown.error");
            request.setAttribute("showForm", "true");
            request.setAttribute("alumniBean", getObjectFromViewState("alumniBean"));
            return false;
        }
    }

    // 1st validation: captcha validation
    public ActionForward validateFenixAcessData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final AlumniLinkRequestBean alumniBean = (AlumniLinkRequestBean) getObjectFromViewState("alumniBean");

        if (!alumniBean.getPrivacyPolicy().booleanValue()) {
            request.setAttribute("showForm", "true");
            request.setAttribute("privacyPolicyPublicAccessMessage", "privacy.policy.acceptance");
            request.setAttribute("alumniBean", alumniBean);
            return mapping.findForward("alumniPublicAccess");
        }

        if (!validateCaptcha(mapping, request)) {
            return mapping.findForward("alumniPublicAccess");
        }

        try {
            final Alumni alumni =
                    RegisterAlumniData.run(alumniBean.getStudentNumber(), alumniBean.getDocumentIdNumber().trim(),
                            alumniBean.getEmail());
            String url = AlumniNotificationService.getRegisterConclusionURL(alumni);
            request.setAttribute("alumniEmailSuccessMessage", "http" + url.split("http")[1]);
            request.setAttribute("alumni", alumni);

        } catch (DomainException e) {
            if ("error.no.registrations".equals(e.getKey())) {
                request.setAttribute("showReportError", "true");
                String alumniEmail = Installation.getInstance().getInstituitionalEmailAddress("alumni");
                request.setAttribute("errorMessage", getResources(request).getMessage(e.getKey(), e.getArgs(), alumniEmail));
            } else if ("error.no.concluded.registrations".equals(e.getKey()) || "error.person.no.student".equals(e.getKey())) {
                request.setAttribute("showReportError", "true");
                request.setAttribute("errorMessage", getResources(request).getMessage(e.getKey(), e.getArgs()));
            }
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("showForm", "true");
            request.setAttribute("alumniBean", alumniBean);
            return mapping.findForward("alumniPublicAccess");
        }
        return mapping.findForward("alumniPublicAccessRegistrationEmail");
    }

    public ActionForward innerFenixPublicAccessValidation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String alumniId = BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.first.argument");
        String urlToken = BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.second.argument");
        final Alumni alumni = getDomainObject(request, alumniId);

        if (StringUtils.isEmpty(alumniId) || StringUtils.isEmpty(urlToken) || alumni == null) {
            request.setAttribute("alumniPublicAccessTitle", "registration.error.old.request.link.title");
            request.setAttribute("alumniPublicAccessMessage", "error.alumni.wrong.arguments");
            return mapping.findForward("alumniPublicAccessMessage");
        }

        if (alumni.isRegistered() && !alumni.isRecoveringPassword()) {
            request.setAttribute("alumniPublicAccessTitle", "registration.error.old.request.link.title");
            request.setAttribute("alumniPublicAccessMessage", "error.alumni.already.registered");
            return mapping.findForward("alumniPublicAccessMessage");
        }

        if (!alumni.getUrlRequestToken().equals(UUID.fromString(request.getParameter(urlToken)))) {
            request.setAttribute("alumniPublicAccessTitle", "registration.error.old.request.link.title");
            request.setAttribute("alumniPublicAccessMessage", "registration.error.old.request.link.message");
            return mapping.findForward("alumniPublicAccessMessage");
        }

        request.setAttribute("alumniBean", new AlumniLinkRequestBean(alumni));
        request.setAttribute("alumniId", getFromRequest(request, alumniId));
        request.setAttribute("urlToken", request.getParameter(urlToken));
        return mapping.findForward("alumniPublicAccessInner");
    }

    public ActionForward registrationConclusion(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Alumni alumni = ((AlumniLinkRequestBean) getObjectFromViewState("alumniBean")).getAlumni();
        RenderUtils.invalidateViewState();

        alumni.validateEmailFromRegistrationProcess();

        if (alumni.hasPastLogin()) {
            try {
                RegisterAlumniData.run(alumni, Boolean.TRUE);
                request.setAttribute("loginAlias", alumni.getLoginUsername());
                request.setAttribute("registrationResult", "true");
            } catch (FenixServiceException e) {
                request.setAttribute("registrationResult", "false");
            }

            request.setAttribute("passwordAccessBean", new AlumniPasswordBean(alumni));
            request.setAttribute("alumni", alumni);
            return mapping.findForward("alumniRegistrationResult");

        } else {
            request.setAttribute("passwordAccessBean", new AlumniPasswordBean(alumni, AlumniRequestType.PASSWORD_REQUEST));
            if (alumni.hasAnyPendingIdentityRequests()) {
                request.setAttribute("pendingRequests", "true");
            }
            return mapping.findForward("alumniCreatePasswordRequest");
        }
    }

    public ActionForward createPasswordRequestInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("publicAccessBean", getObjectFromViewState("publicAccessBean"));
        return mapping.findForward("alumniCreatePasswordRequest");
    }

    public ActionForward createPasswordRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniPasswordBean alumniBean = (AlumniPasswordBean) getObjectFromViewState("passwordAccessBean");

        try {
            RegisterAlumniData.run(alumniBean);
            request.setAttribute("loginAlias", alumniBean.getAlumni().getLoginUsername());
            request.setAttribute("registrationResult", "true");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey());
            request.setAttribute("registrationResult", "false");
            return mapping.findForward("alumniCreatePasswordRequest");
        }

        request.setAttribute("publicAccessBean", new AlumniPublicAccessBean(alumniBean.getAlumni()));
        request.setAttribute("alumni", alumniBean.getAlumni());
        return mapping.findForward("alumniPasswordRequired");
    }
}