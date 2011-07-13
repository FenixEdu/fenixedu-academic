/*
 * Created on 27/Mai/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.uploadFiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.DuplicateSibsPaymentFileProcessingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity.ProcessSibsPaymentFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Mapping(module = "manager", path = "/uploadFiles", input = "/uploadFiles.do?method=prepareChooseForUploadFiles&page=0", attribute = "chooseForUploadFilesForm", formBean = "chooseForUploadFilesForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "error", path = "/manager/uploadFiles/chooseForUploadFiles_error.jsp"),
		@Forward(name = "chooseForUploadFiles", path = "/manager/uploadFiles/chooseForUploadFiles.jsp"),
		@Forward(name = "firstPage", path = "/manager/uploadFiles/welcomeScreen.jsp") })
public class UploadFilesAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("firstPage");
    }

    public ActionForward prepareChooseForUploadFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String file = request.getParameter("file");
	request.setAttribute("file", file);

	return mapping.findForward("chooseForUploadFiles");
    }

    public ActionForward uploadGratuityFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();
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

	try {
	    ProcessSibsPaymentFile.run(fileName, fileEntries, userView);
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