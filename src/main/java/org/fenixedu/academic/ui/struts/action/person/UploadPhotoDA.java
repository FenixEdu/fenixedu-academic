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
package org.fenixedu.academic.ui.struts.action.person;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean.UnableToProcessTheImage;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.fileManager.UploadOwnPhoto;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.person.UpdateEmergencyContactDA.EmergencyContactBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import com.google.common.io.ByteStreams;

/**
 * Action to upload personal photographs.
 * 
 * @author Pedro Santos (pmrsa)
 */
@Mapping(module = "person", path = "/uploadPhoto", functionality = VisualizePersonalInfo.class)
@Forwards({ @Forward(name = "visualizePersonalInformation", path = "/person/visualizePersonalInfo.jsp"),
        @Forward(name = "confirm", path = "/person/uploadPhoto.jsp"), @Forward(name = "upload", path = "/person/uploadPhoto.jsp") })
public class UploadPhotoDA extends FenixDispatchAction {
    private static final int MAX_RAW_SIZE = 1000000; // 2M

    public ActionForward togglePhotoAvailability(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        boolean availability = Boolean.parseBoolean(request.getParameter("available"));

        atomic(() -> {
            Authenticate.getUser().getPerson().setPhotoAvailable(availability);
        });

        request.setAttribute("personBean", new PersonBean(AccessControl.getPerson()));
        request.setAttribute("emergencyContactBean", new EmergencyContactBean(AccessControl.getPerson()));
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("photo", new PhotographUploadBean());
        request.setAttribute("phroperCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.caption"));
        request.setAttribute("phroperSubCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.subCaption"));
        request.setAttribute("phroperButtonCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.buttonCaption"));
        request.setAttribute("phroperLoadingCaption", BundleUtil.getString(Bundle.APPLICATION, "phroper.loadingCaption"));
        request.setAttribute("buttonClean", BundleUtil.getString(Bundle.APPLICATION, "button.clean"));
        request.setAttribute("buttonRevert", BundleUtil.getString(Bundle.APPLICATION, "button.phroper.revert"));
        return mapping.findForward("upload");
    }

    public ActionForward upload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PhotographUploadBean photo = getRenderedObject();
        RenderUtils.invalidateViewState();
        String base64Thumbnail = request.getParameter("encodedThumbnail");
        String base64Image = request.getParameter("encodedPicture");
        if (base64Image != null && base64Thumbnail != null) {
            DateTime now = new DateTime();
            photo.setFilename("mylovelypic_" + now.getYear() + now.getMonthOfYear() + now.getDayOfMonth() + now.getHourOfDay()
                    + now.getMinuteOfDay() + now.getSecondOfMinute() + ".png");
            photo.setBase64RawContent(base64Image.split(",")[1]);
            photo.setBase64RawThumbnail(base64Thumbnail.split(",")[1]);
            photo.setContentType(base64Image.split(",")[0].split(":")[1].split(";")[0]);
        }

        ActionMessages actionMessages = new ActionMessages();
        try (InputStream stream = photo.getFileInputStream()) {
            if (stream == null) {
                actionMessages.add("fileRequired", new ActionMessage("errors.fileRequired"));
                saveMessages(request, actionMessages);
                return prepare(mapping, actionForm, request, response);
            }
        }

        if (ContentType.getContentType(photo.getContentType()) == null) {
            actionMessages.add("fileUnsupported", new ActionMessage("errors.unsupportedFile"));
            saveMessages(request, actionMessages);
            return prepare(mapping, actionForm, request, response);
        }

        if (photo.getRawSize() > MAX_RAW_SIZE) {
            actionMessages.add("fileTooLarge", new ActionMessage("errors.fileTooLarge"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return prepare(mapping, actionForm, request, response);
        }

        try {
            photo.processImage();
        } catch (UnableToProcessTheImage e) {
            actionMessages.add("unableToProcessImage", new ActionMessage("errors.unableToProcessImage"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return prepare(mapping, actionForm, request, response);
        }
        photo.createTemporaryFiles();

        request.setAttribute("preview", true);
        request.setAttribute("photo", photo);
        return mapping.findForward("confirm");
    }

    public ActionForward save(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PhotographUploadBean photo = getRenderedObject();
        RenderUtils.invalidateViewState();

        try (InputStream stream = photo.getFileInputStream()) {
            UploadOwnPhoto.run(ByteStreams.toByteArray(stream), ContentType.getContentType(photo.getContentType()));
        }
        final Person person = Authenticate.getUser().getPerson();
        request.setAttribute("personBean", new PersonBean(person));
        EmergencyContactBean emergencyContactBean = new EmergencyContactBean(person);
        request.setAttribute("emergencyContactBean", emergencyContactBean);
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("personBean", new PersonBean(AccessControl.getPerson()));
        request.setAttribute("emergencyContactBean", new EmergencyContactBean(AccessControl.getPerson()));
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward cancelSubmission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Photograph photo = AccessControl.getPerson().getPersonalPhotoEvenIfRejected();
        if (photo != null) {
            photo.cancelSubmission();
        }
        request.setAttribute("personBean", new PersonBean(AccessControl.getPerson()));
        request.setAttribute("emergencyContactBean", new EmergencyContactBean(AccessControl.getPerson()));
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("personBean", new PersonBean(AccessControl.getPerson()));
        request.setAttribute("emergencyContactBean", new EmergencyContactBean(AccessControl.getPerson()));
        return mapping.findForward("visualizePersonalInformation");
    }

}