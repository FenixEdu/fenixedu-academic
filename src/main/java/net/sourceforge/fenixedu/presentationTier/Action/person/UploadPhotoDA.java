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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.UploadOwnPhoto;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean.UnableToProcessTheImage;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * Action to upload personal photographs.
 * 
 * @author Pedro Santos (pmrsa)
 */
@Mapping(module = "person", path = "/uploadPhoto", attribute = "voidForm", formBean = "voidForm", scope = "request",
        parameter = "method", functionality = VisualizePersonalInfo.class)
@Forwards(value = {
        @Forward(name = "visualizePersonalInformation", path = "/person/visualizePersonalInfo.jsp", tileProperties = @Tile(
                title = "private.personal.dspace.information")),
        @Forward(name = "confirm", path = "/person/uploadPhoto.jsp", tileProperties = @Tile(
                title = "private.personal.dspace.information")),
        @Forward(name = "upload", path = "/person/uploadPhoto.jsp", tileProperties = @Tile(
                title = "private.personal.dspace.information")) })
public class UploadPhotoDA extends FenixDispatchAction {
    private static final int MAX_RAW_SIZE = 1000000; // 2M

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
        if (photo.getFileInputStream() == null) {
            actionMessages.add("fileRequired", new ActionMessage("errors.fileRequired"));
            saveMessages(request, actionMessages);
            return prepare(mapping, actionForm, request, response);
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

    public ActionForward preview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String filename = request.getParameter("file");
        FileInputStream file = new FileInputStream(filename);
        DataOutputStream output = new DataOutputStream(response.getOutputStream());
        output.write(new ByteArray(file).getBytes());
        output.close();
        return null;
    }

    public ActionForward save(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PhotographUploadBean photo = getRenderedObject();
        RenderUtils.invalidateViewState();

        UploadOwnPhoto.run(new ByteArray(photo.getFileInputStream()).getBytes(),
                ContentType.getContentType(photo.getContentType()));
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward cancelSubmission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Photograph photo = AccessControl.getPerson().getPersonalPhotoEvenIfRejected();
        if (photo != null) {
            photo.cancelSubmission();
        }
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("visualizePersonalInformation");
    }

}