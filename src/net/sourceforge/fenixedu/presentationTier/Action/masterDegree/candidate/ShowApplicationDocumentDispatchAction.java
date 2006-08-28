package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.io.DataOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.ApplicationDocumentType;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowApplicationDocumentDispatchAction extends FenixDispatchAction {

    /** request * */
    public static final String REQUEST_DOCUMENT_TYPE = "documentType";

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        request.setAttribute("candidateID", request.getParameter("candidateID"));
        
        return mapping.findForward("showApplicationDocumentsList");
    }

    public ActionForward showApplicationDocuments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors actionErrors = new ActionErrors();

        request.setAttribute("candidateID", request.getParameter("candidateID"));
        
        String documentTypeStr = request.getParameter(REQUEST_DOCUMENT_TYPE);

        InfoPerson infoPerson = readPersonByUsername(userView, actionErrors, request, mapping);

        if (infoPerson == null) {
            return mapping.findForward("notOK");
        }

        FileSuportObject file = null;
        try {
            Object args[] = {
                    infoPerson.getIdInternal(),
                    ((documentTypeStr != null) ? ApplicationDocumentType.valueOf(documentTypeStr)
                            : ApplicationDocumentType.CURRICULUM_VITAE) };
            file = (FileSuportObject) ServiceUtils.executeService(userView,
                    "RetrieveApplicationDocument", args);

        } catch (Exception e) {
            return mapping.findForward("notOK");
        }

        if (file != null) {
            response.setHeader("Content-disposition", "attachment;filename=" + file.getFileName());
            response.setContentType(file.getContentType());
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(file.getContent());
            dos.close();
            return null;
        }

        return mapping.findForward("notOK");
    }

    /**
     * 
     * @param userView
     * @param actionErrors
     * @param request
     * @param mapping
     * @return
     */
    private InfoPerson readPersonByUsername(IUserView userView, ActionErrors actionErrors,
            HttpServletRequest request, ActionMapping mapping) {
        InfoPerson result = null;

        try {
            Object[] args = { userView.getUtilizador() };
            result = (InfoPerson) ServiceUtils.executeService(userView, "ReadPersonByUsername", args);
            return result;
        } catch (ExcepcaoInexistente e) {
            actionErrors.add("unknownPerson", new ActionError("error.exception.nonExisting", userView
                    .getUtilizador()));
            saveErrors(request, actionErrors);
        } catch (FenixServiceException e) {
            actionErrors.add("unableReadPerson", new ActionError("errors.unableReadPerson", userView
                    .getUtilizador()));
            saveErrors(request, actionErrors);
        } catch (FenixFilterException e) {
            actionErrors.add("unableReadPerson", new ActionError("errors.unableReadPerson", userView
                    .getUtilizador()));
            saveErrors(request, actionErrors);
        }
        return null;
    }
}