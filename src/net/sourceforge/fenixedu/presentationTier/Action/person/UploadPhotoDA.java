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
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

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
        parameter = "method")
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
        return mapping.findForward("upload");
    }

    public ActionForward upload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PhotographUploadBean photo = getRenderedObject();
        RenderUtils.invalidateViewState();

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
                new ByteArray(photo.getCompressedInputStream()).getBytes(), ContentType.getContentType(photo.getContentType()));
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
}