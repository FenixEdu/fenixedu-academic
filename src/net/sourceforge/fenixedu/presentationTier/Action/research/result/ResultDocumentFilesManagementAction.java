package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.FileManagerException;

public class ResultDocumentFilesManagementAction extends ResultsManagementAction {
    public ActionForward prepareManageDocumentFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        final ResultDocumentFileSubmissionBean bean = new ResultDocumentFileSubmissionBean(result);
        
        request.setAttribute("bean", bean);
        return mapping.findForward("manageDocumentFiles");
    }

    public ActionForward createResultDocumentFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ResultDocumentFileSubmissionBean bean = (ResultDocumentFileSubmissionBean) RenderUtils.getViewState().getMetaObject().getObject();
        final Object[] args = { bean };

        try {
            ServiceUtils.executeService(getUserView(request), "CreateResultDocumentFile", args);
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
        } catch (FileManagerException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
        } catch (FenixServiceException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        }

        return backToResult(mapping, form, request, response);
    }
    
    public ActionForward removeResultDocumentFile(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return backToResult(mapping, form, request, response);
    }    
}
