package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * Action to upload personal photographs.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class UploadPhotoDA extends FenixDispatchAction {
    private static final int MAX_RAW_SIZE = 1000000; // 2M

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("photo", new PhotographUploadBean());
	return mapping.findForward("upload");
    }

    public ActionForward upload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	PhotographUploadBean photo = (PhotographUploadBean) getRenderedObject();
	RenderUtils.invalidateViewState();

	ActionMessages actionMessages = new ActionMessages();
	if (photo.getFileInputStream() == null) {
	    actionMessages.add("fileRequired", new ActionMessage("errors.fileRequired"));
	    saveMessages(request, actionMessages);
	    return prepare(mapping, actionForm, request, response);
	}

	if (!photo.getContentType().equals("image/jpeg") && !photo.getContentType().equals("image/png")) {
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

	photo.processImage();
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
	PhotographUploadBean photo = (PhotographUploadBean) getRenderedObject();
	RenderUtils.invalidateViewState();
	Object[] args = { new ByteArray(photo.getFileInputStream()).getBytes(),
		new ByteArray(photo.getCompressedInputStream()).getBytes(), ContentType.getContentType(photo.getContentType()) };
	ServiceUtils.executeService("UploadOwnPhoto", args);
	return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward acknowledgeRejection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	executeService(request, "AcknowledgePhotographRejection", new Object[0]);
	return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward cancelSubmission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	executeService(request, "CancelPhotographSubmission", new Object[0]);
	return mapping.findForward("visualizePersonalInformation");
    }
}
