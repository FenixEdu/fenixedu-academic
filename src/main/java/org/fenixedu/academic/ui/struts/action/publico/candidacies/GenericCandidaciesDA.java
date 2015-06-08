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
package org.fenixedu.academic.ui.struts.action.publico.candidacies;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.GenericApplication;
import org.fenixedu.academic.domain.candidacy.GenericApplicationFile;
import org.fenixedu.academic.domain.candidacy.GenericApplicationLetterOfRecomentation;
import org.fenixedu.academic.domain.candidacy.GenericApplicationRecomentation;
import org.fenixedu.academic.domain.candidacy.util.GenericApplicationPeriodBean;
import org.fenixedu.academic.domain.candidacy.util.GenericApplicationRecommendationBean;
import org.fenixedu.academic.domain.candidacy.util.GenericApplicationUploadBean;
import org.fenixedu.academic.domain.candidacy.util.GenericApplicationUserBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.period.GenericApplicationPeriod;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.publico.PublicApplication.PublicCandidaciesApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = PublicCandidaciesApp.class, path = "generic-applications", titleKey = "title.applications")
@Mapping(path = "/genericApplications", module = "publico")
@Forwards({
        @Forward(name = "genericApplications.listApplicationPeriods",
                path = "/publico/candidacy/genericApplications/listApplicationPeriods.jsp"),
        @Forward(name = "genericApplications.viewApplicationPeriod",
                path = "/publico/candidacy/genericApplications/viewApplicationPeriod.jsp"),
        @Forward(name = "genericApplications.confirmEmail", path = "/publico/candidacy/genericApplications/confirmEmail.jsp"),
        @Forward(name = "genericApplications.uploadRecommendation",
                path = "/publico/candidacy/genericApplications/uploadRecommendation.jsp") })
public class GenericCandidaciesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listApplicationPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final SortedSet<GenericApplicationPeriod> periods = GenericApplicationPeriod.getPeriods();
        request.setAttribute("periods", periods);

        final User userView = Authenticate.getUser();
        if (userView != null && RoleType.MANAGER.isMember(userView.getPerson().getUser())) {
            final GenericApplicationPeriodBean genericApplicationPeriodBean = new GenericApplicationPeriodBean();
            request.setAttribute("genericApplicationPeriodBean", genericApplicationPeriodBean);
        }

        return mapping.findForward("genericApplications.listApplicationPeriods");
    }

    public ActionForward viewApplicationPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final GenericApplicationPeriod applicationPeriod = getDomainObject(request, "applicationPeriodId");
        request.setAttribute("applicationPeriod", applicationPeriod);

        if (applicationPeriod.isCurrentUserAllowedToMange()) {
            final GenericApplicationUserBean genericApplicationUserBean = new GenericApplicationUserBean(applicationPeriod);
            request.setAttribute("genericApplicationUserBean", genericApplicationUserBean);
        }

        return mapping.findForward("genericApplications.viewApplicationPeriod");
    }

    public ActionForward createApplicationPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationPeriodBean bean = getRenderedObject("genericApplicationPeriodBean");
        bean.createNewPeriod();
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward addManager(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationUserBean bean = getRenderedObject("genericApplicationUserBean");
        bean.addManagerUser();
        request.setAttribute("changedManagerList", Boolean.TRUE);
        return viewApplicationPeriod(mapping, form, request, response);
    }

    public ActionForward removeManager(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationPeriod applicationPeriod = getDomainObject(request, "applicationPeriodId");
        final User user = getDomainObject(request, "userId");
        applicationPeriod.removeManagerService(user);
        request.setAttribute("changedManagerList", Boolean.TRUE);
        return viewApplicationPeriod(mapping, form, request, response);
    }

    public void justCreateApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationPeriod period = getDomainObject(request, "periodOid");
        final String email = (String) getFromRequest(request, "email");
        if (period != null && email != null) {
            final GenericApplication application = period.createApplication(email);
            request.setAttribute("sentEmailForApplication", application);
        }
    }

    public ActionForward createApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            justCreateApplication(mapping, form, request, response);
        } catch (DomainException ex1) {
            addActionMessage(request, ex1.getMessage());
        }
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward createApplicationFromPeriodPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            justCreateApplication(mapping, form, request, response);
        } catch (DomainException ex1) {
            addActionMessage(request, ex1.getMessage());
        }
        return viewApplicationPeriod(mapping, form, request, response);
    }

    public ActionForward confirmEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)) {
            request.setAttribute("application", application);
            if (application.getGenericApplicationPeriod().isOpen()) {
                request.setAttribute("uploadBean", new GenericApplicationUploadBean());
            }
            request.setAttribute("recommendationBean", new GenericApplicationRecommendationBean());
            return mapping.findForward("genericApplications.confirmEmail");
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward viewApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplication application = getDomainObject(request, "applicationId");
        if (application != null && application.getGenericApplicationPeriod().isCurrentUserAllowedToMange()) {
            request.setAttribute("application", application);
            return mapping.findForward("genericApplications.confirmEmail");
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward saveApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();
        request.setAttribute("applicationSaved", Boolean.TRUE);
        return confirmEmail(mapping, form, request, response);
    }

    public ActionForward submitApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)) {
            application.submitApplication();
            request.setAttribute("applicationSaved", Boolean.TRUE);
        }
        return confirmEmail(mapping, form, request, response);
    }

    public ActionForward uploadDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationUploadBean uploadBean = getRenderedObject("genericApplicationDocumentUploadFormFile");
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");

        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)) {
            try {
                final GenericApplicationFile file = uploadBean.uploadTo(application);
                if (file != null) {
                    RenderUtils.invalidateViewState();
                } else {
                    request.setAttribute("hasUploadFileError", Boolean.TRUE);
                }
                return confirmEmail(mapping, form, request, response);
            } catch (Error e) {
                addActionMessage(request, "message.file.could.not.read");
                request.setAttribute("hasUploadFileError", Boolean.TRUE);
                return confirmEmail(mapping, form, request, response);
            }
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward downloadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationFile file = getDomainObject(request, "fileExternalId");
        if (application != null
                && file != null
                && file.getGenericApplication() == application
                && ((confirmationCode != null && application.getConfirmationCode() != null && application.getConfirmationCode()
                        .equals(confirmationCode)) || application.getGenericApplicationPeriod().isCurrentUserAllowedToMange())) {
            response.setContentType(file.getContentType());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
            response.setContentLength(file.getSize().intValue());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(file.getContent());
            dos.close();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
            response.getWriter().close();
        }
        return null;
    }

    public ActionForward downloadRecomendationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationLetterOfRecomentation file = getDomainObject(request, "fileExternalId");
        if (file != null
                && file.getRecomentation() != null
                && file.getRecomentation().getConfirmationCode() != null
                && ((file.getRecomentation().getGenericApplication().getGenericApplicationPeriod().isCurrentUserAllowedToMange()) || file
                        .getRecomentation().getConfirmationCode().equals(confirmationCode))) {
            response.setContentType(file.getContentType());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
            response.setContentLength(file.getSize().intValue());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(file.getContent());
            dos.close();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
            response.getWriter().close();
        }
        return null;
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationFile file = getDomainObject(request, "fileExternalId");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode) && file != null
                && file.getGenericApplication() == application) {
            FenixFramework.atomic(() -> file.delete());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
            response.getWriter().close();
        }
        return confirmEmail(mapping, form, request, response);
    }

    public ActionForward requestRecommendation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationRecommendationBean recommendationBean = getRenderedObject("recommendationBean");
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode)) {
            recommendationBean.requestRecommendation(application);
            RenderUtils.invalidateViewState();
            return confirmEmail(mapping, form, request, response);
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward deleteRecommendationRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationRecomentation recommendation = getDomainObject(request, "recomentationId");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode) && recommendation != null
                && recommendation.getGenericApplication() == application) {
            recommendation.sendEmailForRecommendationDelete();
            removeRecommendationFromApplication(application, recommendation);
            return confirmEmail(mapping, form, request, response);
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    @Atomic
    private void removeRecommendationFromApplication(GenericApplication application,
            GenericApplicationRecomentation recommendation) {
        application.removeGenericApplicationRecomentation(recommendation);
    }

    public ActionForward resendRecommendationRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final GenericApplication application = getDomainObject(request, "applicationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        final GenericApplicationRecomentation recomentation = getDomainObject(request, "recomentationId");
        if (application != null && confirmationCode != null && application.getConfirmationCode() != null
                && application.getConfirmationCode().equals(confirmationCode) && recomentation != null
                && recomentation.getGenericApplication() == application) {
            recomentation.sendEmailForRecommendation();
            return confirmEmail(mapping, form, request, response);
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

    public ActionForward uploadRecommendation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final GenericApplicationRecomentation recomentation = getDomainObject(request, "recommendationExternalId");
        final String confirmationCode = (String) getFromRequest(request, "confirmationCode");
        if (recomentation != null && confirmationCode != null && recomentation.getConfirmationCode() != null
                && recomentation.getConfirmationCode().equals(confirmationCode)) {
            GenericApplicationUploadBean uploadBean = getRenderedObject("uploadBean");
            if (uploadBean == null) {
                uploadBean = new GenericApplicationUploadBean();
                uploadBean.setDisplayName(BundleUtil.getString(Bundle.CANDIDATE, "label.recommendation.document"));
            } else {
                try {
                    final GenericApplicationLetterOfRecomentation file = uploadBean.uploadTo(recomentation);
                    if (file != null) {
                        RenderUtils.invalidateViewState();
                        request.setAttribute("recommendationSaved", Boolean.TRUE);
                    }
                } catch (Error e) {
                    addActionMessage(request, "message.file.could.not.read");
                }
            }
            request.setAttribute("uploadBean", uploadBean);
            request.setAttribute("recomentation", recomentation);
            return mapping.findForward("genericApplications.uploadRecommendation");
        }
        request.setAttribute("invalidOrIncorrectConfirmationCode", Boolean.TRUE);
        return listApplicationPeriods(mapping, form, request, response);
    }

}
