package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.io.DataOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.ApplicationDocumentType;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

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