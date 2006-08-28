package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

public class InsertApplicationDocumentsAction extends FenixDispatchAction {

    /** file names * */
    private final String CANDIDATE_CH_FNAME = "candidateCH";

    private final String CANDIDATE_CH2_FNAME = "candidateCH2";

    private final String CANDIDATE_CMI_FNAME = "candidateCMI";

    private final String CANDIDATE_CV_FNAME = "candidateCV";

    /** files extensions * */
    private final String PDF_EXTENSION = ".pdf";

    private final String DOC_EXTENSION = ".doc";

    private final String PDF_CAPS_EXTENSION = ".PDF";

    private final String DOC_CAPS_EXTENSION = ".DOC";

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm appDocForm = (DynaActionForm) form;

        Integer candidateID = new Integer(request.getParameter("candidateID"));

        appDocForm.set("candidateID", candidateID);
        request.setAttribute("candidateID", candidateID);

        return mapping.findForward("chooseFile");

    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        DynaActionForm appDocForm = (DynaActionForm) form;
        FormFile cvFormFile = (FormFile) appDocForm.get("cvFile");
        FormFile cmiFormFile = (FormFile) appDocForm.get("cmiFile");
        FormFile chFormFile = (FormFile) appDocForm.get("chFile");
        FormFile chFormFile2 = (FormFile) appDocForm.get("chFile2");

        // transport candidate ID
        Integer candidateID = new Integer(request.getParameter("candidateID"));
        if (candidateID == null) {
            candidateID = (Integer) appDocForm.get("candidateID");
        }
        appDocForm.set("candidateID", candidateID);
        request.setAttribute("candidateID", candidateID);

        String username = userView.getUtilizador();
        ActionErrors actionErrors = new ActionErrors();

        InfoPerson infoPerson = getPessoaByUsername(username, userView, actionErrors);
        if (infoPerson == null) {
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseFile");
        }

        if (!allFilesHaveKnownExtensions(new FormFile[] { cvFormFile, cmiFormFile, chFormFile,
                chFormFile2 })) {
            actionErrors.add("fnunknownextension", new ActionError(
                    "error.insertApplicationDocuments.invalidExtensions"));
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseFile");
        }

        FileSuportObject cvFile = prepareFileSupport(cvFormFile, CANDIDATE_CV_FNAME
                + (cvFormFile.getFileName().endsWith(PDF_EXTENSION) ? PDF_EXTENSION : DOC_EXTENSION));
        FileSuportObject cmiFile = prepareFileSupport(cmiFormFile, CANDIDATE_CMI_FNAME
                + (cmiFormFile.getFileName().endsWith(PDF_EXTENSION) ? PDF_EXTENSION : DOC_EXTENSION));
        FileSuportObject chFile = prepareFileSupport(chFormFile, CANDIDATE_CH_FNAME
                + (chFormFile.getFileName().endsWith(PDF_EXTENSION) ? PDF_EXTENSION : DOC_EXTENSION));
        FileSuportObject chFile2 = prepareFileSupport(chFormFile2, CANDIDATE_CH2_FNAME
                + (chFormFile2.getFileName().endsWith(PDF_EXTENSION) ? PDF_EXTENSION : DOC_EXTENSION));

        // vector de ficheiros que foram submetidos para
        // posterior comparacao com o resultado do servico
        Boolean[] submitedFiles = { ((cvFormFile.getFileName().equals("")) ? false : true),
                ((cmiFormFile.getFileName().equals("")) ? false : true),
                ((chFormFile.getFileName().equals("")) ? false : true),
                ((chFormFile2.getFileName().equals("")) ? false : true), };

        Object[] args1 = { cvFile, cmiFile, chFile, chFile2, infoPerson.getIdInternal() };
        Boolean[] serviceResult = null;

        try {
            serviceResult = (Boolean[]) ServiceUtils.executeService(userView,
                    "StoreApplicationDocuments", args1);
        } catch (Exception e) {
            actionErrors.add("error.Exception",new ActionError("error.Exception"));
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseFile");
        }

        if (!allFilesUploadedSucessfully(submitedFiles, serviceResult, actionErrors)) {
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseFile");
        }

        actionErrors.add("updateCompleted", new ActionError("label.appdocuments.submit.ok"));
        saveErrors(request, actionErrors);
        return mapping.findForward("chooseFile");
    }

    /** todos os ficheiros foram submetidos com sucesso ? quais nao foram ? * */
    private boolean allFilesUploadedSucessfully(Boolean[] submitedFiles, Boolean[] serviceResult,
            ActionErrors actionErrors) {
        boolean result = true;

        /***********************************************************************
         * algo correu mal! os ficheiros submetidos devem ser iguais aos
         * retornados pelo servico
         **********************************************************************/
        if (submitedFiles.length != serviceResult.length)
            return false;

        for (int i = 0; i < submitedFiles.length; i++) {
            // i+1 para nao comecar em zero

            if (!submitedFiles[i].booleanValue()) {
                actionErrors.add("fnotsubmited", new ActionError(
                        "label.applicationDocuments.fileNotSubmited", i + 1));
                continue;
            }

            if (submitedFiles[i].booleanValue() && serviceResult[i].booleanValue())
                actionErrors.add("fwellsubmited", new ActionError(
                        "label.applicationDocuments.fileSubmitOK", i + 1));
            else {
                actionErrors.add("fsubmitedNotOk", new ActionError(
                        "label.applicationDocuments.fileSubmitNotOK", i + 1));
                result = false;
            }
        }

        return result;
    }

    /** todas as extensoes sao conhecidas dos ficheiros * */
    private boolean allFilesHaveKnownExtensions(FormFile[] files) {
        for (FormFile file : files) {
            if (file.getFileName().equals("")) {
                continue;
            }
            if (!file.getFileName().endsWith(PDF_EXTENSION)
                    && !file.getFileName().endsWith(DOC_EXTENSION)
                    && !file.getFileName().endsWith(PDF_CAPS_EXTENSION)
                    && !file.getFileName().endsWith(DOC_CAPS_EXTENSION)) {
                return false;
            }
        }
        return true;
    }

    /** saber qual a pessoa que faz o upload * */
    private InfoPerson getPessoaByUsername(String username, IUserView userView, ActionErrors actionErrors) {
        try {
            Object[] args = { username };
            return (InfoPerson) ServiceUtils.executeService(userView, "ReadPersonByUsername", args);
        } catch (ExcepcaoInexistente e) {
            actionErrors.add("unknownPerson", new ActionError("error.exception.nonExisting", username));
        } catch (FenixServiceException e) {
            actionErrors.add("unableReadPerson", new ActionError("errors.unableReadPerson", username));
        } catch (FenixFilterException e) {
            actionErrors.add("unableReadPerson", new ActionError("errors.unableReadPerson", username));
        }
        return null;
    }

    /** prepara os objectos com os ficheiros que recebemos do form * */
    private FileSuportObject prepareFileSupport(FormFile formFile, String fileName) {
        FileSuportObject file = new FileSuportObject();

        if (formFile == null)
            return null;

        try {
            file.setContent(formFile.getFileData());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        file.setContentType(formFile.getContentType());

        file.setFileName(fileName);
        file.setLinkName(fileName);

        file.setContentType(formFile.getContentType());

        return file;
    }
}