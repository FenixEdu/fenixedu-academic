package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultDocumentFilesManagementAction extends ResultsManagementAction {
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final ResearchResult result = getResultFromRequest(request);
        if (result == null) {
            return backToResultList(mapping, form, request, response);
        }

        setResDocFileRequestAttributes(request, result);
        String forwardTo = request.getParameter("forwardTo");
    	
        return mapping.findForward(forwardTo);
    }

    public ActionForward prepareAlter(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        request.setAttribute("editExisting", "editExisting");
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResultDocumentFileSubmissionBean bean = getRenderedObject("editBean");

        try {
            final Object[] args = { bean };
            executeService(request, "CreateResultDocumentFile", args);
        } catch (Exception e) {
            final ActionForward defaultForward = backToResultList(mapping, form, request, response);
            return processException(request, mapping, defaultForward, e);
        }

        RenderUtils.invalidateViewState("editBean");
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer documentFileId = getRequestParameterAsInteger(request, "documentFileId");

        try {
            final Object[] args = { documentFileId };
            executeService(request, "DeleteResultDocumentFile", args);
        } catch (Exception e) {
            final ActionForward defaultForward = backToResultList(mapping, form, request, response);
            return processException(request, mapping, defaultForward, e);
        }

        return prepareEdit(mapping, form, request, response);
    }

    private void setResDocFileRequestAttributes(HttpServletRequest request, ResearchResult result)
            throws FenixFilterException, FenixServiceException {
        final ResultDocumentFileSubmissionBean bean = new ResultDocumentFileSubmissionBean(result);
        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
    }

    @Override
    public ResultDocumentFileSubmissionBean getRenderedObject(String id) {
        return (ResultDocumentFileSubmissionBean) super.getRenderedObject(id);
    }
}
