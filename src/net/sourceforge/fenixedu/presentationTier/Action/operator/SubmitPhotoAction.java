/*
 * Created on Aug 9, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.operator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileNameTooLongServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SubmitPhotoAction extends DispatchAction {

    public ActionForward preparePhotoUpload(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseFile");
    }

    public ActionForward photoUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm photoForm = (DynaActionForm) form;
        FormFile formFile = (FormFile) photoForm.get("theFile");
        String username = (String) photoForm.get("username");
        ActionErrors actionErrors = new ActionErrors();
        InfoPerson infoPerson = null;

        if (formFile == null || (formFile != null && formFile.getFileData().length == 0)) {
            actionErrors.add("fileRequired", new ActionError("errors.fileRequired"));
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseFile");
        }

        try {
            Object[] args = { username };
            infoPerson = (InfoPerson) ServiceUtils
                    .executeService(userView, "ReadPersonByUsername", args);
        } catch (ExcepcaoInexistente e) {
            actionErrors.add("unknownPerson", new ActionError("error.exception.nonExisting", username));
            saveErrors(request, actionErrors);
            //return preparePhotoUpload(mapping, form, request, response);
            return mapping.findForward("chooseFile");
        } catch (FenixServiceException e) {
            actionErrors.add("unableReadPerson", new ActionError("errors.unableReadPerson", username));
            saveErrors(request, actionErrors);
            //return preparePhotoUpload(mapping, form, request, response);
            return mapping.findForward("chooseFile");

        }

        // String fileType = formFile.getContentType().replaceFirst("/", ".");
        String fileName = "personPhoto.jpeg";
        //+ fileType.substring(fileType.indexOf("."));

        FileSuportObject file = new FileSuportObject();

        file.setContent(formFile.getFileData());
        file.setContentType(formFile.getContentType());

        file.setFileName(fileName);
        file.setLinkName(fileName);

        file.setContentType(formFile.getContentType());

        Object[] args1 = { file, infoPerson.getIdInternal() };
        Boolean serviceResult = null;

        try {
            serviceResult = (Boolean) ServiceUtils.executeService(userView, "StorePhoto", args1);
        } catch (FileAlreadyExistsServiceException e1) {
            actionErrors.add("fileAlreadyExists", new ActionError("errors.fileAlreadyExists", file
                    .getFileName()));
            saveErrors(request, actionErrors);
            //return preparePhotoUpload(mapping, form, request, response);
            return mapping.findForward("chooseFile");

        } catch (FileNameTooLongServiceException e1) {
            actionErrors.add("fileNameTooLong", new ActionError("errors.fileNameTooLong", file
                    .getFileName()));
            saveErrors(request, actionErrors);
            //return preparePhotoUpload(mapping, form, request, response);
            return mapping.findForward("chooseFile");

        } catch (FenixServiceException e1) {
            actionErrors.add("unableToStoreFile", new ActionError("errors.unableToStoreFile", file
                    .getFileName()));
            saveErrors(request, actionErrors);
            //return preparePhotoUpload(mapping, form, request, response);
            return mapping.findForward("chooseFile");

        }
        if (!serviceResult.booleanValue()) {
            actionErrors.add("fileTooBig", new ActionError("errors.fileTooBig", file.getFileName()));
            saveErrors(request, actionErrors);
            //return preparePhotoUpload(mapping, form, request, response);
            return mapping.findForward("chooseFile");
        }

        actionErrors.add("updateCompleted", new ActionError("label.operator.submit.ok", ""));
        saveErrors(request, actionErrors);
        //return preparePhotoUpload(mapping, form, request, response);
        return mapping.findForward("chooseFile");
    }
}