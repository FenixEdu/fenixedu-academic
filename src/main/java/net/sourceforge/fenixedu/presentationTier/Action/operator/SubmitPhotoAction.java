package net.sourceforge.fenixedu.presentationTier.Action.operator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean.UnableToProcessTheImage;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(module = "operator", path = "/submitPhoto", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseFile", path = "/operator/photo/submitPhoto_bd.jsp", tileProperties = @Tile(
        title = "private.operator.submitphotos")) })
public class SubmitPhotoAction extends FenixDispatchAction {

    private static final int MAX_RAW_SIZE = 1000000; // 2M

    public ActionForward preparePhotoUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
        request.setAttribute("photo", new PhotographUploadBean());
        request.setAttribute("phroperCaption", bundle.getString("phroper.caption"));
        request.setAttribute("phroperSubCaption", bundle.getString("phroper.subCaption"));
        request.setAttribute("phroperButtonCaption", bundle.getString("phroper.buttonCaption"));
        request.setAttribute("phroperLoadingCaption", bundle.getString("phroper.loadingCaption"));
        request.setAttribute("buttonClean", bundle.getString("button.clean"));

        return mapping.findForward("chooseFile");
    }

    public ActionForward photoUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PhotographUploadBean photo = getRenderedObject();
        RenderUtils.invalidateViewState();
        String base64Image = request.getParameter("encodedPicture");
        if (base64Image != null) {
            photo.setFilename("mylovelypic.png");
            photo.setBase64RawThumbnail(base64Image.split(",")[1]);
            photo.setContentType(base64Image.split(",")[0].split(":")[1].split(";")[0]);
        }

        ActionMessages actionMessages = new ActionMessages();
        if (photo.getFileInputStream() == null) {
            actionMessages.add("error", new ActionMessage("errors.fileRequired"));
            saveMessages(request, actionMessages);
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        if (ContentType.getContentType(photo.getContentType()) == null) {
            actionMessages.add("error", new ActionMessage("errors.unsupportedFile"));
            saveMessages(request, actionMessages);
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        if (photo.getRawSize() > MAX_RAW_SIZE) {
            actionMessages.add("error", new ActionMessage("errors.fileTooLarge"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        try {
            photo.processImage();
        } catch (UnableToProcessTheImage e) {
            actionMessages.add("error", new ActionMessage("errors.unableToProcessImage"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        try {
            updatePersonPhoto(photo);
        } catch (Exception e) {
            actionMessages.add("error", new ActionMessage("errors.unableToSaveImage"));
            saveMessages(request, actionMessages);
            photo.deleteTemporaryFiles();
            return preparePhotoUpload(mapping, actionForm, request, response);
        }

        actionMessages.add("success", new ActionMessage("label.operator.submit.ok", ""));
        saveMessages(request, actionMessages);
        return preparePhotoUpload(mapping, actionForm, request, response);
    }

    @Service
    private void updatePersonPhoto(final PhotographUploadBean photo) throws FileNotFoundException, IOException {
        if (photo.getUsername() == null) {
            throw new DomainException("error.operatorPhotoUpload.null.username");
        }
        final Person person = Person.readPersonByUsername(photo.getUsername());
        if (person == null) {
            throw new DomainException("error.operatorPhotoUpload.invalid.username");
        }
        person.setPersonalPhoto(new Photograph(ContentType.getContentType(photo.getContentType()), new ByteArray(photo
                .getFileInputStream()), new ByteArray(photo.getCompressedInputStream()), PhotoType.INSTITUTIONAL));
    }
}