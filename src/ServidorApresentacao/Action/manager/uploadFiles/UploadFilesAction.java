/*
 * Created on 27/Mai/2004
 *
 */
package ServidorApresentacao.Action.manager.uploadFiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.gratuity.masterDegree.DuplicateSibsPaymentFileProcessingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class UploadFilesAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("firstPage");
    }

    public ActionForward prepareChooseForUploadFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        String file = request.getParameter("file");
        request.setAttribute("file", file);

        return mapping.findForward("chooseForUploadFiles");
    }

    public ActionForward uploadGratuityFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm uploadGratuityFileForm = (DynaActionForm) actionForm;

        FormFile uploadedFile = (FormFile) uploadGratuityFileForm.get("uploadedFile");

        InputStreamReader input = new InputStreamReader(uploadedFile.getInputStream());
        BufferedReader reader = new BufferedReader(input);

        String line = null;
        List fileEntries = new ArrayList();

        while ((line = reader.readLine()) != null) {
            fileEntries.add(line);
        }

        reader.close();

        String fileName = uploadedFile.getFileName();

        Object args[] = { fileName, fileEntries, userView };

        try {
            ServiceUtils.executeService(userView, "ProcessSibsPaymentFile", args);
        } catch (DuplicateSibsPaymentFileProcessingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("duplicateSibsPaymentFileProcessing", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.findForward("error");

        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        ActionMessages messages = new ActionMessages();
        messages.add("message1", new ActionMessage("message.manager.uploadSIBSFileSuccess"));
        saveMessages(request, messages);

        return mapping.findForward("error");

    }

}