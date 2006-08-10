package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultDocumentFilesManagementAction extends ResultsManagementAction {
    public ActionForward prepareManageDocumentFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null) { return backToResultList(mapping, form, request, response); }
        
        final ResultDocumentFileSubmissionBean bean = new ResultDocumentFileSubmissionBean(result);
        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
        
        return mapping.findForward("manageDocumentFiles");
    }

    public ActionForward createResultDocumentFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final ResultDocumentFileSubmissionBean bean = (ResultDocumentFileSubmissionBean) RenderUtils.getViewState().getMetaObject().getObject();

        try {
			ResultDocumentFile.create(bean);
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		} 
        
        return backToResult(mapping, form, request, response);
    }
    
    public ActionForward removeResultDocumentFile(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return backToResult(mapping, form, request, response);
    }    
}
