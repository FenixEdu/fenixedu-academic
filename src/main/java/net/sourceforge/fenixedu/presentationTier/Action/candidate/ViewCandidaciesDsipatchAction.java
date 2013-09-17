/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy.SaveCandidacyDocumentFiles;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "candidate", path = "/viewCandidacies", attribute = "candidacyForm", formBean = "candidacyForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "uploadDocuments", path = "/candidate/uploadDocuments.jsp", tileProperties = @Tile(
                title = "private.candidate.applications")),
        @Forward(name = "viewDetail", path = "/candidate/viewCandidacyDetails.jsp", tileProperties = @Tile(
                title = "private.candidate.applications")),
        @Forward(name = "view", path = "/candidate/viewCandidacies.jsp", tileProperties = @Tile(
                title = "private.candidate.applications")) })
public class ViewCandidaciesDsipatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("view");
    }

    public ActionForward viewDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Candidacy candidacy = getCandidacy(request);
        request.setAttribute("canChangePersonalData", candidacy.getActiveCandidacySituation().canChangePersonalData());
        request.setAttribute("candidacy", candidacy);

        return mapping.findForward("viewDetail");
    }

    private Candidacy getCandidacy(HttpServletRequest request) {
        final String candidacyID = request.getParameter("candidacyID");
        for (final Candidacy candidacy : getUserView(request).getPerson().getCandidaciesSet()) {
            if (candidacy.getExternalId().equals(candidacyID)) {
                return candidacy;
            }
        }

        return null;
    }

    public ActionForward prepareUploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        fillRequest(request, getCandidacy(request));

        return mapping.findForward("uploadDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        List<CandidacyDocumentUploadBean> beans =
                (List<CandidacyDocumentUploadBean>) RenderUtils.getViewState("candidacyDocuments").getMetaObject().getObject();

        for (CandidacyDocumentUploadBean bean : beans) {
            bean.createTemporaryFile();
        }

        SaveCandidacyDocumentFiles.run(beans);

        for (CandidacyDocumentUploadBean bean : beans) {
            bean.deleteTemporaryFile();
        }

        fillRequest(request, getCandidacy(beans));

        return mapping.findForward("uploadDocuments");
    }

    private void fillRequest(HttpServletRequest request, Candidacy candidacy) {
        if (RenderUtils.getViewState("candidacyDocuments") != null) {
            RenderUtils.invalidateViewState("candidacyDocuments");
        }
        List<CandidacyDocumentUploadBean> candidacyDocuments = new ArrayList<CandidacyDocumentUploadBean>();
        for (CandidacyDocument candidacyDocument : candidacy.getCandidacyDocuments()) {
            candidacyDocuments.add(new CandidacyDocumentUploadBean(candidacyDocument));
        }

        request.setAttribute("candidacyDocuments", candidacyDocuments);
        request.setAttribute("candidacy", candidacy);
    }

    private Candidacy getCandidacy(List<CandidacyDocumentUploadBean> beans) {
        if (!beans.isEmpty()) {
            return beans.iterator().next().getCandidacyDocument().getCandidacy();
        }
        return null;
    }
}