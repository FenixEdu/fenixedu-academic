package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.CreateResultDocumentFile;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.DeleteResultDocumentFile;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/result/resultDocumentFilesManagement", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "viewEditPublication", path = "/resultPublications/showPublication.do"),
        @Forward(name = "ListPublications", path = "/resultPublications/listPublications.do"),
        @Forward(name = "editPatent", path = "/resultPatents/showPatent.do"),
        @Forward(name = "listPatents", path = "/resultPatents/management.do"),
        @Forward(name = "editDocumentFiles", path = "/researcher/result/documents/editResultDocumentFiles.jsp") })
public class ResultDocumentFilesManagementAction extends ResultsManagementAction {
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final ResearchResult result = getResultFromRequest(request);
        if (result == null) {
            return backToResultList(mapping, form, request, response);
        }

        setResDocFileRequestAttributes(request, result);
        return mapping.findForward("editDocumentFiles");
    }

    public ActionForward prepareAlter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        request.setAttribute("editExisting", "editExisting");
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final ResultDocumentFileSubmissionBean bean = getRenderedObject("editBean");

        try {

            CreateResultDocumentFile.run(bean);
        } catch (Exception e) {
            final ActionForward defaultForward = backToResultList(mapping, form, request, response);
            return processException(request, mapping, defaultForward, e);
        }

        RenderUtils.invalidateViewState("editBean");
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final Integer documentFileId = getRequestParameterAsInteger(request, "documentFileId");

        try {

            DeleteResultDocumentFile.run(documentFileId);
        } catch (Exception e) {
            final ActionForward defaultForward = backToResultList(mapping, form, request, response);
            return processException(request, mapping, defaultForward, e);
        }

        return prepareEdit(mapping, form, request, response);
    }

    private void setResDocFileRequestAttributes(HttpServletRequest request, ResearchResult result) throws 
            FenixServiceException {
        final ResultDocumentFileSubmissionBean bean = new ResultDocumentFileSubmissionBean(result);
        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
    }

    @Override
    public ResultDocumentFileSubmissionBean getRenderedObject(String id) {
        return (ResultDocumentFileSubmissionBean) super.getRenderedObject(id);
    }
}